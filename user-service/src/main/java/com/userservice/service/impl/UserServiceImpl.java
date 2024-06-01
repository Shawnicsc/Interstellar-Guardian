package com.userservice.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.userservice.Exception.MyException;
import com.userservice.domain.User;
import com.userservice.mapper.UserMapper;
import com.userservice.service.UserService;
import com.userservice.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.userservice.common.constants.CODE_400;
import static com.userservice.common.constants.CODE_401;

/**
* @author 13627
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-04-28 14:29:05
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    private static final String SALT = "Shawni";
    /**
     * @description: 登录
     * @author Shawn i
     * @date: 2024/4/27 15:57
     */
    @Override
    public User login(User user, HttpServletRequest request) {
        if(StrUtil.hasEmpty(user.getUserName(),user.getUserPassword()))
            throw new MyException(CODE_400,"缺少参数");
        user.setUserPassword(SecureUtil.md5(user.getUserPassword()+SALT));
        User getUser = getUser(user.getUserName(), user.getUserPassword());
        if(getUser == null)
            throw new MyException(CODE_401,"未找到相应用户");

        String token = TokenUtils.genToken(getUser.getId().toString(), getUser.getUserPassword());
        getUser.setToken(token);
        request.getSession().setAttribute("token",token);

        return getUser;
    }
    /**
     * @description: 注册
     * @author Shawn i
     * @date: 2024/4/27 15:57
     */
    @Override
    public User register(User user) {
        if(StrUtil.hasEmpty(user.getUserName(),user.getUserPassword()))
            throw new MyException(CODE_400,"缺少参数");


        User getUser = getUser(user.getUserName(), user.getUserPassword());
        if(getUser != null){
            throw new MyException(CODE_401,"该用户已存在");
        }
        else{
            String safePassword = DigestUtil.sha256Hex(user.getUserPassword() + SALT );
            user.setUserPassword(safePassword);
            save(user);
        }
        return user;
    }
    /**
     * @description: 查找用户
     * @author Shawn i
     * @date: 2024/4/27 15:57
     */
    private User getUser(String userName, String userPassword){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userName",userName);
        userQueryWrapper.eq("userPassword",userPassword);
        User getUser = getOne(userQueryWrapper);
        return getUser;
    }

}




