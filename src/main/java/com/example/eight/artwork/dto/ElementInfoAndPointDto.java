package com.example.eight.artwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElementInfoAndPointDto {
    private Long id;
    private String point;
    private boolean isSolved;
}
