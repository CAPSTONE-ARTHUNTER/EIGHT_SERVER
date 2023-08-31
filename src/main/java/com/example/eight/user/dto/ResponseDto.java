package com.example.eight.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDto<T> {
    private String status;
    private String message;
    private T data;

    @Builder
    public ResponseDto(String status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
