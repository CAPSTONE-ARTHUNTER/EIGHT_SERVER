package com.example.eight.global.oauth2.handler;

import com.example.eight.user.entity.User;
import com.example.eight.user.repository.UserRepository;
import com.example.eight.global.jwt.JwtService;
import com.example.eight.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("소셜 로그인 완료! OAuth2LoginSuccessHandler 실행");
        try {
            // 로그인한 유저의 정보 가져오기
            DefaultOAuth2User OAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = OAuth2User.getAttributes();
            log.info("소셜 로그인 한 OAuth2User: {}", OAuth2User);

            // 해당 유저의 토큰 생성 TODO: 일단 GUEST로 하고, 추가 유저 정보 입력하면(nickname) USER로 바꾸기
            log.info("USER 이므로 로그인 성공! 토큰 생성");
            CreateToken(response, attributes); // 토큰 생성

        } catch (Exception e) {
            throw e;
        }
    }


    private void CreateToken(HttpServletResponse response, Map<String, Object> attributes) throws IOException {
        String userEmail = (String) attributes.get("email");    // 현재 로그인한 유저의 이메일
        log.info("로그인한 이메일: "+ userEmail);

        // access와 refresh 토큰 생성
        String accessToken = jwtService.createAccessToken(userEmail);   // uesrEmail로 access token 생성
        String refreshToken = jwtService.createRefreshToken();
        log.info("access 토큰 : "+accessToken);
        log.info("refresh 토큰 : "+refreshToken);

        // acess와 refresh 토큰에 Bearer 붙여서 response 헤더로 전송
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);
        jwtService.sendTokens(response, accessToken, refreshToken);

        // refresh 토큰을 DB에 저장 TODO: 아직 유효한 refresh 토큰이 아직 저장되어 있는 경우 처리
        jwtService.saveRefreshToken(userEmail, refreshToken);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없음"));
        userService.updateRefreshToken(user.getId(), refreshToken);
    }
}
