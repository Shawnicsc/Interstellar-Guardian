package com.userservice.service;

import com.userservice.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author 13627
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-04-28 14:29:05
*/
@Service
public interface UserService extends IService<User> {
    /**
     * @description: 登录
     * @author Shawn i
     * @date: 2024/3/13 15:57
     */
    User login(User user, HttpServletRequest request);
    /**
     * @description: 注册
     * @author Shawn i
     * @date: 2024/3/13 15:57
     */
    User register(User user);

}
