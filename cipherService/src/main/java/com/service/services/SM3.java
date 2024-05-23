package com.service.services;

import cn.hutool.crypto.SmUtil;
import org.springframework.stereotype.Service;
/**
 * @description: SM3 加解密
 * @author Shawn i
 * @date: 2024/3/13 15:57
 */
@Service
public class SM3 {

    public String encrypt(String message){
        return SmUtil.sm3(message);
    }

}
