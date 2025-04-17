package com.sp.web.user.auth.service;

import com.sp.web.user.auth.model.dto.*;
import com.sp.web.user.jwt.JwtUtil;
import com.sp.web.user.auth.mapper.LoginMapper;
import com.sp.web.user.auth.model.entity.UserEntity;
import com.sp.web.user.redis.dto.TokenInfo;
import com.sp.web.user.redis.service.RedisLoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CreateUserResponseDto createUser(CreateUserDto dto) {

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
        userEntity.setNickname(dto.getNickname());
        userEntity.setGender(dto.getGender());
        userEntity.setBirthDate(dto.getBirthDate());
        userEntity.setRole("ROLE_USER");

        // DB에 저장
        boolean isCreated = loginMapper.insertUser(userEntity);

        if (isCreated) {
            return new CreateUserResponseDto(true, dto.getUserId(), "회원가입 성공");
        } else {
            return new CreateUserResponseDto(false, null, "회원가입 실패: 이미 존재하는 사용자");
        }
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
    @Transactional
    public LoginResponseDto postLogin(LoginUserDto dto, HttpServletResponse response) {

        log.info("================== login start ==================");

        // Spring Security의 AuthenticationManager를 사용해 인증 처리
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getUserPassword()) // 🔥 입력한 원래 비밀번호 사용
        );

        UserEntity userEntity = (UserEntity) authentication.getPrincipal();

        // 인증된 정보를 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Access + Refresh Token 발급
        String accessToken = jwtUtil.generateAccessToken(dto.getUserId());
        String refreshToken = jwtUtil.generateRefreshToken(dto.getUserId());

        redisLoginService.saveRefreshToken(dto.getUserId(), refreshToken);

        // 🔐 refreshToken을 HttpOnly 쿠키로 설정
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false) // 개발용: true면 HTTPS 필요
                .path("/")     // 쿠키 경로
                .maxAge(7 * 24 * 60 * 60) // 7일
                .sameSite("Lax") // 필요에 따라 변경 가능 (Cross 요청이면 "None")
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        // JWT 생성 후 반환
        return new LoginResponseDto(true, accessToken, refreshToken, "로그인 성공", userEntity);

    }

    @Transactional
    public LogoutResponseDto postLogout(HttpServletRequest request, HttpServletResponse response) {
        String token = jwtUtil.resolveToken(request);

        // 토큰이 없는 경우 - 쿠키만 정리
        if (token == null) {
            log.info("로그아웃 요청 - 토큰 없음, 쿠키만 정리");
            clearRefreshTokenCookie(response);
            return new LogoutResponseDto(true, "세션 정보가 정리되었습니다.", null);
        }

        String userId = null;
        boolean isValidToken = false;

        // 토큰 유효성 확인 및 사용자 ID 추출
        try {
            userId = jwtUtil.getUserIdFromToken(token);
            isValidToken = jwtUtil.validateToken(token);
            log.info("로그아웃 요청 - 사용자: {}, 유효한 토큰: {}", userId, isValidToken);
        } catch (Exception e) {
            log.info("로그아웃 요청 - 만료되거나 유효하지 않은 토큰");
        }

        // 리프레시 토큰 삭제 시도
        try {
            redisLoginService.deleteRefreshToken(token);
            log.info("리프레시 토큰 삭제 성공");
        } catch (Exception e) {
            log.warn("리프레시 토큰 삭제 실패: {}", e.getMessage());
        }

        // 유효한 토큰이면 블랙리스트에 추가
        if (isValidToken) {
            try {
                long expiration = jwtUtil.getExpiration(token);
                redisLoginService.addToBlacklist(token, expiration);
                log.info("토큰을 블랙리스트에 추가 (만료 시간: {})", expiration);
            } catch (Exception e) {
                log.error("블랙리스트 추가 실패: {}", e.getMessage());
            }
        }

        // 항상 리프레시 토큰 쿠키 삭제
        clearRefreshTokenCookie(response);

        String message = userId != null ? "로그아웃 성공" : "만료된 세션이 정리되었습니다.";
        return new LogoutResponseDto(true, message, userId);
    }

    @Transactional
    public ReissueResponseDto postReissue(String refreshTokenHeader) {

        String refreshToken = jwtUtil.resolveToken(refreshTokenHeader);

        if(refreshTokenHeader == null || !jwtUtil.validateToken(refreshToken)) {
            return new ReissueResponseDto(false,  null,null,null, "유효하지 않은 리프레시 토큰");
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);

        TokenInfo tokenInfo = redisLoginService.getTokenInfo(refreshToken);
        if(tokenInfo == null || !tokenInfo.getUserId().equals(userId)) {
            return new ReissueResponseDto(false, null, null,null, "토큰이 일치하지 않거나 만료됨");
        }
        String newAccessToken = jwtUtil.generateAccessToken(userId);

        return new ReissueResponseDto(true, newAccessToken, null, userId, "AccessToken 재발급 완료");

    }

    // 리프레시 토큰 쿠키 삭제 메서드 분리
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", expiredCookie.toString());
    }

}
