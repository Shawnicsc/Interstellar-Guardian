package com.ipfsservice.Exception;



import com.ipfsservice.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author shawni
 * @Description 全局异常处理器
 * @Date 2024/4/28 11:40
 * @Version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public Result bizExceptionHandler(MyException e) {
       return new Result(e.getCode(),e.getMessage());
    }

}
