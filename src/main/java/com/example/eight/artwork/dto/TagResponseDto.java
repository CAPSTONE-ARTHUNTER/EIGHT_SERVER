package com.example.eight.artwork.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponseDto {
    private Long id;
    private String name;
    private String relicImage;
    private String error;

    public static class TagResponseDtoBuilder {

        public TagResponseDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
