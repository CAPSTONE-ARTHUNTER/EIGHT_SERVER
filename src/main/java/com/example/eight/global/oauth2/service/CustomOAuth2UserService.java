package com.example.eight.global.oauth2.service;

import com.example.eight.global.oauth2.dto.OAuthAttributes;
import com.example.eight.user.Role;
import com.example.eight.user.SocialType;
import com.example.eight.user.entity.User;
import com.example.eight.user.repository.UserRepository;
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
import java.util.Optional;

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
        log.info("registration Id: {}", registrationId);                // google
        log.info("userNameAttributeName: {}", userNameAttributeName);   // sub
        log.info("socialType: {}", socialType);                         // GOOGLE

        // oAuth2User의 정보 attributes에 저장 (dto 클래스인 OAuthAttributes)
        OAuthAttributes attributes = OAuthAttributes.of(socialType, userNameAttributeName,
                oAuth2User.getAttributes());

        // 해당 User가 이미 DB에 있다면 Update, 업다면 새로 Create
        User user = createOrUpdateUser(attributes, socialType);


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
    User 생성 (최초 가입시)
    */
    public User createUser(OAuthAttributes attributes, SocialType socialType){

        return User.builder()
                .name(attributes.getName())
                .email(attributes.getEmail())
                .picture(attributes.getPicture())
                .role(Role.USER)   // TODO: 일단 USER로 설정
                .build();
    }

    /*
    User 엔티티에
    유저의 attributes 저장/업데이트 하기
     */
    private User createOrUpdateUser(OAuthAttributes attributes, SocialType socialType) {
        String email = attributes.getEmail();
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        // 해당 유저가 이미 로그인한 적이 있다면 정보만 업데이트
        if (userOptional.isPresent()) {
            user = userOptional.get();
            log.info("기존에 DB에 저장된 유저입니다. 이메일: {} ", email);
            user.update(attributes.getName(), attributes.getPicture());
        }
        // 최초 로그인이라면 User 생성
        else {
            log.info("DB에 유저를 새로 저장합니다. 이메일: {}", email);
            user = createUser(attributes, socialType);
        }

        return userRepository.save(user);
    }
}