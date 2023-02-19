package com.lms.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication()
public class WebSecurityConfig  {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtRequestFilter jwtRequestFilter, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {

        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests().requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest()
                .authenticated().and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);

        return http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
}
