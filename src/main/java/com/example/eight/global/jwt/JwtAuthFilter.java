package com.example.eight.global.jwt;

import com.example.eight.user.entity.User;
import com.example.eight.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserRepository userRepository;

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
        // request 헤더에서 access 토큰 가져오기
        Optional<String> accessToken = jwtService.getToken(request, "accessToken");

        // access 토큰이 유효하다면
        if (accessToken.isPresent() && jwtService.validateToken(accessToken.get())) {
            log.info("[authenticateAccessToken] access 토큰 유효함: {}",accessToken);

            // 토큰에서 claim 추출
            String userEmail = jwtService.getClaim(accessToken.get()).orElse(null);
            // claim으로 찾은 DB에서 유저의 Authentication 객체를 SecurityContextHodler에 저장
            if (userEmail != null) {
                userRepository.findByEmail(userEmail)
                        .ifPresent(this::authenticateUser); // authenticateUser 메소드 실행
            }
        }
        filterChain.doFilter(request, response);    // 인증 허가 처리된 객체를 SecurityContextHolder에 담기
    }

    /**
     * 인증객체 생성
     */
    public void authenticateUser(User foundUser) {

        //UserDetails 객체
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(foundUser.getEmail())
                .password("randomPassword")     //소셜로그인은 비밀번호가 없으므로 임의의 비밀번호 지정
                .roles(foundUser.getRole().name())
                .build();

        // Authentication 객체 (new UsernamePasswordAuthenticationToken()로 생성)
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,                    // principal (유저 정보)
                null,                           // credentails
                userDetails.getAuthorities());  //authorities
        log.info("[authenticateUser] Authentication 객체를 생성했습니다. (유저 이메일: {})",authentication.getName());

        // Authentication 객체에 대한 인증 허가
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     *  Refresh 토큰으로 유저 찾아서 -> refresh와 access 토큰 재발급
     */
    public void reCreateTokens(HttpServletResponse response, String refreshToken) {

    }


}

