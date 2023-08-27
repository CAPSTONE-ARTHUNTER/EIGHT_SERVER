package com.example.eight.global.config;

import com.example.eight.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.example.eight.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.example.eight.global.oauth2.service.CustomOAuth2UserService;
import com.example.eight.user.entity.Role;
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

@RequiredArgsConstructor
@EnableWebSecurity  //스프링 시큐리티 활성화
@Configuration      //스프링의 Configuration 정의
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final JwtAuthFilter jwtAuthFilter;


    @Bean
    protected SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 스프링 시큐리티의 보안기능 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
//              .formLogin(AbstractHttpConfigurer::disable)     // 스프링 시큐리티의 Form Login 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)    // Bearer 방식 사용하므로 httpBasic 비활성화
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 세션 사용하지 않음
                );

        httpSecurity
                // URL별 권한 설정
                .authorizeHttpRequests(Requests ->
                        Requests
                                // 다음 URL은 인증 없어도 모두가 접근 가능 TODO: (수정 예정)
                                .requestMatchers(
                                        "/",
                                        "/css/**",
                                        "/image/**",
                                        "/js/**").permitAll()
                                // 다음 URL은 role이 USER여야 가능 TODO: (수정 예정)
                                .requestMatchers(
                                        "/users/**").hasRole(Role.USER.name())
                                // 나머지 URL은 인증 필요
                                .anyRequest().authenticated()
                );

        httpSecurity
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .successHandler(oAuth2LoginSuccessHandler)      // OAuth2 로그인 성공시 Handler
                                .failureHandler(oAuth2LoginFailureHandler)      // OAuth2 로그인 실패시 Handler
                                // OAuth 로그인 후 유저 정보 가져옴
                                .userInfoEndpoint(userInfo ->
                                        userInfo
                                                .userService(customOAuth2UserService)   // OAuth2UserService 인터페이스를 커스텀한 Service클래스
                                )
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

        return httpSecurity.build();
    }
}
