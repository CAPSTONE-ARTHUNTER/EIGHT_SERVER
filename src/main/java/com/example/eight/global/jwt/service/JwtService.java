package com.example.eight.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.eight.global.ResponseDto;
import com.example.eight.global.jwt.dto.JwtTokenDto;
import com.example.eight.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
                .withExpiresAt(new Date(now.getTime() + accessTokenExpiration*1000)) // 만료 시간: 1시간
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
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpiration*1000)) // 만료 시간 : 2주
                .sign(Algorithm.HMAC512(secretKey));    // HMAC512 알고리즘 사용 ( refresh token은 claim 없음)
    }

    /*
     RefreshToken을 DB에 저장/업데이트
     */
    public void saveRefreshToken(String email, String refreshToken) {
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    log.info("리프레시 토큰으로 찾은 유저: {}", user.getEmail());
                    user.updateRefreshToken(refreshToken);
                    userRepository.save(user);
                });
        log.info("saveRefreshToken 메소드 종료");
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
                log.info("[오류] 인자로 'accessToken', 'refreshToken' 중 하나를 입력하세요");
                return Optional.empty();
            }
        }

        // 해당 토큰 추출해서 리턴
        return Optional.ofNullable(request.getHeader(header))
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.replace("Bearer ", ""));    // 헤더에서 "Bearer" 삭제해서 토큰만 가져오기
    }
    /*
     * Token 유효성 검사
     */
    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }

    /*
     * AccessToken에서 Claim 추출
     */
    public Optional<String> getClaim(String accessToken) {
        try {
            DecodedJWT decodedJWT = (JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken)); //유효하지 않은 토큰이면 예외
            // claim 리턴
            return Optional.ofNullable(decodedJWT.getClaim("email").asString());
        } catch (Exception e) {
            log.error("Claim 추출 실패");
            // 유효하지 않은 토큰이면 빈 객체 리턴
            return Optional.empty();
        }
    }

    /*
     *  Refresh 토큰으로 유저 찾아서 -> refresh와 access 토큰 재발급
     */
    public Map<String, String> reCreateTokens(String refreshToken) {
        Map<String, String> tokens = new HashMap<String, String>();

        userRepository.findByRefreshToken(refreshToken)
                // refresh token으로 찾은 유저가 있으면
                .ifPresent(user -> {
                    // 1. refresh 토큰 재발급
                    String newRefreshToken = createRefreshToken();
                    log.info("새로 발급한 refresh 토큰: {}", newRefreshToken);
                    user.updateRefreshToken(newRefreshToken);   // refreshToken은 DB에 저장
                    userRepository.save(user);

                    // 2. access token 재발급
                    String newAccessToken = createAccessToken(user.getEmail());    // access token 생성
                    log.info("새로 발급한 access 토큰: {}", newAccessToken);

                    // 3. 토큰 리턴
                    tokens.put("accessToken", newAccessToken);
                    tokens.put("refreshToken", newRefreshToken);
                });
        return tokens;
    }

    // RefreshToken 검증하여 토큰 재발급하는 메소드
    public ResponseEntity<ResponseDto> validateRefreshToken(HashMap<String, String> bodyJson, HttpServletResponse response) throws IOException {
        try {
            // 1. refresh 토큰 유효성 검사
            String refreshToken = bodyJson.get("refreshToken"); // Request 바디로 받은 refresh 토큰 가져오기
            validateToken(refreshToken); // 토큰 유효한지 확인

            // 2. DB에 해당 refreshToken 있는지 확인
            Boolean isExists = userRepository.existsByRefreshToken(refreshToken);
            if (!isExists) { // DB에 없다면 401 응답
                return createResponseEntity(HttpStatus.UNAUTHORIZED, "Refresh Token is not found in Database", null);
            }

            // 유효하면 accessToken과 refreshToken 재발급해서
            Map<String, String> tokens = reCreateTokens(refreshToken);

            // 토큰 담아 200 응답
            String newRefreshToken = tokens.get("refreshToken");
            String newAccessToken = tokens.get("accessToken");
            JwtTokenDto jwtTokenDto = new JwtTokenDto("Bearer " + newAccessToken, "Bearer " + newRefreshToken);
            return createResponseEntity(HttpStatus.OK, "Reissue Tokens successful", jwtTokenDto);
        }
        // RefresToken 만료된 경우 401 응답
        catch (TokenExpiredException e) {
            return createResponseEntity(HttpStatus.UNAUTHORIZED, "Expired Refresh Token", null);
        }
        // 그 외 401 응답
        catch (Exception e) {
            log.error(e.getMessage());
            return createResponseEntity(HttpStatus.UNAUTHORIZED, "Refresh Token is Invalid", null);
        }
    }

    // 에러 응답 생성하여 리턴하는 메소드
    private ResponseEntity<ResponseDto> createResponseEntity(HttpStatus status, String message, JwtTokenDto data) {
        ResponseDto responseDto = ResponseDto.builder()
                .status(status.toString())
                .message(message)
                .data(data)
                .build();
        return ResponseEntity.status(status).body(responseDto);
    }


    // AccessToken에서 expiresTime 추출하는 메소드
    public Optional<Long> getExpiresTime(String accessToken) {
        try {
            DecodedJWT decodedJWT = (JWT.require(Algorithm.HMAC512(secretKey)).build().verify(accessToken));
            // 만료시간 리턴 (현재시간 차감 후 남은 만료시간)
            Long expiresTime = decodedJWT.getExpiresAt().getTime();
            Long now = new Date().getTime();    // 현재 시간
            return Optional.ofNullable(expiresTime - now);  // 기본 만료시간은 1시간 (3600초)
        } catch (Exception e) {
            log.error("expiresTime 추출 오류: {}", e);
            return Optional.empty();
        }
    }
}