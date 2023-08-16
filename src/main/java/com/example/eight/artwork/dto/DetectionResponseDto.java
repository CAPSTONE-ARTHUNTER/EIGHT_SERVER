package com.example.eight.artwork.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetectionResponseDto {
    private String status;
    private String message;
    private DetectionData data;

    @Getter
    @Builder
    public static class DetectionData {
        private String image;
        private String name;
    }
}
