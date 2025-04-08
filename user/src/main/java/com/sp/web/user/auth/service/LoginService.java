package com.sp.web.user.auth.service;

import com.sp.web.user.auth.model.dto.LogoutResponseDto;
import com.sp.web.user.jwt.JwtUtil;
import com.sp.web.user.auth.mapper.LoginMapper;
import com.sp.web.user.auth.model.dto.CreateUserDto;
import com.sp.web.user.auth.model.dto.LoginUserDto;
import com.sp.web.user.auth.model.entity.UserEntity;
import com.sp.web.user.redis.service.RedisLoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil; // JWT 유틸 추가
    private final RedisLoginService redisLoginService;

    @Value("${jwt.expiration-time}")
    private long expirationTime;


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
        userEntity.setRole("ROLE_USER");

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
    public Map<String, String> postLogin(LoginUserDto dto) {

        log.info("================== login start ==================");

        // Spring Security의 AuthenticationManager를 사용해 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getUserPassword()) // 🔥 입력한 원래 비밀번호 사용
        );

        // 인증된 정보를 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Access + Refresh Token 발급
        String accessToken = jwtUtil.generateAccessToken(dto.getUserId());
        String refreshToken = jwtUtil.generateRefreshToken(dto.getUserId());

        redisLoginService.saveRefreshToken(dto.getUserId(), refreshToken);

        // 클라이언트에 둘 다 전달
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);

        // JWT 생성 후 반환
        return tokenMap;

    }

    public LogoutResponseDto postLogout(HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);

        if(token != null && jwtUtil.validateToken(token) ) {
            String userId = jwtUtil.getUserIdFromToken(token);

//            redisLoginService.deleteToken(token);
            redisLoginService.deleteRefreshToken(token);
            return new LogoutResponseDto(true, "로그아웃 성공", userId);
        }
        return new LogoutResponseDto(false, "유효하지 않은 토큰", null);
    }
}
