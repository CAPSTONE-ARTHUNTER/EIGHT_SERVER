package com.example.eight.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.eight.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Setter         //테스트코드에서 사용하기 위해 추가
@Service
@RequiredArgsConstructor
@Getter
@Slf4j
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

    private final UserRepository userRepository;

    @Value("${jwt.access.header}")
    private String accessHeader;    // Authorization

    @Value("${jwt.refresh.header}")
    private String refreshHeader;   // Authorization-refresh


    /*
     AccessToken 생성
     */
    public String createAccessToken(String email) {
        Date now = new Date();

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

    /*
     RefreshToken을 DB에 저장/업데이트
     */
    public void saveRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresentOrElse(
                        user -> user.updateRefreshToken(refreshToken),
                        () -> new Exception("해당 유저가 없음")
                );
    }

    /*
    AccessToken과 RefreshToken을 헤더로 보내기
    */
    public void sendTokens(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);      // Acess Token을 헤더에 싣기
        response.setHeader(refreshHeader, refreshToken);    // Refresh Token을 헤더에 싣기
        log.info("Access Token 및 Refresh Token 헤더로 보내기 성공");
    }

    /*
    request 헤더에서 Token 추출
     */
    public Optional<String> getToken(HttpServletRequest request, String tokenType) {
        String header;
        switch (tokenType) {    // accessToken인지 refreshToken인지 구분
            case "accessToken" -> header = accessHeader;
            case "refreshToken" -> header = refreshHeader;
            default -> {
                log.info("유효하지 않습니다.'accessToken' 혹은 'refreshToken'을 입력하세요");
                return Optional.empty();
            }
        }

        // 해당 토큰 추출해서 리턴
        return Optional.ofNullable(request.getHeader(header))
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.replace("Bearer ", ""));    // 헤더에서 "Bearer" 삭제해서 토큰만 가져오기
    }
}