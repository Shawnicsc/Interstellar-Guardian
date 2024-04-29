package com.userservice.controller;

import com.userservice.common.Result;
import com.userservice.domain.User;
import com.userservice.service.UserService;
import com.userservice.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static com.userservice.common.constants.CODE_200;
import static com.userservice.common.constants.CODE_500;

/**
 * @author Shawn i
 * @version 1.0
 * @description: TODO
 * @date 2024/4/28 13:48
 */
@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User getUser = userService.login(user);
        if(getUser == null)
            return new Result(CODE_500,"用户名或密码有误");

        return new Result(CODE_200,"登录成功",getUser);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        User register = userService.register(user);
        return new Result(CODE_200,"注册成功",register);
    }
}
