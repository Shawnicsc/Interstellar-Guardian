package com.ipfsservice.Exception;

import lombok.Getter;

/**
 * @Author shawni
 * @Description 自定义异常类
 * @Date 2024/4/28 11:40
 * @Version 1.0
 */
@Getter
public class MyException extends RuntimeException{
    private String code;

    public MyException(String code,String message) {
        super(message);
        this.code = code;
    }
}
