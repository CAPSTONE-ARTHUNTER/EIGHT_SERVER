package com.example.eight.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Optional;

/*
모든 request의 토큰 검사
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    //OncePerRequestFilter의 doFilterInternal()를 Override
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("-- request 헤더의 jwt 토큰 검증 --");

        // request 헤더에서 refresh 토큰 가져오기
        Optional<String> refreshToken = jwtService.getToken(request, "refreshToken")
                .filter(jwtService::validateToken);

        // 유효한 refresh token이 없다면 -> access 토큰을 검사해서 인증 처리
        if(refreshToken.isEmpty()){
            log.info("[doFilterInternal] 유효한 refresh 토큰이 없어 access 토큰을 검사합니다.");
            authenticateAccessToken(request, response, filterChain);
            return;
        }

        // 유효한 refresh token이 있다면 -> access 토큰이 만료된 경우
        // refresh 토큰이 DB와 일치하는지 판단 후 토큰 재발급
        if(refreshToken.isPresent()) {
            log.info("[doFilterInternal] 유효한 refresh 토큰이니 access 토큰과 refresh 토큰을 재발급합니다.");
            reCreateTokens(response, refreshToken.get());
        }
    }

    /**
     * Access 토큰 확인해서 유저 인증 처리
     */
    public void authenticateAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }

    /**
     *  Refresh 토큰으로 유저 찾아서 -> refresh와 access 토큰 재발급
     */
    public void reCreateTokens(HttpServletResponse response, String refreshToken) {

    }


}

