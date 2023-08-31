package com.example.eight.user.dto;

import com.example.eight.user.Role;
import com.example.eight.user.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserProfileDto {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private double exp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Role role;
    private SocialType socialType;


    @Builder
    public UserProfileDto(Long id, String name, String email, double exp, String picture,
                          LocalDateTime createdAt, LocalDateTime updatedAt,
                          Role role, SocialType socialType){
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.exp = exp;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
        this.socialType = socialType;
    }

}
