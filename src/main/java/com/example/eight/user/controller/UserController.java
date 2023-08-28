package com.example.eight.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    @GetMapping("/token-test")
    public String tokenTest() {
        return "토큰 검증 테스트를 완료했습니다.";
    }
}
