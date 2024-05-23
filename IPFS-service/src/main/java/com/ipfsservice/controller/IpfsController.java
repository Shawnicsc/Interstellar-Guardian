package com.ipfsservice.controller;

import com.ipfsservice.Exception.MyException;
import com.ipfsservice.common.Result;
import com.ipfsservice.domain.IpfsFile;
import com.ipfsservice.domain.entity.downloadDao;
import com.ipfsservice.domain.entity.fileDao;
import com.ipfsservice.service.IpfsFileService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.ipfsservice.common.constants.*;

/**
 * @author Shawn i
 * @version 1.0
 * @description: TODO
 * @date 2024/4/28 20:41
 */
@RestController
@RequestMapping("/ipfs")
public class IpfsController {
    @Autowired
    private IpfsFileService ipfsFileService;
    /**
     * @description: 上传字符串接口
     * @author Shawn i
     * @date: 2024/5/8 9:45
     */
    @PostMapping("/uploadStr")
    public Result uploadStr(@RequestBody IpfsFile ipfsFile){
        try {
            String result = ipfsFileService.uploadToIpfs(ipfsFile.getSecretKey().getBytes(StandardCharsets.UTF_8),ipfsFile.getUserid());
            if (result != null) {
                return new Result(CODE_200, "上传成功", result);
            } else {
                return new Result(CODE_500, "服务器错误");
            }
        } catch (Exception e) {
            throw new MyException(CODE_500,"服务器错误");
        }
    }
    /**
     * @description: 上传文件接口
     * @author Shawn i
     * @date: 2024/5/8 9:45
     */
    @PostMapping("/uploadFile")
    public Result uploadFile(@MultipartForm fileDao fileDao) {
        try {
            String result = ipfsFileService.uploadToIpfs(fileDao.getFile(),Long.parseLong(fileDao.getUserId()));
            if (result != null) {
                return new Result(CODE_200, "上传成功", result);
            } else {
                return new Result(CODE_500, "服务器错误");
            }
        } catch (Exception e) {
            throw new MyException(CODE_500,"服务器错误");
        }
    }
    /**
     * @description: 下载文件接口
     * @author Shawn i
     * @date: 2024/5/8 9:45
     */
    @GetMapping("/downloadFile")
    public void downloadFromIpfs(@RequestParam String hash, @RequestParam Long userId,HttpServletResponse response) {
        try {
            IpfsFile file = ipfsFileService.getByHash(hash,userId);
            String fileName = file.getFileName();
            byte[] bytes = ipfsFileService.downFromIpfs(hash);
            if(bytes!=null){
                // 清空response
                response.reset();
                // 设置响应头，告知浏览器下载文件
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\""); // 设置下载文件的默认名称
                System.out.println("response");
                // 获取输出流，将数据写入到浏览器
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
            }
            ipfsFileService.updateDownload(hash,userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new MyException(CODE_500,"下载失败");
        }
    }
    /**
     * @description: 下载字符接口
     * @author Shawn i
     * @date: 2024/5/8 9:45
     */
    @GetMapping("/downloadStr/{hash}")
    public Result downloadStr(@PathVariable String hash){
        try{
            byte[] bytes = ipfsFileService.downStr(hash);
            return new Result(CODE_200,"下载成功",new String(bytes));
        }catch (Exception e){
            throw new MyException(CODE_500,"下载失败");
        }
    }
    /**
     * @description: 分享码生成 接口
     * @author Shawn i
     * @date: 2024/5/8 9:46
     */
    @PostMapping("/share")
    public Result shareCode(downloadDao downloadDao){
        return new Result(CODE_200,"分享码生成成功",ipfsFileService.shareCode(downloadDao.getHash(),downloadDao.getUserId()));
    }
    /**
     * @description: 分享码校验接口
     * @author Shawn i
     * @date: 2024/5/8 9:46
     */
    @PostMapping("/check")
    public Result checkCode(downloadDao downloadDao){
        if(ipfsFileService.checkCode(downloadDao.getHash(),downloadDao.getUserId()) == null)
            return new Result(CODE_400,"分享码不存在");
        return new Result(CODE_200,"分享码存在",ipfsFileService.checkCode(downloadDao.getHash(),downloadDao.getUserId()));
    }
    /**
     * @description: 获取 hash list 接口
     * @author Shawn i
     * @date: 2024/5/8 9:46
     */
    @GetMapping("/getList")
    public List<IpfsFile> getList(@RequestParam Long userId){
        return ipfsFileService.getList(userId);
    }

    /**
     * @description: 获取已下载文件
     * @author Shawn i
     * @date: 2024/5/23 15:58
     */
    @GetMapping("getDownloadList")
    public List<IpfsFile> getDownloadList(@RequestParam Long userId){
        return ipfsFileService.getDownloadList(userId);
    }

    /**
     * @author: Shawn i
     * @description: 获取 IPFS 网络流量
     * @date: 2024/5/23 21:42
     **/
    @GetMapping("/getNet")
    public Result getNet(){
        return new Result(CODE_200,"网络流量获取成功",ipfsFileService.getNet());
    }
}
