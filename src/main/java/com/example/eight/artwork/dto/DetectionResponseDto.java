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

        public static DetectionData createFromRequest(DetectionRequestDto requestDto, String imageUrl) {
            return DetectionData.builder()
                    .image(imageUrl)  // 요소 이미지 URL
                    .name(requestDto.getName())  // 요소 이름(class)
                    .build();
        }
    }
}
