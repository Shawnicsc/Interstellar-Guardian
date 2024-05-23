package com.service.services;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * @description: AES 加密解密
 * @author Shawn i
 * @date: 2024/3/13 15:57
 */
@Service
public class AES {
    //使用学号作为密钥输入
    private static final String key = "2021211973";
    private SymmetricCrypto aes;

    /**
     * @Author Shawni
     * 构造函数，对密钥进行处理
     */
    public AES(){
        byte[] Key = convertKey();
        //构建
        aes = new SymmetricCrypto(SymmetricAlgorithm.AES, Key);
    }
    public String encrypt(String message){
        return aes.encryptHex(message);
    }

    public String decrypt(String message){
        String decryptStr = aes.decryptStr(message, CharsetUtil.CHARSET_UTF_8);
        return decryptStr;
    }

    private byte[] convertKey(){
        // 将String类型的数字转换为BigInteger
        BigInteger originalBigInt = new BigInteger(key);

        // 左移96位，将10位数字扩展到128位
        BigInteger expandedBigInt = originalBigInt.shiftLeft(96);

        return expandedBigInt.toByteArray();
    }

}
