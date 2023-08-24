package com.example.eight.user.entity;


import com.example.eight.user.Role;
import com.example.eight.user.SocialType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;


@Slf4j
@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    //@Column(nullable = false)
    private String name;

    //@Column(nullable = false, length = 30)
    @Column(length = 30)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String email;

    @Column
    private String picture;

    //@Column(nullable = false, columnDefinition = "DECIMAL(10.1) default 0.0 ")
    @Column(columnDefinition = "DECIMAL(10.1) default 0.0 ")
    private double exp;

    //@Column(name = "created_at", nullable = false)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    //@Column(name = "updated_at", nullable = false)
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    // 생성자
    @Builder
    public User(String name, String nickname, String email, double exp, String picture,
                LocalDateTime createdAt, LocalDateTime updatedAt,
                Role role, SocialType socialType){
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.exp = exp;
        this.picture = picture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
        this.socialType = socialType;
    }
    // 유저 정보 업데이트하기
    public User update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    // 유저의 ROLE key 리턴하기
    public String getRoleKey(){
        return this.role.getKey();
    }
}

