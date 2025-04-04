package com.sp.web.user.auth.service;

import com.sp.web.user.auth.mapper.LoginMapper;
import com.sp.web.user.auth.model.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginMapper loginMapper;

    public CustomUserDetailsService(LoginMapper loginMapper) {
        this.loginMapper = loginMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 사용자 정보 가져오기
        UserEntity userEntity = loginMapper.selectUserInfo(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return new org.springframework.security.core.userdetails.User(
                userEntity.getUserId(),
                userEntity.getUserPassword(), // 저장된 암호화된 비밀번호
                new ArrayList<>() // 권한 정보 (Roles) 추가 가능
        );
    }
}
