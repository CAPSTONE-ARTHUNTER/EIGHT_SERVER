package com.example.eight.artwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetectionRequestDto {
    private double time;
    private ImageInfo image;
    private List<Prediction> predictions;

    public DetectionResponseDto.DetectionData getData() {
        return DetectionResponseDto.DetectionData.createFromRequest(this);
    }

    @Getter
    @Builder
    public static class ImageInfo {
        private int width;
        private int height;
    }

    @Getter
    @Builder
    public static class Prediction {
        private double x;
        private double y;
        private double width;
        private double height;
        private double confidence;
        private String elementClassName;
    }
}