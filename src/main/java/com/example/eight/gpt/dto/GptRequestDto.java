package com.example.eight.gpt.dto;

public class GptRequestDto {
    private String name;

    public GptRequestDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GptRequestDto(String name) {
        this.name = name;
    }
}
