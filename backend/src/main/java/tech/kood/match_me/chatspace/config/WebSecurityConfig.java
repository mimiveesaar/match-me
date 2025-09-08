package tech.kood.match_me.chatspace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // allow all requests to WebSocket endpoints
                .requestMatchers("/ws/**").permitAll()
                // all other requests still require authentication
                .anyRequest().authenticated()
            )
            // disable CSRF for WebSocket/SockJS connections
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}