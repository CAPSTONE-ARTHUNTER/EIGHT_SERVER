package com.example.eight.user.config.auth.dto;

import com.example.eight.user.entity.User;
import lombok.Getter;

import java.io.Serializable;

// 인증된 사용자의 정보를 세션에 저장
@Getter
public class SessionUser implements Serializable {  // 직렬화 인터페이스
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user){      //User 엔티티 기반
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
