package com.sp.web.user.redis.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.web.user.redis.dto.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLoginService {

    @Qualifier("tokenInfoRedisTemplate")
    private final RedisTemplate<String, TokenInfo> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String LOGIN_PREFIX = "LOGIN:";
    private static final String REFRESH_PREFIX = "REFRESH:";

    @Value("${jwt.refresh-expiration-time}")
    private long refreshExpirationTime;

//    public void saveToken(String userId, String token , long expirationMillis) {
//        String key = LOGIN_PREFIX + token;
//        redisTemplate.opsForValue().set(key, userId, Duration.ofMillis(expirationMillis));
//        log.info("📦 Redis 저장: key={}, userId={}", key, userId);
//    }

    public void saveRefreshToken(String userId, String refreshToken) {
        String key = REFRESH_PREFIX + refreshToken;
        TokenInfo tokenInfo = new TokenInfo(userId, refreshToken);
        log.info("🔐 저장 전 TokenInfo = {}", tokenInfo.getClass().getName());
        redisTemplate.opsForValue()
                .set(key, tokenInfo, Duration.ofMillis(refreshExpirationTime));
        log.info("📦 Redis 저장: key={}, tokenInfo={}", key, tokenInfo);
    }

    public boolean isRefreshTokenValid(String userId, String token) {
        String key = REFRESH_PREFIX + token;
        TokenInfo saved = redisTemplate.opsForValue().get(key);  // TokenInfo로 받아야 함!

        return saved != null &&
                saved.getUserId().equals(userId) &&
                saved.getRefreshToken().equals(token);  // 실제 토큰 값 비교
    }

    public void deleteToken(String token) {
        redisTemplate.delete(LOGIN_PREFIX + token);
    }

    public void deleteRefreshToken(String token) {
        redisTemplate.delete(REFRESH_PREFIX + token);
    }

    public boolean isTokenValid(String token) {
        return redisTemplate.hasKey(LOGIN_PREFIX + token);
    }

    public TokenInfo getTokenInfo(String refreshToken) {
        String key = REFRESH_PREFIX + refreshToken;
        TokenInfo tokenInfo = redisTemplate.opsForValue().get(key);
        log.info("🔍 Redis 조회: key={}, tokenInfo={}", key, tokenInfo);
        return tokenInfo;
    }
}
