package gateway.springcouldgateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
* @EnableRedisRepositories
    이 애노테이션은 Spring Data Redis에서 Redis Repositories를 활성화합니다.
    즉, Redis를 데이터 저장소로 사용하여 Repository 인터페이스를 자동으로 인식하고 활용할 수 있게 됩니다.
* */
@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    /*
    * redisProperties는 Spring Boot의 application.yml에서 정의된 Redis 관련 설정을 바인딩하는 데 사용됩니다.
    * 이를 통해 host, port, connect-timeout 등의 값을 가져옵니다.
    */
    private final RedisProperties redisProperties;

    /*
    redisConnectionFactory()
    redisConnectionFactory() 메서드는 RedisConnectionFactory를 생성하는 빈을 제공합니다.
    LettuceConnectionFactory는 Redis와의 연결을 관리하는 역할을 합니다.
    RedisStandaloneConfiguration을 사용하여 Redis 서버의 호스트와 포트를 설정합니다.
    */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setPort(redisProperties.getPort());
        return new LettuceConnectionFactory(config);
    }

    /*
    redisTemplate() 메서드는 RedisTemplate을 설정합니다.
    RedisTemplate은 Redis에 데이터를 읽고 쓰는 데 사용되는 기본 객체입니다.
    StringRedisSerializer를 사용하여 Redis 키와 값을 문자열로 직렬화합니다.
    이 직렬화 방식은 Redis에 데이터를 저장할 때 유용합니다.
    */
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
