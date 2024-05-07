package com.ipfsservice.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ipfsservice.Exception.MyException;
import com.ipfsservice.common.constants;
import com.ipfsservice.domain.IpfsFile;
import com.ipfsservice.service.IpfsFileService;
import com.ipfsservice.mapper.IpfsFileMapper;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.ipfsservice.common.constants.CODE_401;
import static com.ipfsservice.common.constants.CODE_500;

/**
* @author 13627
* @description 针对表【ipfsfile】的数据库操作Service实现
* @createDate 2024-04-28 21:00:11
*/
@Service
public class IpfsFileServiceImpl extends ServiceImpl<IpfsFileMapper, IpfsFile>
    implements IpfsFileService{
    // ipfs 的服务器地址和端口，与yaml文件中的配置对应
    @Value("${ipfs.config.multi-addr}")
    private String multiAddr;

    private IPFS ipfs;

    private Jedis jedis = RedisCacheClient.getResource();

    @PostConstruct
    public void setMultiAddr() {
        ipfs = new IPFS(multiAddr);
    }

    @Override
    public String uploadToIpfs(MultipartFile file,Long userId) throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile(file.getOriginalFilename(), null);
        // 将 MultipartFile 写入临时文件
        file.transferTo(tempFile);

        NamedStreamable.FileWrapper uploadFile = new NamedStreamable.FileWrapper(tempFile);

        MerkleNode addResult = ipfs.add(uploadFile).get(0);

        IpfsFile ipfsFile = new IpfsFile();
        ipfsFile.setHashcode(addResult.hash.toString());
        ipfsFile.setUserid(userId);
        this.save(ipfsFile);

        return addResult.hash.toString();
    }

    @Override
    public String uploadToIpfs(byte[] data,Long userId) throws IOException {
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(data);
        MerkleNode addResult = ipfs.add(file).get(0);

        IpfsFile ipfsFile = new IpfsFile();
        ipfsFile.setHashcode(addResult.hash.toString());
        ipfsFile.setUserid(userId);
        this.save(ipfsFile);
        if(StrUtil.isBlank(jedis.get(ipfsFile.getHashcode())))
            jedis.del(ipfsFile.getHashcode());
        jedis.set(ipfsFile.getHashcode(),new String(data));

        return addResult.hash.toString();
    }

    /**
     * @description: 下载 byte数据
     * @author Shawn i
     * @date: 2024/4/29 15:00
     */
    @Override
    public byte[] downStr(String hash) {
        byte[] data = null;
        try {
            String hash1 = jedis.get(hash);
            if(hash1 != null)
                return hash1.getBytes(StandardCharsets.UTF_8);
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            throw new MyException(CODE_500,"服务器错误");
        }
        return data;
    }

    /**
     * @description: 下载文件到浏览器默认地址，btw 文件名无法获取，ipfs 通过内容寻址，文件名和hash值无直接关联
     * @author Shawn i
     * @date: 2024/4/29 15:10
     */
    @Override
    public byte[] downFromIpfs(String hash) {
        byte[] data = null;
        try {
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            throw new MyException(CODE_500,"服务器错误");
        }
        if (data != null && data.length > 0) {
            return data;
        }
        return null;
    }

    @Override
    public String shareCode(String hash) {
        //查找目标hash
        QueryWrapper<IpfsFile> ipfsFileQueryWrapper = new QueryWrapper<>();
        ipfsFileQueryWrapper.eq("HashCode",hash);
        IpfsFile file = getOne(ipfsFileQueryWrapper);
        if(file == null)
            throw new MyException(CODE_401,"目标文件不存在");
        if(file.getSecretKey() != null)
            return file.getSecretKey();
        //设置分享码
        file.setSecretKey(SecureUtil.md5(file.getUserid()+hash));
        //更新数据库
        boolean update = updateById(file);
        if(!update)
            throw new MyException(CODE_500,"服务器错误");

        return file.getSecretKey();

    }

    @Override
    public String checkCode(String shareCode) {
        QueryWrapper<IpfsFile> ipfsFileQueryWrapper = new QueryWrapper<>();
        ipfsFileQueryWrapper.eq("secretKey",shareCode);
        IpfsFile one = getOne(ipfsFileQueryWrapper);
        if(one == null)
            return null;
        return one.getHashcode();
    }

    @Override
    public List<IpfsFile> getList(Long userid) {
        QueryWrapper<IpfsFile> ipfsFileQueryWrapper = new QueryWrapper<>();
        ipfsFileQueryWrapper.eq("userid",userid);
        List<IpfsFile> list = list(ipfsFileQueryWrapper);
        return list;
    }

}




