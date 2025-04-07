package com.sp.web.user.redis.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLoginService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String LOGIN_PREFIX = "LOGIN:";
    private static final String REFRESH_PREFIX = "REFRESH:";

    @Value("${jwt.refresh-expiration-time}")
    private long refreshExpirationTime;

    public void saveToken(String userId, String token , long expirationMillis) {
        String key = LOGIN_PREFIX + token;
        redisTemplate.opsForValue().set(key, userId, Duration.ofMillis(expirationMillis));
        log.info("📦 Redis 저장: key={}, userId={}", key, userId);
    }

    public void saveRefreshToken(String userId, String refreshToken) {
        String key = REFRESH_PREFIX + refreshToken;
        redisTemplate.opsForValue()
                .set(key, userId, Duration.ofMillis(refreshExpirationTime));
        log.info("📦 Redis 저장: key={}, userId={}", key, userId);
    }

    public boolean isRefreshTokenValid(String userId, String token) {
        String saved = redisTemplate.opsForValue().get(REFRESH_PREFIX + token);
        return saved != null && saved.equals(token);
    }

    public void deleteToken(String token) {
        redisTemplate.delete(LOGIN_PREFIX + token);
    }

    public boolean isTokenValid(String token) {
        return redisTemplate.hasKey(LOGIN_PREFIX + token);
    }

}
