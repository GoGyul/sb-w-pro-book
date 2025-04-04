//package gateway.springcouldgateway.jwt;
//
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Component
//public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
//
//    /*
//    *   사용자가 보호된 API에 접근
//        Spring Security의 필터 체인이 JWT 토큰을 검증
//        JWT가 없거나 유효하지 않은 경우
//        이 클래스(JwtAuthenticationEntryPoint)가 실행되어 401 Unauthorized 응답을 반환
//    * */
//    @Override
//    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
//        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
//
//        String responseBody = "{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}";
//        DataBuffer buffer = exchange.getResponse()
//                .bufferFactory()
//                .wrap(responseBody.getBytes(StandardCharsets.UTF_8));
//
//        return exchange.getResponse().writeWith(Mono.just(buffer));
//    }
//
//}
