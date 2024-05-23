package com.ipfsservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ipfsservice.domain.IpfsFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
* @author 13627
* @description 针对表【ipfsFile】的数据库操作Service
* @createDate 2024-04-28 21:00:11
*/
public interface IpfsFileService extends IService<IpfsFile> {
    /**
     * @description: 上传文件
     * @author Shawn i
     * @date: 2024/5/8 9:48
     */
    String uploadToIpfs(MultipartFile file,Long userId) throws IOException;

    /**
     * @description: 将byte格式的数据,上传至ipfs
     * @author Shawn i
     * @date: 2024/5/8 9:48
     */
    String uploadToIpfs(byte[] data,Long userId) throws IOException;

    /**
     * @description: 根据Hash值,从ipfs下载内容,返回byte数据格式
     * @author Shawn i
     * @date: 2024/5/8 9:48
     */
    byte[] downStr(String hash);

   /**
    * @description: 根据Hash值,从ipfs下载内容,并写入指定文件destFilePath
    * @author Shawn i
    * @date: 2024/5/8 9:48
    */
    byte[] downFromIpfs(String hash);
    /**
     * @description: 生成分享码
     * @author Shawn i
     * @date: 2024/5/8 9:47
     */
    String shareCode(String hash,Long userId);
    /**
     * @description: 校验分享码
     * @author Shawn i
     * @date: 2024/5/8 9:47
     */
    IpfsFile checkCode(String shareCode,Long userId);
    /**
     * @description: 获取 hash list
     * @author Shawn i
     * @date: 2024/5/8 9:47
     */
    List<IpfsFile> getList(Long userid);
    /**
     * @description: 根据hash获取 ipfsFile
     * @author Shawn i
     * @date: 2024/5/20 16:30
     */
    IpfsFile getByHash(String hash,Long userId);

    /**
     * @description: 标识已下载
     * @author Shawn i
     * @date: 2024/5/23 15:48
     */
    void updateDownload(String hash,Long userId);

    /**
     * @description: 获取已下载文件
     * @author Shawn i
     * @date: 2024/5/23 15:50
     */
    List<IpfsFile> getDownloadList(Long userid);

    /**
     * @author: Shawn i
     * @description: 获取 IPFS 网络流量
     * @date: 2024/5/23 21:37
     **/
    String getNet();
}
