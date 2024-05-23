package com.service.services;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

class SM2Test {
    @Test
    public void encrypt() {
        String data = "11111";
        KeyPair pair = SecureUtil.generateKeyPair("SM2");
        byte[] privateKey = pair.getPrivate().getEncoded();
        byte[] publicKey = pair.getPublic().getEncoded();
        System.out.println(privateKey);
        System.out.println(publicKey);
        cn.hutool.crypto.asymmetric.SM2 sm2 = SmUtil.sm2(privateKey, publicKey);
        String encryptStr = sm2.encryptBcd(data, KeyType.PublicKey);
        System.out.println(encryptStr);
        String s = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
        System.out.println(s);


    }

    @Test
    public void decrypt(){
        String encryptStr = "";
        cn.hutool.crypto.asymmetric.SM2 sm2 = SmUtil.sm2();

    }

}