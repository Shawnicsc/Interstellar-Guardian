package com.service.services;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
/**
 * @description: SM2 加解密
 * @author Shawn i
 * @date: 2024/3/13 15:57
 */
@Service
public class SM2 {

    private KeyPair pair;
    private byte[] privateKey ;
    private byte[] publicKey ;
    cn.hutool.crypto.asymmetric.SM2 sm2;

    public SM2(){
        pair = SecureUtil.generateKeyPair("SM2");
        privateKey = pair.getPrivate().getEncoded();
        publicKey = pair.getPublic().getEncoded();
        sm2 = SmUtil.sm2(privateKey, publicKey);

    }

    public String encrypt(String data) {
        String encryptStr = sm2.encryptBcd(data,KeyType.PublicKey);
        return encryptStr;
    }

    public String decrypt(String data){
        String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(data, KeyType.PrivateKey));
        return  decryptStr;
    }

}
