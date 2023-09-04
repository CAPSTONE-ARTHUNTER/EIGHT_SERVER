package com.example.eight.artwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/*
특정 부분의 각 요소들 조회를 위한 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElementResponseDto {
    private String relicName;       // 작품명
    private String partName;        // 작품의 부분 중 조회하고자 하는 부분명
    private boolean part_isSolved;
    private List<ElementInfoDto> elementInfoList;   // 해당 부분의 각 요소들 정보 리스트
}
