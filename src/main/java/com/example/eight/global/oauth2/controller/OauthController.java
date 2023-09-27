package com.example.eight.global.oauth2.controller;

import com.example.eight.user.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OauthController {



    @GetMapping("/app/login/google")
    public ResponseEntity<ResponseDto> googleLogin(@RequestParam String code){
    }
}
