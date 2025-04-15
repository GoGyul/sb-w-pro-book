package com.sp.web.user.auth.service;

import com.sp.web.user.auth.mapper.LoginMapper;
import com.sp.web.user.auth.model.entity.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        // UserEntity를 UserDetails로 반환
        return userEntity;  // UserEntity는 UserDetails를 구현하므로 직접 반환
    }
}
