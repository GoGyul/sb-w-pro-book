package com.sp.web.user.login.service;

import com.sp.web.user.login.mapper.LoginMapper;
import com.sp.web.user.login.model.dto.CreateUserDto;
import com.sp.web.user.login.model.dto.LoginUserDto;
import com.sp.web.user.login.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(CreateUserDto dto) {

        //아이디 중복 확인
        if (loginMapper.countByUserId(dto) > 0) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getUserPassword());

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

    /*
1️⃣ loginService()에서 authenticationManager.authenticate() 실행
2️⃣ UsernamePasswordAuthenticationToken을 통해 아이디와 비밀번호를 전달
3️⃣ AuthenticationManager가 내부적으로 CustomUserDetailsService를 호출
4️⃣ CustomUserDetailsService가 DB에서 사용자 정보를 가져옴
5️⃣ Spring Security가 passwordEncoder.matches()를 사용해 입력한 비밀번호 검증
6️⃣ 인증이 성공하면 Authentication 객체가 반환됨
7️⃣ SecurityContextHolder에 인증 정보를 저장
    */
    public Boolean loginService(LoginUserDto dto) {

        log.info("================== login start ==================");

        // Spring Security의 AuthenticationManager를 사용해 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getUserPassword()) // 🔥 입력한 원래 비밀번호 사용
        );

        // 인증된 정보를 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;

    }
}
