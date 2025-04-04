package gateway.springcouldgateway.config;

import gateway.springcouldgateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CustomRoute {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator cRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ms1", r -> r.path("/board/**")
                        .uri("http://localhost:8081"))
                .route("ms2",r->r.path("/auth/**")
                        .uri("http://localhost:8082"))
                .route("ms2-user", r -> r.path("/user/**")   // ✅ 추가된 라우트
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("http://localhost:8082"))
                .build();
    }

}
