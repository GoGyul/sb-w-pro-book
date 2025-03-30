package com.sp.web.user.login.service;

import com.sp.web.user.login.mapper.LoginMapper;
import com.sp.web.user.login.model.CreateUserDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final LoginMapper loginMapper;

    public boolean createUser(CreateUserDto dto) {

        //아이디 중복 확인
        if (loginMapper.countByUserId(dto) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        return true;
    }
}
