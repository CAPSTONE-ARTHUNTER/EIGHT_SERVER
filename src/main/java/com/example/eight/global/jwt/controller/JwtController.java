package com.example.eight.global.jwt.controller;

import com.example.eight.global.ResponseDto;
import com.example.eight.global.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@RestController
public class JwtController {

    private final JwtService jwtService;

    // Request 바디의 refresh token 검증 -> 유효한 경우 access token 재발급
    @Transactional
    @PostMapping("/app/auth/refresh")
    public ResponseEntity<ResponseDto> validateRefreshToken(@RequestBody HashMap<String, String> bodyJson, HttpServletResponse response) throws IOException {

        return jwtService.validateRefreshToken(bodyJson, response);
    }

    @Transactional
    @PostMapping("/app/auth/logout")
    public ResponseEntity<ResponseDto> logout(@RequestHeader("Authorization") String accessToken){
        if(accessToken != null && accessToken.startsWith("Bearer ")){
            accessToken = accessToken.substring(7); // 헤더에서 Bearer 삭제
        }

        return jwtService.logout(accessToken);
    }
}
