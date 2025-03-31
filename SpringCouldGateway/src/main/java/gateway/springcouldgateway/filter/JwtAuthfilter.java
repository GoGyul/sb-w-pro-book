package gateway.springcouldgateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class JwtAuthfilter implements WebFilter {

    @Value("${jwt.secret}")// application.yml에서 설정할 예정
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

        if(request.getURI().getPath().equals("/user/**")) {
            return chain.filter(exchange);
        }

        //ㅎㅔ더에서 토큰 추출
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(token == null ||!token.startsWith("Bearer ") ) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();

        }

        token = token.replace("Bearer ", "");

        try {
            // 토큰 검증
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .build()
                    .verify(token);

            // 유효한 토큰이면 요청 계속 진행
            return chain.filter(exchange);
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
