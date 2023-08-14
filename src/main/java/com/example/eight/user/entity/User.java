package com.example.eight.user.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, columnDefinition = "DECIMAL(10.1) default 0.0 ")
    private double exp;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    @Builder
    public User(String nickname, String email, double exp,
                LocalDateTime createdAt, LocalDateTime updatedAt){
        this.nickname = nickname;
        this.email = email;
        this.exp = exp;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // update 추가
}

