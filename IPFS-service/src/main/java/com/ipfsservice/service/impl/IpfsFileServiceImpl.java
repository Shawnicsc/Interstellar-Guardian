package com.ipfsservice.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ipfsservice.Exception.MyException;
import com.ipfsservice.domain.IpfsFile;
import com.ipfsservice.mapper.IpfsFileMapper;
import com.ipfsservice.service.IpfsFileService;
import com.ipfsservice.utils.RedisCacheClient;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ipfsservice.common.constants.*;

/**
* @author 13627
* @description 针对表【ipfsfile】的数据库操作Service实现
* @createDate 2024-04-28 21:00:11
*/
@Service
public class IpfsFileServiceImpl extends ServiceImpl<IpfsFileMapper, IpfsFile>
    implements IpfsFileService {
    // ipfs 的服务器地址和端口，与yaml文件中的配置对应
    @Value("${ipfs.config.multi-addr}")
    private String multiAddr;

    private IPFS ipfs;

    private Jedis jedis = RedisCacheClient.getResource(); // 获取redis 连接

    @PostConstruct
    public void setMultiAddr() {
        ipfs = new IPFS(multiAddr);
    } // 连接 IPFS 节点

    @Override
    public String uploadToIpfs(MultipartFile file, Long userId) throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile(file.getOriginalFilename(), null);
        // 将 MultipartFile 写入临时文件
        file.transferTo(tempFile);
        String fileName = file.getOriginalFilename();
        NamedStreamable.FileWrapper uploadFile = new NamedStreamable.FileWrapper(tempFile);

        MerkleNode addResult = ipfs.add(uploadFile).get(0);

        IpfsFile ipfsFile = new IpfsFile();
        ipfsFile.setFileName(fileName);
        ipfsFile.setHashcode(addResult.hash.toString());
        ipfsFile.setUserid(userId);
        this.save(ipfsFile);

        return addResult.hash.toString();
    }

    @Override
    public String uploadToIpfs(byte[] data, Long userId) throws IOException {
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(data);
        MerkleNode addResult = ipfs.add(file).get(0);

        IpfsFile ipfsFile = new IpfsFile();
        ipfsFile.setHashcode(addResult.hash.toString());
        ipfsFile.setUserid(userId);
        this.save(ipfsFile);

        // Store data in Redis
        jedis.setex(ipfsFile.getHashcode(), 3600, new String(data)); // Cache data for 1 hour

        return addResult.hash.toString();
    }


    @Override
    public byte[] downStr(String hash) {
        byte[] data = null;
        try {
            String hash1 = jedis.get(hash);
            if (hash1 != null)
                return hash1.getBytes(StandardCharsets.UTF_8);
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            throw new MyException(CODE_500, "服务器错误");
        }
        return data;
    }

    @Override
    public byte[] downFromIpfs(String hash) {
        byte[] data = null;
        try {
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            throw new MyException(CODE_500, "服务器错误");
        }
        if (data != null && data.length > 0) {
            return data;
        }
        return null;
    }

    @Override
    public String shareCode(String hash, Long userId) {
        // 检查 Redis 缓存中是否存在 secretKey
        String cachedSecretKey = jedis.get("secretKey:" + hash);
        if (cachedSecretKey != null) {
            return cachedSecretKey;
        }

        // 查找目标 hash
        QueryWrapper<IpfsFile> ipfsFileQueryWrapper = new QueryWrapper<>();
        ipfsFileQueryWrapper.eq("HashCode", hash);
        ipfsFileQueryWrapper.eq("userid", userId);
        IpfsFile file = getOne(ipfsFileQueryWrapper);
        if (file == null) {
            throw new MyException(CODE_401, "目标文件不存在");
        }

        // 生成新的 secretKey
        String temp = UUID.randomUUID().toString();
        String newSecretKey = DigestUtil.sha256Hex(temp + hash);
        file.setSecretKey(newSecretKey);
        file.setModifiedTime(new java.sql.Timestamp(System.currentTimeMillis()));

        // 更新 Redis 缓存中的 secretKey 和时间戳
        String redisKey = "secretKey:" + hash;
        jedis.set(redisKey, newSecretKey);
        jedis.setex(newSecretKey, 2 * 60 * 60, hash);
        jedis.set(redisKey + ":timestamp", String.valueOf(System.currentTimeMillis()));
        jedis.expire(redisKey, 2 * 60 * 60); // 两小时后过期
        jedis.setex(hash, 2 * 60 * 60, file.getFileName());

        // 更新数据库
        boolean update = updateById(file);
        if (!update) {
            throw new MyException(CODE_500, "服务器错误");
        }

        return newSecretKey;
    }

    public IpfsFile checkCode(String shareCode, Long userId) {
        // 检查 Redis 缓存中是否存在 hashcode
        String hashcode = jedis.get(shareCode);
        String filename = jedis.get(hashcode);

        if (hashcode != null) {
            IpfsFile ipfsFile = new IpfsFile();
            ipfsFile.setFileName(filename);
            ipfsFile.setHashcode(hashcode);
            return ipfsFile;
        }

        // 从数据库中查找对应的 hashcode
        QueryWrapper<IpfsFile> ipfsFileQueryWrapper = new QueryWrapper<>();
        ipfsFileQueryWrapper.eq("secretKey", shareCode);
        ipfsFileQueryWrapper.eq("userid", userId);
        IpfsFile one = getOne(ipfsFileQueryWrapper);
        if (one == null) {
            return null;
        }

        // 将映射关系存储到 Redis 中
        jedis.setex(shareCode, 2 * 60 * 60, one.getHashcode()); // 两小时后过期

        return one;
    }

    @Override
    public List<IpfsFile> getList(Long userid) {
        QueryWrapper<IpfsFile> ipfsFileQueryWrapper = new QueryWrapper<>();
        ipfsFileQueryWrapper.eq("userid", userid);
        return list(ipfsFileQueryWrapper);
    }

    @Override
    public IpfsFile getByHash(String hash, Long userId) {
        QueryWrapper<IpfsFile> ipfsFileQueryWrapper = new QueryWrapper<>();
        ipfsFileQueryWrapper.eq("HashCode", hash);
        ipfsFileQueryWrapper.eq("userid", userId);
        IpfsFile file = getOne(ipfsFileQueryWrapper);
        if (file == null)
            throw new MyException(CODE_401, "目标文件不存在");
        return file;
    }

    @Override
    public void updateDownload(String hash, Long userId) {
        QueryWrapper<IpfsFile> ipfsFileQueryWrapper = new QueryWrapper<>();

        ipfsFileQueryWrapper.eq("HashCode", hash);
        ipfsFileQueryWrapper.eq("userid", userId);
        IpfsFile file = getOne(ipfsFileQueryWrapper);
        if (file == null) {
            throw new MyException(CODE_401, "目标文件不存在");
        }
        file.setIsDownload("1+" + userId);
        updateById(file);
    }

    @Override
    public List<IpfsFile> getDownloadList(Long userId) {
        QueryWrapper<IpfsFile> ipfsFileQueryWrapper = new QueryWrapper<>();
        ipfsFileQueryWrapper.eq("isDownload", "1+" + userId);
        return list(ipfsFileQueryWrapper);
    }

    @Override
    public String getNet() {
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String format = formatter.format(date);
        try {
            Map stats = ipfs.stats.bw();
            Integer totalIn = (Integer) stats.get("TotalIn");
            Integer totalOut = (Integer) stats.get("TotalOut");
            jsonObject.put("TotalIn", totalIn);
            jsonObject.put("TotalOut", totalOut);
            jsonObject.put("time", format);
            return jsonObject.toString();
        } catch (IOException e) {
            throw new MyException(CODE_400, "获取流量异常");
        }
    }
}






