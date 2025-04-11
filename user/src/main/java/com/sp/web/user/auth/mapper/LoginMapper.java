package com.sp.web.user.auth.mapper;

import com.sp.web.user.auth.model.dto.CreateUserDto;
import com.sp.web.user.auth.model.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {


    int countByUserId(CreateUserDto dto);

    boolean insertUser(UserEntity userEntity);

    UserEntity selectUserInfo(String userId);
}
