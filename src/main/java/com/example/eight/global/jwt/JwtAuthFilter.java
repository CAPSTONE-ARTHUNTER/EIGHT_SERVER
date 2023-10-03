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
import java.util.List;
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
        // request 헤더에서 refresh 토큰 가져오기
        Optional<String> refreshToken = jwtService.getToken(request, "refreshToken")
                .filter(jwtService::validateToken);

        /*
         1. 다음 요청은 토큰을 담아 요청할 필요가 없으므로 필터 실행하지 않음
         */
        String requestURI = request.getRequestURI();    // 요청 URI
        if (List.of(    "/",
                        "/favicon.ico",
                        "/app/login/google",
                        "/app").contains(requestURI)) {
            log.info("\""+requestURI + "\" 이므로 토큰을 검증하지 않습니다. ");
        }

        /*
         2. 유효한 refresh token이 없다면 -> access 토큰을 검사해서 인증 처리
         */
        else if(refreshToken.isEmpty()){
            log.info("\""+requestURI + "\" 이므로 -- 토큰을 검증합니다 --");
            log.info("[doFilterInternal] 유효한 refresh 토큰이 없어 access 토큰을 검사합니다.");
            authenticateAccessToken(request, response, filterChain);
        }
        /*
         3. 유효한 refresh token이 있다면 -> refresh 토큰이 DB와 일치하는지 판단 후 토큰 재발급
         */
        else{
            log.info("\""+requestURI + "\" 이므로 -- 토큰을 검증합니다 --");
            log.info("[doFilterInternal] 유효한 refresh 토큰이니 access 토큰과 refresh 토큰을 재발급합니다.");
            reCreateTokens(response, refreshToken.get());
        }

        // 다음 필터 호출
        filterChain.doFilter(request, response);
    }

    /**
     * Access 토큰 확인해서 유저 인증 처리
     */
    public void authenticateAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request 헤더에서 access 토큰 가져오기
        Optional<String> accessToken = jwtService.getToken(request, "accessToken");

        // access 토큰이 유효하므로 토큰 검증 성공
        if (accessToken.isPresent() && jwtService.validateToken(accessToken.get())) {
            log.info("[authenticateAccessToken] access 토큰 유효함: {}",accessToken);

            // 토큰에서 claim 추출
            String userEmail = jwtService.getClaim(accessToken.get()).orElse(null);
            // claim으로 찾은 DB에서 유저의 Authentication 객체를 SecurityContextHodler에 저장
            if (userEmail != null) {
                Optional<User> userOptional = userRepository.findByEmail(userEmail);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    authenticateUser(user, accessToken.get());
        try {
                }
            }
        }
        // 토큰 검증 실패 -> 로그인 페이지로 리다이렉트
        else{
            log.info("유효한 access 토큰도 없으므로 로그인 페이지로 리다이렉트합니다.");
            response.sendRedirect("/oauth2/authorization/google");
        } catch (TokenExpiredException e) {
            //AccessToken 만료된 경우 예외처리
            handleTokenExpiredException(response, e);
        }
    }

    /**
     * 인증객체 생성
     */
    public void authenticateUser(User foundUser, String accessToken) {

        //UserDetails 객체
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(foundUser.getEmail())
                .password("randomPassword")     //소셜로그인은 비밀번호가 없으므로 임의의 비밀번호 지정
                .roles(foundUser.getRole().name())
                .build();

        // Authentication 객체 (new UsernamePasswordAuthenticationToken()로 생성)
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,                    // principal (유저 정보)
                accessToken,                           // credentails
                userDetails.getAuthorities());  //authorities
        log.info("[authenticateUser] Authentication 객체를 생성했습니다. (유저 이메일: {})",authentication.getName());

        // Authentication 객체에 대한 인증 허가
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // AccessToken 만료된 경우 401 응답하는 메소드
    private void handleTokenExpiredException(HttpServletResponse response, TokenExpiredException e) throws IOException {
        log.error("유효한 Access 토큰이 아님 - {}", e.getMessage());
        ResponseDto responseDto = ResponseDto.builder()
                .status("Unauthorized")
                .message("Access Token Expired")
                .build();
        String jsonResponse = new ObjectMapper().writeValueAsString(responseDto);
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }


}

