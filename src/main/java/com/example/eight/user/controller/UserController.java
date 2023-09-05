package com.example.eight.user.controller;

import com.example.eight.user.dto.ResponseDto;
import com.example.eight.user.dto.UserProfileDto;
import com.example.eight.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    private final UserService userService;


    @GetMapping("/")
    public String getRootPage(){
        return "[루트 페이지] 구글 로그인 화면으로 리다이렉트되지 않습니다.";
    }

    @GetMapping("/app")
    public String getMainPage(){
        return "[메인 페이지] 구글 로그인 화면으로 리다이렉트되지 않습니다.";
    }

    @GetMapping("/app/users/test")
    public String getUserPage(){
        return "/users/** 페이지는 인증이 필요합니다.";
    }

    @GetMapping("/oauth2/authorization/google")
    public void googleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {     // @RestController여서 String 타입 리턴시 리다이렉트 불가
        log.info("인증이 필요하여 \"/auth/oauth2/google\"로 이동했습니다.");

        // 구글 로그인 URI
        String redirectUri = "http://localhost:8080/login/oauth2/code/google";
        String scope = "email profile openid";
        String encodedScope = URLEncoder.encode(scope, StandardCharsets.UTF_8);

        String GoogleLoginURI = "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=" + googleClientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=" + encodedScope +
                "&access_type=offline";

        // 구글 로그인 페이지로 리다이렉트
        response.sendRedirect(GoogleLoginURI);
    }

    // 유저 프로필 조회
    @GetMapping("/app/users/profile")
    public ResponseEntity<ResponseDto> getUserInfo() {
        // 프로필 응답 생성
        UserProfileDto userProfileDto = userService.getUserProfile();

        ResponseDto responseDto = ResponseDto.builder()
                .status("success")
                .message("Get user profile successful")
                .data(userProfileDto)
                .build();

        return ResponseEntity.ok(responseDto);
    }
}
