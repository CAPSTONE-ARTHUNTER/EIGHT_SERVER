package com.example.eight.artwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetectionRequestDto {
    private String name;

    public DetectionResponseDto.DetectionData getData() {
        return DetectionResponseDto.DetectionData.createFromRequest(this);
    }
}