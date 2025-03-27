package gateway.springcouldgateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustonRoute {

    @Bean
    public RouteLocator cRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ms1", r -> r.path("/sample/**")
                        .uri("http://localhost:8081"))
                .route("ms2",r->r.path("/user/**")
                        .uri("http://localhost:8082"))
                .build();
    }

}
