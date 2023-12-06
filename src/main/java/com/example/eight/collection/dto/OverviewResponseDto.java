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
public class OverviewResponseDto {
    private String userImage;
    private double userExp;
    private int solvedRelicNum;
    private List<OverviewDto> badgeList;    // 수집한 작품의 id와 뱃지 이미지 담은 List
}
