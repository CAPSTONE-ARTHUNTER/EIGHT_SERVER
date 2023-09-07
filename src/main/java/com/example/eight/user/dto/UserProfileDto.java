package com.example.eight.user.dto;

import com.example.eight.user.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileDto {
    private Long id;
    private String name;
    private String email;
    private String picture;
    private double exp;
    private SocialType socialType;


    @Builder
    public UserProfileDto(Long id, String name, String email, double exp, String picture, SocialType socialType){
        this.id = id;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.exp = exp;
        this.socialType = socialType;
    }

}
