package com.sp.web.user.redis.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLoginService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String LOGIN_PREFIX = "LOGIN:";

    public void saveToken(String userId, String token , long expirationMillis) {
        String key = LOGIN_PREFIX + token;
        redisTemplate.opsForValue().set(key, userId, Duration.ofMillis(expirationMillis));
        log.info("📦 Redis 저장: key={}, userId={}", key, userId);
    }

    public void deleteToken(String token) {
        redisTemplate.delete(LOGIN_PREFIX + token);
    }

    public boolean isTokenValid(String token) {
        return redisTemplate.hasKey(LOGIN_PREFIX + token);
    }

}
