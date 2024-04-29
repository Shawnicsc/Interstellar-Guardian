package com.ipfsservice.common;

import lombok.Data;

/**
 * @author Shawn i
 * @version 1.0
 * @description: TODO
 * @date 2024/4/27 20:56
 */
@Data
public class Result {
    private String code = "200"; // 状态码
    private String message; //消息
    private Object data; // 数据

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
