package com.service.controller;


import com.alibaba.fastjson2.JSONObject;
import com.service.entity.cipher;
import com.service.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @description: 服务提供者
 * @author Shawn i
 * @date: 2024/3/13 15:57
 */
@RestController
@RequestMapping("/cipher")
public class CipherController {
    @Autowired
    private AES aes;
    @Autowired
    private ECDSA ecd;
    @Autowired
    private SHA256 sha256;
    @Autowired
    private SM2 sm2;
    @Autowired
    private SM3 sm3;
    @Autowired
    private SM4 sm4;
    @Autowired
    private Paillier paillier;
    @PostMapping("/encrypt")
    public String encrypt( cipher cipher) throws Exception {
        JSONObject jsonObject = new JSONObject();
        switch (cipher.getAlg()){
            case "AES":
                jsonObject.put("encryptedText",aes.encrypt(cipher.getText()));
                return jsonObject.toString();
            case "ECDSA":
                jsonObject.put("encryptedText",ecd.encrypt(cipher.getText()));
                return jsonObject.toString();
            case "SHA256":
                jsonObject.put("encryptedText",sha256.encrypt(cipher.getText()));
                return jsonObject.toString();
            case "SM2":
                jsonObject.put("encryptedText",sm2.encrypt(cipher.getText()));
                return jsonObject.toString();
            case "SM3":
                jsonObject.put("encryptedText",sm3.encrypt(cipher.getText()));
                return jsonObject.toString();
            case "SM4":
                jsonObject.put("encryptedText",sm4.encrypt(cipher.getText()));
                return jsonObject.toString();
            case "Paillier":
                jsonObject.put("encryptedText",paillier.Encryption(new BigInteger(cipher.getText())).toString());
                return jsonObject.toString();
            default:
                jsonObject.put("encryptedText", "请输入正确的参数");
                return jsonObject.toString();
        }

    }
    @PostMapping("/decrypt")
    public String decrypt( cipher cipher) throws Exception {
        JSONObject jsonObject = new JSONObject();
        switch (cipher.getAlg()){
            case "AES":
                jsonObject.put("decryptedText",aes.decrypt(cipher.getText()));
                return jsonObject.toString();
            case "ECDSA":
                jsonObject.put("decryptedText",ecd.decrypt(cipher.getText()));
                return jsonObject.toString();
            case "SHA256":
                jsonObject.put("decryptedText","this alg can not decrypt");
                return jsonObject.toString();
            case "SM2":
                jsonObject.put("decryptedText",sm2.decrypt(cipher.getText()));
                return jsonObject.toString();
            case "SM3":
                jsonObject.put("decryptedText","this alg can not decrypt ");
                return jsonObject.toString();
            case "SM4":
                jsonObject.put("decryptedText",sm4.decrypt(cipher.getText()));
                return jsonObject.toString();
            case "Paillier":
                jsonObject.put("decryptedText",paillier.Decryption(new BigInteger(cipher.getText())).toString());
                return jsonObject.toString();
            default:
                jsonObject.put("decryptedText","请输入正确的参数");
                return jsonObject.toString();
        }
    }
}
