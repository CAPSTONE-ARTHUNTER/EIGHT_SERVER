package com.example.eight.gpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GptResponseDto {
    private String answer;

    public GptResponseDto(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}