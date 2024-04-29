package com.ipfsservice.controller;

import com.ipfsservice.Exception.MyException;
import com.ipfsservice.common.Result;
import com.ipfsservice.common.constants;
import com.ipfsservice.service.IpfsFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.ipfsservice.common.constants.CODE_200;
import static com.ipfsservice.common.constants.CODE_500;

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
}
