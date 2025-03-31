package com.sp.web.user.login.service;

import com.sp.web.user.login.mapper.LoginMapper;
import com.sp.web.user.login.model.dto.CreateUserDto;
import com.sp.web.user.login.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final LoginMapper loginMapper;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public boolean createUser(CreateUserDto dto) {

        //아이디 중복 확인
        if (loginMapper.countByUserId(dto) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = encoder.encode(dto.getUserPassword());

        // 새로운 유저 객체 생성 (DB에 저장하기 위한 모델)
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(dto.getUserId());
        userEntity.setUserPassword(encodedPassword);
        userEntity.setGender(dto.getGender());
        userEntity.setBirthDate(dto.getBirthDate());

        // DB에 저장
        loginMapper.insertUser(userEntity);

        return true;
    }
}
