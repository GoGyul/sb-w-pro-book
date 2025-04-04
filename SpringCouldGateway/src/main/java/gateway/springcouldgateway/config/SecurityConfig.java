package gateway.springcouldgateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange -> exchange
                        .anyExchange().permitAll()                 // ✅ 나머지는 허용
                )
                .build();
    }

//    private final TokenProvider tokenProvider;
//    private final WebFilter webFilter;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//
//    // WebFilter 빈을 명확하게 지정 (예: weightCalculatorWebFilter 사용)
//    public SecurityConfig(TokenProvider tokenProvider,
//                          @Qualifier("weightCalculatorWebFilter") WebFilter webFilter,
//                          JwtAccessDeniedHandler jwtAccessDeniedHandler,
//                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
//        this.tokenProvider = tokenProvider;
//        this.webFilter = webFilter;
//        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
//        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//    }
//
//    /*
//    @Bean → Spring Security의 필터 체인을 설정하는 메서드
//    ServerHttpSecurity → WebFlux용 보안 설정을 구성하는 객체
//    * */
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
//        JwtFilter jwtFilter = new JwtFilter(tokenProvider);
//        /*
//        WebFlux는 세션을 사용하지 않는 구조라서 따로
//        SessionCreationPolicy.STATELESS를 설정할 필요가 없습니다.
//        Spring MVC 기반의 HttpSecurity에서는 필요하지만,
//        WebFlux에서는 기본 동작이 Stateless이므로 삭제해도 됩니다.
//        WebFlux 기본 동작: Stateless이므로 별도로 설정하지 않아도 JWT 인증 방식과 잘 맞음 ✅
//        */
//        http
//                //CSRF(Cross-Site Request Forgery) 보호 비활성화
//                //REST API는 세션 기반 인증이 없으므로 필요 없음
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .addFilterAt(webFilter, SecurityWebFiltersOrder.CORS)
//                .exceptionHandling(exception ->{
//                    exception.accessDeniedHandler(jwtAccessDeniedHandler); // 권한 없음 처리
//                    exception.authenticationEntryPoint(jwtAuthenticationEntryPoint); // 인증 실패 처리
//                })
//                .authorizeExchange(exchange -> exchange
//                        .pathMatchers("/admin/**").hasAuthority("ROLE_ADMIN") //ROLE_ADMIN 권한이 필요
//                        .pathMatchers("/auth/**", "/user/**").permitAll() //경로는 인증 없이 허용
//                        .anyExchange().authenticated() //그 외 모든 요청은 인증 필요
//                )
//                //JwtFilter를 Spring Security 인증 필터 위치에 추가
//                //JWT 토큰 검증 후 사용자 인증 처리
//                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);
//        //보안 설정을 적용하고 SecurityWebFilterChain 반환
//        return http.build();
//
//    }
//
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}
