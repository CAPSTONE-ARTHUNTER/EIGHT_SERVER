package com.example.eight.artwork.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponseDto {
    private Long id;
    private String name;

    public static class TagResponseDtoBuilder {

        public TagResponseDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
    }
}
