package com.example.eight.global.oauth2.service;

import com.example.eight.global.oauth2.dto.OAuthAttributes;
import com.example.eight.user.SocialType;
import com.example.eight.user.entity.User;
import com.example.eight.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private final UserRepository userRepository;

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
        SocialType socialType
                = getSocialType(registrationId);

        // oAuth2User의 정보 attributes에 저장 (dto 클래스인 OAuthAttributes)
        OAuthAttributes attributes = OAuthAttributes.of(socialType, userNameAttributeName,
                oAuth2User.getAttributes());

        // User 엔티티에 저장
        User user = saveOrUpdate(attributes, socialType);       // 아래에서 정의한 saveOrUpdate 메소드


        // OAuth2User 리턴
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(), attributes.getNameAtrributeKey());
    }

    // Social Type 리턴
    private SocialType getSocialType(String registrationId) {
        // 다른 소셜로그인 추가시 로직 추가
        if(registrationId.equals("google")) {
            log.info("구글 소셜로그인 진행");
            return SocialType.GOOGLE;
        }
        else {
            log.info("그 외 로그인 진행");
            return null;
        }
    }
    /*
    User 엔티티에
    유저의 attributes 저장/업데이트 하기
     */
    private User saveOrUpdate(OAuthAttributes attributes, SocialType socialType){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(),
                        attributes.getPicture()))
                .orElse(attributes.toEntity(socialType));

        return userRepository.save(user);
    }
}