package com.example.eight.artwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetectionRequestDto {
    private double time;
    private ImageInfo image;
    private List<Prediction> predictions;

    public static class ImageInfo {
        private int width;
        private int height;
    }

    public static class Prediction {
        private double x;
        private double y;
        private double width;
        private double height;
        private double confidence;
        private String className;
    }
}
