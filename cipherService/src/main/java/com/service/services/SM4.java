package com.service.services;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.stereotype.Service;

/**
 * @description: SM4 加解密
 * @author Shawn i
 * @date: 2024/3/13 15:57
 */
@Service
public class SM4 {
    SymmetricCrypto sm4 = SmUtil.sm4();

    public String encrypt(String message){
        return sm4.encryptHex(message);
    }

    public String decrypt(String encryptStr){
        return sm4.decryptStr(encryptStr, CharsetUtil.CHARSET_UTF_8);
    }
}
