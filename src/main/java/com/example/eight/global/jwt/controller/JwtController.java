package com.example.eight.global.jwt.controller;

import com.example.eight.global.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
public class JwtController {


    // Request 바디의 refresh token 검증 -> 유효한 경우 access token 재발급
    @PostMapping("/app/auth/refresh")
    public ResponseEntity<ResponseDto> validateRefreshToken(@RequestBody HashMap<String, String> bodyJson, HttpServletResponse response) throws IOException {
    }
}
