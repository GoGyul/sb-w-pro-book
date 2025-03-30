package com.sp.web.user.login.mapper;

import com.sp.web.user.login.model.CreateUserDto;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {


    int countByUserId(CreateUserDto dto);

}
