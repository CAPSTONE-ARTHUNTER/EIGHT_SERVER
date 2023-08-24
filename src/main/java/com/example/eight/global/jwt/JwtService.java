package com.example.eight.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@Getter
public class JwtService {
    /*
     application.properties의 프로퍼티 주입
      */
    @Value("${jwt.secretKey}")
    private String secretKey;   //application.properties에서 지정한 secret key

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpiration;


    /*
     AccessToken 생성
     */
    public String createAccessToken(String email) {
        Date now = new Date();
        log.info("now: "+String.valueOf(now));

        return JWT.create()
                .withSubject("AccessToken") // Subject를 AccessToken으로 지정
                .withExpiresAt(new Date(now.getTime() + accessTokenExpiration)) // 만료 시간
                .withClaim("email", email)         // claim으로 email 하나 사용
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용
    }

    /*
     RefreshToken 생성
     */
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject("RefreshToken")     // Subject를 RefreshToken으로 지정
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpiration)) // 만료 시간
                .sign(Algorithm.HMAC512(secretKey));    // HMAC512 알고리즘 사용 ( refresh token은 claim 없음)
    }

}
