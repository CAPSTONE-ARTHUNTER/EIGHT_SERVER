package com.example.eight.user.config.auth.dto;

import com.example.eight.user.entity.Role;
import com.example.eight.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes; //attributes
    private String nameAtrributeKey;        // OAuth 로그인에서의 Key
    private String name;
    private String email;
    private String picture;                 // 유저 프로필 사진 URl

    /*
    생성자
     */
    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String name, String email, String picture){
        this.attributes = attributes;
        this.nameAtrributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    /*
    of() : OAuth2 provider 별로 유저 정보 가져오기
     */
    public static OAuthAttributes of(String registraionId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes){
        // 구글 OAuth의 유저 정보 리턴
        return ofGoogle(userNameAttributeName, attributes);
    }

    /*
    ofGoogle() : 구글 OAuth의 유저 정보 가져오기
     */
    public static OAuthAttributes ofGoogle(String userNameAttributeName,
                                     Map<String, Object> attributes){
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture( (String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    /*
    User 엔티티 생성 (최초 가입시)
     */
    public User toEntity(){

        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)   // 최초 가입시 role은 GUEST
                .build();
    }
}
