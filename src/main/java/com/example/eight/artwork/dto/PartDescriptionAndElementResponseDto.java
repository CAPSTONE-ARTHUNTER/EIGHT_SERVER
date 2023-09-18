package com.example.eight.artwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
특정 부분의 해설과 요소들의 정보 조회하는 API
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartDescriptionAndElementResponseDto {
    // 작품 정보
    private Long relicId;
    private String relicImage;  // 공공 API - imgUri
    private String relicName;   // 공공 API - nameKr
    //부분 정보 및 해설
    private String partName;
    private String partDescription;
    private boolean part_isSolved;
    // 요소 정보
    private int elementNum;
    private int elementSolvedNum;
    private List<ElementInfoAndPointDto> elementList;  // 요소들의 id, point, 수집여부 리스트
}
