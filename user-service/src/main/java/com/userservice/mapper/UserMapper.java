package com.userservice.mapper;

import com.userservice.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 13627
* @description 针对表【user(用户)】的数据库操作Mapper
* @createDate 2024-04-28 14:29:05
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




