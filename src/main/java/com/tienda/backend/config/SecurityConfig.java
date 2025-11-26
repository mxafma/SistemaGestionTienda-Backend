
package com.tienda.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tienda.backend.util.JwtUtil;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtil);

        http
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                // Allow unauthenticated access to auth endpoints (login/register)
                .requestMatchers("/api/auth/**").permitAll()
                // Only allow ADMIN role to delete compras
                .requestMatchers(HttpMethod.DELETE, "/api/compras/**").hasRole("ADMIN")
                // Allow unauthenticated creation of usuarios (public user registration endpoint)
                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                // Require authentication for create/update operations on other API routes
                .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/**").authenticated()
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
