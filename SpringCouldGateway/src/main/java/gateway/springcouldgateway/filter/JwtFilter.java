//package gateway.springcouldgateway.filter;
//
///*
//* JwtFilter 필터는 Spring Security를 활용하여 JWT 토큰을 검증하고,
//* 유효한 토큰의 경우 사용자의 인증 정보를 SecurityContext에 저장하는 역할을 한다.
//* 이를 통해 인증된 사용자의 요청만 처리할 수 있도록 한다.
//* 이 필터가 요청당 한 번씩만 실행되도록 하기 위해서이다.
//* doFilterInternal 메서드가 실제 필터링 로직을 구현하는 곳으로, 요청이 들어올 때 마다 실행된다.
//*
//* 요청 헤더에서  'Authorization' 필드를 찾아, 그 값이 'Bearer '로 시작하는 경우, 토큰을 추출한다.
//* 추출한 토큰이 유효한지 tokenProvider를 통해 검사하고, 유효하면 Authentication 객체를 생성한다.
//* Authentication 객체를 SecurityContextHolder의 SecurityContext에 저장하여, 다음 필터나 요청 처리 과정에서 현재 사용자가 인증되었음을 알린다.
//* filterChain.doFilter를 호출하여 요청 및 응답을 다음 필터로 넘기거나 리소스에 도달한다.
//* */
//
//import gateway.springcouldgateway.jwt.TokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.ReactiveSecurityContextHolder;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.io.IOException;
//
///*
//*
//*/
//
//@RequiredArgsConstructor
//public class JwtFilter implements WebFilter {
//
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//    public static final String BEARER_PREFIX = "Bearer ";
//
//    private final TokenProvider tokenProvider;
//
//
//    /**
//     * filter()는 Spring WebFlux의 필터 체인에서 요청을 가로채는 메서드입니다.
//     * ServerWebExchange → 클라이언트의 HTTP 요청 및 응답을 포함하는 객체
//     * WebFilterChain → 다음 필터를 실행하는 체인
//     */
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
////        JWT 토큰을 요청 헤더에서 가져오기 위해 ServerHttpRequest를 사용합니다.
////        resolveToken(request) 메서드를 호출하여 JWT 토큰을 추출합니다.
//        ServerHttpRequest request = exchange.getRequest();
//        String token = resolveToken(request);
//
//        /*
//        tokenProvider.validateToken(token): 토큰 검증
//        tokenProvider.getAuthentication(token): 토큰에서 Authentication 객체 생성
//        SecurityContextHolder.createEmptyContext(): 새로운 SecurityContext 생성
//        context.setAuthentication(authentication): 사용자 인증 정보 저장
//        */
//        if (token != null && tokenProvider.validateToken(token)) {
//            Authentication authentication = tokenProvider.getAuthentication(token);
//            SecurityContext context = SecurityContextHolder.createEmptyContext();
//            context.setAuthentication(authentication);
//
//            /*
//            ReactiveSecurityContextHolder를 사용하여 비동기 SecurityContext를 설정
//            contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)))
//            이후의 요청 처리 과정에서 SecurityContext를 사용할 수 있도록 설정됨.
//            chain.filter(exchange): 다음 필터로 요청을 전달합니다.
//            */
//            return chain.filter(exchange)
//                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));
//        }
//
//        return chain.filter(exchange);
//    }
//
//    /*
//    HttpHeaders.AUTHORIZATION 값을 가져와 "Bearer " 접두사를 제거한 JWT 토큰을 반환합니다.
//    Bearer 접두사가 없는 경우 null을 반환합니다.
//    */
//    private String resolveToken(ServerHttpRequest request) {
//        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//}
