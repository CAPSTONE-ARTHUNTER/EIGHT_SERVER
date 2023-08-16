package com.example.eight.user.config.auth;

import com.example.eight.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity  //스프링 시큐리티 활성화
@Configuration      //스프링의 Configuration 정의
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 스프링 시큐리티의 보안기능 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                // URL별 권한 설정
                .authorizeHttpRequests(Requests ->
                        Requests
                                // 다음 URL은 인증 없어도 모두가 접근 가능 (수정 예정)
                                .requestMatchers(
                                        "/",
                                        "/css/**",
                                        "/image/**",
                                        "/js/**").permitAll()
                                // 다음 URL은 role이 USER여야 가능 (수정 예정)
                                .requestMatchers(
                                        "/api/v1/**").hasRole(Role.USER.name())
                                // 나머지 URL은 인증 필요
                                .anyRequest().authenticated()
                );

        httpSecurity
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                // OAuth 로그인 후 유저 정보 가져옴
                                .userInfoEndpoint(userInfo ->
                                        userInfo
                                                // OAuth2UserService 인터페이스를 커스텀한 Service클래스
                                                .userService(customOAuth2UserService)
                                )
                );

        httpSecurity
                .logout(logout ->
                        logout
                                // 로그아웃 성공시 "/"로 리다이렉션 (수정 예정)
                                .logoutSuccessUrl("/")
                );


        return httpSecurity.build();
    }
}
