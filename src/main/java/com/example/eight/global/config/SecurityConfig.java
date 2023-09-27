package com.example.eight.global.config;

import com.example.eight.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import com.example.eight.global.jwt.JwtAuthFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@EnableWebSecurity  //스프링 시큐리티 활성화
@Configuration      //스프링의 Configuration 정의
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;


    @Bean
    protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 스프링 시큐리티의 보안기능 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
//              .formLogin(AbstractHttpConfigurer::disable)     // 스프링 시큐리티의 Form Login 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)    // Bearer 방식 사용하므로 httpBasic 비활성화
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션 사용하지 않음
                )
                // URL별 권한 설정
                .authorizeHttpRequests(Requests ->
                        Requests
                                // 인증 없이 접근 가능한 URL TODO: 추가 예정
                                .requestMatchers(
                                        "/",                    // 루트 페이지
                                        "/app",                         // 메인 페이지
                                        "/app/login/google",
                                        "/css/**",
                                        "/image/**",
                                        "/js/**",
                                        "/favicon.ico"
                                ).permitAll()
                                // USER만 접근 가능한 URL (jwt 토큰이 인증되어야 USER로 인증함)
                                .requestMatchers(
                                        "/app/users/**").hasRole(Role.USER.name())
                                // ADMIN만 접근 가능한 URL
                                .requestMatchers(
                                        "/app/admin/**").hasRole((Role.ADMIN.name()))
                                // 나머지 URL은 인증 필요
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
                );

        httpSecurity
                .logout(logout ->
                        logout
                                // 로그아웃 성공시 "/"로 리다이렉션 TODO: (수정 예정)
                                .logoutSuccessUrl("/")
                );

        httpSecurity
                // 모든 request에서 jwt 토큰 검증하는 필터
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // TODO: cors 설정
        // TODO: 예외 처리 필터

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://capstone-eight.netlify.app"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Authorization-refresh", "Cache-Control", "Content-Type"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Authorization-refresh"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}