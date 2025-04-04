package com.sp.web.user.login.mapper;

import com.sp.web.user.login.model.dto.CreateUserDto;
import com.sp.web.user.login.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {


    int countByUserId(CreateUserDto dto);

    void insertUser(UserEntity userEntity);

    UserEntity selectUserInfo(String userId);
}
