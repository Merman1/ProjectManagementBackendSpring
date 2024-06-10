package com.example.projectmanagementbackendspring.Project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Wyłączenie CSRF na potrzeby testów
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/projects/**").permitAll() // zezwól na wszystkie żądania do /api/auth/projects
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
