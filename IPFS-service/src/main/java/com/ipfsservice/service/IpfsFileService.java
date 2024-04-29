package com.ipfsservice.service;

import com.ipfsservice.domain.IpfsFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
* @author 13627
* @description 针对表【ipfsfile】的数据库操作Service
* @createDate 2024-04-28 21:00:11
*/
public interface IpfsFileService extends IService<IpfsFile> {
    /**
     * 指定path+文件名称,上传至ipfs
     * @return
     * @throws IOException
     */
    String uploadToIpfs(MultipartFile file,Long userId) throws IOException;

    /**
     * 将byte格式的数据,上传至ipfs
     * @param data
     * @return
     * @throws IOException
     */
    String uploadToIpfs(byte[] data,Long userId) throws IOException;

    /**
     * 根据Hash值,从ipfs下载内容,返回byte数据格式
     *
     * @param hash
     * @return
     */
    byte[] downFromIpfs(String hash);

    /**
     * 根据Hash值,从ipfs下载内容,并写入指定文件destFilePath
     *
     * @param hash
     * @param destFilePath
     */
    void downFromIpfs(String hash, String destFilePath);
}
