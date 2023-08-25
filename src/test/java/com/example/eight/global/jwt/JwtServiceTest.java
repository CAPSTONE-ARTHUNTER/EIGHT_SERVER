package com.example.eight.global.jwt;

import com.auth0.jwt.JWT;
import com.example.eight.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.assertj.core.api.Assertions.*;

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
}

