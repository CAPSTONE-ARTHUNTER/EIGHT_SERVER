package com.example.eight.collection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectionResponseDto {
    private int totalRelicNum;
    private List<CollectionDto> solvedRelicList;    // 작품의 id와 image 담은 Dto
}
