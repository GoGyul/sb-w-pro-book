package eureka.eurekaserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*
     시큐리티 설정
     Eureka 서버는 내부망에서만 존재해야 된다.
     하지만 외부망에 구축하거나 내부망에서도 보안을 중요시
     해야하기 때문에 스프링 시큐리티 설정을 진행한다.
     시큐리티는 httpBasic 방식 코드를 작성하면 된다..
    * */

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((auth) -> auth.disable());
        http.authorizeHttpRequests((auth) -> auth.anyRequest().authenticated());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("admin")
                .password(bCryptPasswordEncoder().encode("1234"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

}
