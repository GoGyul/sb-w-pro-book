package com.sp.web.user.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")  // yml 설정값 사용
    private String secretKey;

    @Value("${jwt.expiration-time}")  // 만료 시간 (밀리초)
    private long expirationTime;

    @Value("${jwt.refresh-expiration-time}")
    private long refreshExpirationTime; // ✅ 새 필드

    // ✅ JWT 토큰 생성
    public String generateAccessToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withClaim("type", "access")
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC256(secretKey));  // 🔥 Auth0 방식
    }

    //refreshToken
    public String generateRefreshToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withClaim("type", "refresh")
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationTime))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if(bearer != null && bearer.startsWith("Bearer ")){
            return bearer.substring(7);
        }
        return null;
    }

    // Authorization 헤더 문자열을 직접 파싱할 수 있게 오버로드
    public String resolveToken(String bearerHeader) {
        if (bearerHeader != null && bearerHeader.startsWith("Bearer ")) {
            return bearerHeader.substring(7);
        }
        return null;
    }

    public String getUserIdFromToken(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secretKey))
                .build()
                .verify(token);
        return jwt.getSubject();
    }


}
