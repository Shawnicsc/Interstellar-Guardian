//package com.userservice.Interceptor;
//
//import cn.hutool.core.util.StrUtil;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTDecodeException;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.userservice.Exception.MyException;
//import com.userservice.domain.User;
//import com.userservice.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import static com.userservice.common.constants.CODE_401;
//
///**
// * @Author shawni
// * @Description TODO
// * @Date 2023/10/31 18:59
// * @Version 1.0
// */
//public class JwtInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private UserService userService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
//        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token
//        // 如果不是映射到方法直接通过
//        if(!(object instanceof HandlerMethod)){
//            return true;
//        }
//
//        if(StrUtil.isBlank(token)){
//            throw new MyException(CODE_401,"无token，请重新登录");
//        }
//        // 获取 token 中的 user id
//        String userId;
//        try {
//            userId = JWT.decode(token).getAudience().get(0);
//        } catch (JWTDecodeException j) {
//            throw new MyException(CODE_401,"token验证失败");
//        }
//        User user = userService.getById(userId);
//        if (user == null) {
//            throw new MyException(CODE_401,"用户不存在，请重新登录");
//        }
//        // 验证 token
//        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getUserPassword())).build();
//        try {
//            jwtVerifier.verify(token); //验证token
//        } catch (JWTVerificationException e) {
//            throw new MyException(CODE_401,"token验证失败");
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//
//    }
//    @Override
//    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//
//    }
//}
//
