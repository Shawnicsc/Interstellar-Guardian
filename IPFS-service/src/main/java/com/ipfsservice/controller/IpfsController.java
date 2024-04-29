package com.ipfsservice.controller;

import com.ipfsservice.Exception.MyException;
import com.ipfsservice.common.Result;
import com.ipfsservice.common.constants;
import com.ipfsservice.service.IpfsFileService;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.multihash.Multihash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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
    public Result uploadStr(@RequestParam String message, @RequestParam Long userId){
        try {
            String result = ipfsFileService.uploadToIpfs(message.getBytes(StandardCharsets.UTF_8),userId);
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
    public Result uploadFile(@RequestParam("file") MultipartFile file, @RequestParam Long userId) {
        try {
            String result = ipfsFileService.uploadToIpfs(file,userId);
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
            ipfsFileService.downFromIpfs(hash, response);
        } catch (Exception e) {
            throw new MyException(CODE_500,"下载失败");
        }
    }
    @PostMapping("/downloadStr")
    public Result downloadStr(@RequestParam String hash){
        try{
            byte[] bytes = ipfsFileService.downFromIpfs(hash);
            return new Result(CODE_200,"下载成功",new String(bytes));
        }catch (Exception e){
            throw new MyException(CODE_500,"下载失败");
        }
    }
    @PostMapping("/share")
    public Result shareCode(@RequestParam String hash){
        return new Result(CODE_200,"分享码生成成功",ipfsFileService.shareCode(hash));
    }
    @PostMapping("/check")
    public Result checkCode(@RequestParam String checkCode){
        if(ipfsFileService.checkCode(checkCode) == null)
            return new Result(CODE_400,"分享码不存在");
        return new Result(CODE_200,"分享码存在",ipfsFileService.checkCode(checkCode));
    }
}
