package com.userservice.controller;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.userservice.Exception.MyException;
import com.userservice.common.Result;
import com.userservice.domain.User;
import com.userservice.service.UserService;
import com.userservice.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.userservice.common.constants.*;

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
    /**
     * @description: 登录接口
     * @author Shawn i
     * @date: 2024/5/8 9:54
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletRequest request){
        User getUser = userService.login(user,request);
        if(getUser == null)
            return new Result(CODE_500,"用户名或密码有误");

        return new Result(CODE_200,"登录成功",getUser);
    }
    /**
     * @description: 注册接口
     * @author Shawn i
     * @date: 2024/5/8 9:54
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        User register = userService.register(user);
        return new Result(CODE_200,"注册成功",register);
    }
    /**
     * @description: 校验token接口
     * @author Shawn i
     * @date: 2024/5/8 9:55
     */
    @PostMapping("/verifyToken")
    public Boolean verifyToken(@RequestParam String token){
        if(StrUtil.isBlank(token)){
            throw new MyException(CODE_401,"无token，请重新登录");
        }
        // 获取 token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new MyException(CODE_401,"token验证失败");
        }
        User user = userService.getById(userId);
        if (user == null) {
            throw new MyException(CODE_401,"用户不存在，请重新登录");
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getUserPassword())).build();
        try {
            jwtVerifier.verify(token); //验证token
        } catch (JWTVerificationException e) {
            throw new MyException(CODE_401,"token验证失败");
        }
        return true;
    }
}
