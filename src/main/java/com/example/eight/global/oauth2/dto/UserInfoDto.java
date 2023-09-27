package com.example.eight.global.oauth2.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoDto {
    private String email;
    private String name;
    private String picture;

    @Builder
    public UserInfoDto(String email, String name, String picture){
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
}
