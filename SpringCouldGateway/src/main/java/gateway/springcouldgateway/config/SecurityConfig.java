package gateway.springcouldgateway.config;

import gateway.springcouldgateway.filter.JwtFilter;
import gateway.springcouldgateway.jwt.JwtAccessDeniedHandler;
import gateway.springcouldgateway.jwt.JwtAuthenticationEntryPoint;
import gateway.springcouldgateway.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.server.WebFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final WebFilter webFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // WebFilter 빈을 명확하게 지정 (예: weightCalculatorWebFilter 사용)
    public SecurityConfig(TokenProvider tokenProvider,
                          @Qualifier("weightCalculatorWebFilter") WebFilter webFilter,
                          JwtAccessDeniedHandler jwtAccessDeniedHandler,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.tokenProvider = tokenProvider;
        this.webFilter = webFilter;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        JwtFilter jwtFilter = new JwtFilter(tokenProvider);
        /*
        WebFlux는 세션을 사용하지 않는 구조라서 따로
        SessionCreationPolicy.STATELESS를 설정할 필요가 없습니다.
        Spring MVC 기반의 HttpSecurity에서는 필요하지만,
        WebFlux에서는 기본 동작이 Stateless이므로 삭제해도 됩니다.
        WebFlux 기본 동작: Stateless이므로 별도로 설정하지 않아도 JWT 인증 방식과 잘 맞음 ✅
        */
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .addFilterAt(webFilter, SecurityWebFiltersOrder.CORS)
                .exceptionHandling(exception ->{
                    exception.accessDeniedHandler(jwtAccessDeniedHandler);
                    exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                })
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/api/v2/admin/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers("/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
