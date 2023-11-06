package com.example.eight.gpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GptResponseDto {
    private String ElementDescription;

    public GptResponseDto(String ElementDescription) {
        this.ElementDescription = ElementDescription;
    }

    public String getElementDescription() {
        return ElementDescription;
    }
}