package com.example.eight.user.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String email;

    @Column
    private String picture;

    @Column(nullable = false, columnDefinition = "DECIMAL(10.1) default 0.0 ")
    private double exp;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Builder
    public User(String name, String nickname, String email, double exp, String picture,
                LocalDateTime createdAt, LocalDateTime updatedAt,
                Role role){
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.exp = exp;
        this.picture = picture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
    }

    // update 추가
}

