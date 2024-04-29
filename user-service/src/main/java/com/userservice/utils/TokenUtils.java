package com.userservice.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.userservice.domain.User;
import com.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author shawni
 * @Description TODO
 * @Date 2024/4/28 11:24
 * @Version 1.0
 */
public class TokenUtils {
    private static UserService staticUserService;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void setUserService(){
        staticUserService = userService;
    }
    /**
     *  生成 token
     * @author shawni
     * @date 2024/4/28 11:24
     */
    public static String genToken(String userId,String sign){
        return JWT.create().withAudience(userId) // 将 user id 保存到 token 里面
                .withExpiresAt(DateUtil.offsetHour(new Date(),2)) //2小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
    }
    /**
     *
     * @author shawni
     * @date 2024/4/28 11:24
     *  获取当前用户对象
     */

    public static User getCurrentUser(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");

            if(StrUtil.isNotBlank(token)){

                String userId = JWT.decode(token).getAudience().get(0);

                Integer id = Integer.valueOf(userId);
                return staticUserService.getById(id);

            }
        } catch (JWTDecodeException e) {
            return null;
        }

        return null;
    }

}
