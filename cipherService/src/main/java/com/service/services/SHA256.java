package com.service.services;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
/**
 * @description: SHA256 加解密
 * @author Shawn i
 * @date: 2024/3/13 15:57
 */
@Service
public class SHA256 {

    public String encrypt(String message){
        Digest bcSHA256 = new SHA256Digest();
        bcSHA256.update(message.getBytes(StandardCharsets.UTF_8),0,message.getBytes(StandardCharsets.UTF_8).length);
        byte[] bytes = new byte[bcSHA256.getDigestSize()];
        bcSHA256.doFinal(bytes,0);
        return convertByteToHexString(bytes);
    }

    // byte数组 转成16进制hash
    public String convertByteToHexString(byte[] bytes){
        String result= "";
        for (int i = 0; i < bytes.length; i++) {
            int temp = bytes[i] & 0xff;
            String tempHex = Integer.toHexString(temp);
            if(tempHex.length() < 2){
                result+="0"+tempHex;
            }
            else{
                result += tempHex;
            }
        }
        return result;
    }


}
