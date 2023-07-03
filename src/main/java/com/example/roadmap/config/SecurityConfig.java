package com.example.roadmap.config;

import com.example.roadmap.config.jwt.JwtAuthenticationFilter;
import com.example.roadmap.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정을 위한 클래스
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                // 세션을 사용하지 않을거라 세션 설정을 Stateless 로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 접근 권한 설정부
                .and().authorizeRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers("/user").hasRole("USER")
                .anyRequest().permitAll() // or authenticated()

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // JWT를 사용하기 위해서는 기본적으로 password encoder가 필요
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
