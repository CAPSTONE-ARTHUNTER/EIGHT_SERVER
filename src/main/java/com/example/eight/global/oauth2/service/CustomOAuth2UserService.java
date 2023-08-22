package com.example.eight.global.oauth2.service;

import com.example.eight.global.oauth2.dto.OAuthAttributes;
import com.example.eight.user.entity.User;
import com.example.eight.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private final UserRepository userRepository;
    private final HttpSession httpSession;  // HTTP 세션

    /*
    구글 OAuth 유저 정보 가져오기
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        // 유저 정보 가져와서 oAuth2User에 저장
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        String registrationId           // OAuth 로그인을 제공하는 서비스를 구분 (ex. google)
                = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName   // OAuth 로그인 시 키가 되는 값 (ex. 구글의 기본 코드는 'sub')
                = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // oAuth2User의 정보 attributes에 저장 (dto 클래스인 OAuthAttributes)
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName,
                oAuth2User.getAttributes());

        // User 엔티티에 저장
        User user = saveOrUpdate(attributes);       // 아래에서 정의한 saveOrUpdate 메소드
        // 세션에 유저정보 저장
        httpSession.setAttribute("user", new SessionUser(user));    // SessionUser 클래스 사용


        // OAuth2User 리턴
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(), attributes.getNameAtrributeKey());
    }

    /*
    User 엔티티에
    유저의 attributes 저장/업데이트 하기
     */
    private User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(),
                        attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
