package com.service.services;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.*;

/**
 * @description: ECDSA -> ECIES ECC椭圆曲线加密解密应用
 * @author Shawn i
 * @date: 2024/3/13 15:57
 */

@Service
public class ECDSA {
      // 定义常量
      private static final String EC_ALGORITHM = "EC";
      private static final String EC_PROVIDER = "BC";
      private static final String ECIES_ALGORITHM = "ECIES";
      private KeyPair keyPair;

      public ECDSA() {
         try {
            Security.addProvider(new BouncyCastleProvider());
            generateEccKeyPair();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      private void generateEccKeyPair() {
         try {
            // 获取指定算法的密钥对生成器
            KeyPairGenerator generator = KeyPairGenerator.getInstance(EC_ALGORITHM, EC_PROVIDER);
            // 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
            generator.initialize(256);
            // 随机生成一对密钥（包含公钥和私钥）
            keyPair = generator.generateKeyPair();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      public String encrypt(String data) {
         try {
            PublicKey publicKey = keyPair.getPublic();
            Cipher cipher = Cipher.getInstance(ECIES_ALGORITHM, EC_PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] result = cipher.doFinal(data.getBytes());
            return Base64.encodeBase64String(result);
         } catch (Exception e) {
            e.printStackTrace();
         }
         return null;
      }

      public String decrypt(String data) {
         try {
            PrivateKey privateKey = keyPair.getPrivate();
            Cipher cipher = Cipher.getInstance(ECIES_ALGORITHM, EC_PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(Base64.decodeBase64(data));
            return new String(result);
         } catch (Exception e) {
            e.printStackTrace();
         }
         return null;
      }

}
