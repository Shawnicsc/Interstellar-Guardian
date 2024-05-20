package com.ipfsservice.service;

import com.ipfsservice.domain.IpfsFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    String shareCode(String hash);
    /**
     * @description: 校验分享码
     * @author Shawn i
     * @date: 2024/5/8 9:47
     */
    String checkCode(String shareCode);
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
    IpfsFile getByHash(String hash);
}
