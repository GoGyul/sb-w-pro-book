package gateway.springcouldgateway.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLoginService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String LOGIN_PREFIX = "LOGIN:";
    private static final String REFRESH_PREFIX = "REFRESH:";

    public boolean isTokenValid(String token) {
        String key = LOGIN_PREFIX + token;
        Boolean hasKey = redisTemplate.hasKey(key);
        log.info("🔍 Redis에서 토큰 유효성 확인: key={}, 존재 여부={}", key, hasKey);
        return Boolean.TRUE.equals(hasKey);
    }

    public boolean isRefreshTokenValid(String token) {
        String key = REFRESH_PREFIX + token;
        Boolean hasKey = redisTemplate.hasKey(key);
        log.info("🔍 Redis에서 토큰 유효성 확인: key={}, 존재 여부={}", key, hasKey);
        return Boolean.TRUE.equals(hasKey);
    }

}
