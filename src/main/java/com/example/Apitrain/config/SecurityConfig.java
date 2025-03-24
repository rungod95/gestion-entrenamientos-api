package com.example.Apitrain.config;

import com.example.Apitrain.Security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Profile("!test")  // Evita que se cargue en tests
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/events/test", "/api/auth/login", "/api/auth/register", "/trainings/**", "/athletes/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll() // Permitir acceso a H2 Console
                        .requestMatchers( "/events/**","/facilities/**","trainers/**").authenticated() // Requiere autenticación para estas rutas
                        .anyRequest().authenticated() // Requiere autenticación para otras rutas
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // Deshabilitar protección contra clickjacking
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Filtro JWT
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
