package tech.kood.match_me;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tech.kood.match_me.user_management.features.accessToken.filters.JwtAuthenticationFilter;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable())
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(management -> management.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> {

                    // Allow unauthenticated access to authentication endpoints.
                    requests.requestMatchers(
                            "/api/*/user-management/auth/login",
                            "/api/*/user-management/auth/register",
                            "/api/*/user-management/auth/access-token",
                            "/api/*/user-management/auth/invalidate")
                            .permitAll();

                    // TEMPORARY: Allow unauthenticated access to profile
                    // endpoints for development
                    requests.requestMatchers("/api/profiles/**").permitAll();
                    requests.requestMatchers("/api/chatspace/**").permitAll();

                    // Allow unauthenticated access to health checks and
                    // actuator endpoints.
                    requests.requestMatchers("/actuator/**", "/hawtio/**")
                            .permitAll();

                    // Allow unauthenticated access to Swagger UI and API docs.
                    requests.requestMatchers("/swagger-ui.html",
                            "/swagger-ui/*", "/v3/api-docs",
                            "/v3/api-docs/swagger-config").permitAll();

                    // Require authentication for all other API endpoints.
                    requests.requestMatchers("/api/**").authenticated();

                    // Deny all other requests.
                    requests.anyRequest().permitAll();
                });

        return http.build();
    }

}
