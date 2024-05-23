package com.service.services;

import org.bouncycastle.util.Arrays;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;

class AESTest {
    //算法名
    public static final String KEY_ALGORITHM = "AES";
    //加解密算法/模式/填充方式
    //可以任意选择，为了方便后面与iOS端的加密解密，采用与其相同的模式与填充方式
    //ECB模式只用密钥即可对数据进行加密解密，CBC模式需要添加一个参数iv
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
    public static final String KEY = "2021211973";

    //生成iv
    public static AlgorithmParameters generateIV() throws Exception{
        //iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x00);
        AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        params.init(new IvParameterSpec(iv));
        return params;
    }

    //转化成JAVA的密钥格式
    public static Key convertToKey(byte[] keyBytes) throws Exception{
        SecretKey secretKey = new SecretKeySpec(keyBytes,KEY_ALGORITHM);
        return secretKey;
    }

    //加密
    @Test
    public void encrypt() throws Exception {
        AlgorithmParameters iv = generateIV();
        byte[] data = "Hello,Bouncy Castle".getBytes(StandardCharsets.UTF_8);
        //转化为密钥
        // 将String类型的数字转换为BigInteger
        BigInteger originalBigInt = new BigInteger(KEY);

        // 左移96位，将10位数字扩展到128位
        BigInteger expandedBigInt = originalBigInt.shiftLeft(96);

        Key key = convertToKey(expandedBigInt.toByteArray());

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key,iv);
        byte[] bytes = cipher.doFinal(data);
        StringBuilder str = new StringBuilder();
        for(byte b : bytes)
            str.append(String.format("%x", b));
        String s = str.toString();
        System.out.println(s);
        for (int i = 0; i < bytes.length; i++) {
            System.out.printf("%x",bytes[i]);
        }
    }

    //解密
    public static byte[] decrypt(byte[] encryptedData,byte[] keyBytes,AlgorithmParameters iv) throws Exception{
        Key key = convertToKey(keyBytes);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key,iv);
        return cipher.doFinal(encryptedData);
    }
}