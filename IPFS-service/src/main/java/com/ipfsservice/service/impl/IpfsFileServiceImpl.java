package com.ipfsservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ipfsservice.domain.IpfsFile;
import com.ipfsservice.service.IpfsFileService;
import com.ipfsservice.mapper.IpfsFileMapper;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

        return addResult.hash.toString();
    }

    /**
     * @description: 下载 byte数据
     * @author Shawn i
     * @date: 2024/3/13 15:57
     */
    @Override
    public byte[] downFromIpfs(String hash) {
        byte[] data = null;
        try {
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * @description: 下载文件到指定文件夹 destFile -> D:/hhh.png  btw this function need to be improved
     * @author Shawn i
     * @date: 2024/3/13 15:57
     */
    @Override
    public void downFromIpfs(String hash, String destFile) {
        byte[] data = null;
        try {
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null && data.length > 0) {
            File file = new File(destFile);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}




