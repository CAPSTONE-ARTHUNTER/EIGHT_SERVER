package com.example.eight.global.oauth2.controller;

import com.example.eight.global.jwt.JwtService;
import com.example.eight.global.oauth2.service.OauthService;
import com.example.eight.user.SocialType;
import com.example.eight.global.jwt.dto.JwtTokenDto;
import com.example.eight.global.ResponseDto;
import com.example.eight.global.oauth2.dto.UserInfoDto;
import com.example.eight.user.service.UserService;
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

    private final OauthService oauthService;
    private final JwtService jwtService;
    private final UserService userService;


    @GetMapping("/app/login/google")
    public ResponseEntity<ResponseDto> googleLogin(@RequestParam String code){
        // 1. 프론트엔드로부터 받은 인가코드로 구글 서버에서 accessToken 받아오기
        String accessTokenByGoogle = oauthService.getGoogleAccessToken(code);

        // 2. 구글서버에서 받은 accessToken으로 유저 info 받아오기
        String userInfo = oauthService.getUserInfo(accessTokenByGoogle);

        // 3. DB에 유저 정보 저장/업데이트
        UserInfoDto userInfoDto = userService.parseUserInfo(userInfo);  // 받은 유저 info 파싱해서 필요한 정보 저장
        String userEmail = userInfoDto.getEmail();
        String userName = userInfoDto.getName();
        String userPicture = userInfoDto.getPicture();
        userService.createOrUpdateUser(userEmail, userName, userPicture, SocialType.GOOGLE);

        // 4. 우리 서비스의 access 토큰과 refresh 토큰 생성
        String accessToken = jwtService.createAccessToken(userEmail);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.saveRefreshToken(userEmail, refreshToken);  // refresh 토큰을 DB에 저장

        // 5. 응답 객체 생성
        JwtTokenDto jwtTokenDto = new JwtTokenDto("Bearer " +accessToken, "Bearer " +refreshToken);
        ResponseDto responseDto = ResponseDto.builder()
                .status("success")
                .message("Google login successful")
                .data(jwtTokenDto)
                .build();

        // 응답
        return ResponseEntity.ok(responseDto);
    }
}
