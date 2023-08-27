package com.example.eight.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.eight.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
public class JwtServiceTest {

    @Mock
    private UserRepository userRepository;

    @MockBean   //주의
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtService = new JwtService(userRepository);

        // 프로퍼티 TODO: 테스트용 properties 파일 적용해보기
        jwtService.setSecretKey("sampleSecretKeyForTest");
        jwtService.setAccessTokenExpiration(3600000L);      // 1시간
        jwtService.setRefreshTokenExpiration(604800000L);   // 1주
        jwtService.setAccessHeader("Authorization");
        jwtService.setRefreshHeader("Authorization-refresh");

    }

    @DisplayName("access token 발행")
    @Test
    public void testCreateAccessToken() {
        //given
        String testEmail = "test@example.com";

        //when
        String accessToken = jwtService.createAccessToken(testEmail);   // AccessToken 생성

        //then
        // 1. AccessToken이 비어있지 않은지 검증
        assertThat(accessToken).isNotBlank();
        log.info("발급된 accessToken: "+ accessToken);
        // 2. email이 같은지 검증
        String decodedEmail = JWT.decode(accessToken).getClaim("email").toString();
        assertThat( (decodedEmail)).isEqualTo("\""+testEmail+"\"");
        log.info("발급된 유저 email: "+ decodedEmail);
    }


    @DisplayName("refresh token 발행")
    @Test
    public void TestCreateRefreshToken() {
        //given

        //when
        String refreshToken = jwtService.createRefreshToken();

        //then
        // 1. refreshToken이 비어있지 않은지 검증
        assertThat(refreshToken).isNotBlank();
        log.info("발급된 refreshToken: "+ refreshToken);
    }

    @DisplayName("request 헤더에서 토큰 추출")
    @Test
    public void testGetToken(){
        //given
        String 샘플토큰 = "테스트용Token";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer "+샘플토큰);
        when(request.getHeader("Authorization-refresh")).thenReturn("Bearer "+샘플토큰);

        //when
        Optional<String> accessToken = jwtService.getToken(request, "accessToken");
        Optional<String> refreshToken = jwtService.getToken(request, "refreshToken");
        Optional<String> invalidToken = jwtService.getToken(request, "유효하지 않은 파라미터");

        //then
        assertThat(accessToken.get()).isEqualTo(샘플토큰);
        assertThat(refreshToken.get()).isEqualTo(샘플토큰);
        assertThat(invalidToken).isEmpty();
    }

    @DisplayName("유효성 검사 - 유효한 토큰일 때")
    @Test
    public void testValidateToken_Valid() {
        //given
        String token = JWT.create().sign(Algorithm.HMAC512("sampleSecretKeyForTest"));

        // when
        boolean isValid = jwtService.validateToken(token);

        //then
        assertThat(isValid).isTrue();
        log.info(token);
    }

    @DisplayName("유효성 검사 - 유효하지 않은 토큰일 때")
    @Test
    public void testValidateToken_InValid() {
        //given
        // 1. secret key가 맞지 않을 때
        String token = JWT.create().sign(Algorithm.HMAC512("invalid SecretKey"));
        // 2. 만료되었을 때
        Date 만료된_시간 = new Date(System.currentTimeMillis() - 100); // 이미 만료된 시간
        String 만료된_토큰 = JWT.create().withExpiresAt(만료된_시간)
                .sign(Algorithm.HMAC512("sampleSecretKeyForTest"));

        // when
        boolean isValidKey = jwtService.validateToken(token);
        boolean isValidTime = jwtService.validateToken(만료된_토큰);

        //then
        assertThat(isValidKey).isFalse();
        assertThat(isValidTime).isFalse();
    }
    @DisplayName("Claim 추출")
    @Test
    void testGetClaim() {
        //given
        String testClaim = "test@example.com";
        Date 유효한시간 = new Date(System.currentTimeMillis() + 1000);
        String accessToken = JWT.create()
                .withClaim("email", testClaim)
                .withExpiresAt(유효한시간).sign(Algorithm.HMAC512("sampleSecretKeyForTest"));

        //when
        Optional<String> claim = jwtService.getClaim(accessToken);

        //then
        assertThat(claim).isPresent().hasValue(testClaim);
        log.info(claim.get());
    }
}

