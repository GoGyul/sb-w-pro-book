package gateway.springcouldgateway.filter;

import gateway.springcouldgateway.jwt.JwtTokenProvider;
import gateway.springcouldgateway.redis.RedisLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisLoginService redisLoginService;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider
          , RedisLoginService redisLoginService) {

        super(Config.class); // 이 줄도 꼭 있어야 함!
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisLoginService = redisLoginService;
    }

    /**
     * @param config
     * @return
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = extractToken(exchange.getRequest());
            if(token != null
                    && jwtTokenProvider.validateToken(token)
                    && redisLoginService.isBlacklisted(token)
            ) {
                return chain.filter(exchange);
            }
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }

    private String extractToken(ServerHttpRequest request) {
        String authHearder = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(authHearder != null && authHearder.startsWith("Bearer ")) {
            return authHearder.substring(7);
        }
        return null;
    }

    // ✅ 이거 없으면 Config 관련 오류 뜹니다!
    public static class Config {
        // 필터 설정값을 담을 수 있는 클래스 (지금은 비어있어도 OK)
    }

}
