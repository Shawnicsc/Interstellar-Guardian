package com.ipfsservice.controller;

import com.ipfsservice.Exception.MyException;
import com.ipfsservice.common.Result;
import com.ipfsservice.domain.IpfsFile;
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
    @GetMapping("/downloadFile")
    public void downloadFromIpfs(@RequestParam String hash, HttpServletResponse response) {
        try {
            byte[] bytes = ipfsFileService.downFromIpfs(hash);
            if(bytes!=null){
                // 清空response
                response.reset();
                // 设置响应头，告知浏览器下载文件
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"ipfsFile.ext\""); // 设置下载文件的默认名称
                System.out.println("response");
                // 获取输出流，将数据写入到浏览器
                OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new MyException(CODE_500,"下载失败");
        }
    }
    @GetMapping("/downloadStr/{hash}")
    public Result downloadStr(@PathVariable String hash){
        try{
            byte[] bytes = ipfsFileService.downStr(hash);
            return new Result(CODE_200,"下载成功",new String(bytes));
        }catch (Exception e){
            throw new MyException(CODE_500,"下载失败");
        }
    }
    @PostMapping("/share")
    public Result shareCode(@RequestParam("hash") String hash){
        return new Result(CODE_200,"分享码生成成功",ipfsFileService.shareCode(hash));
    }
    @PostMapping("/check")
    public Result checkCode(@RequestParam(value = "hash") String checkCode){
        if(ipfsFileService.checkCode(checkCode) == null)
            return new Result(CODE_400,"分享码不存在");
        return new Result(CODE_200,"分享码存在",ipfsFileService.checkCode(checkCode));
    }
    @GetMapping("/getList")
    public List<IpfsFile> getList(@RequestParam Long userId){
        return ipfsFileService.getList(userId);
    }
}
