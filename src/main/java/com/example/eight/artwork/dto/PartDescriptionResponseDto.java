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
public class PartDescriptionResponseDto {
    private Long relicId;
    private String relicImage;  // imgUri
    private String relicName;   // nameKr
    private String author;      // author
    private String nationality; //nationalityName2

    private int partNum;        // 총 부분 개수
    private List<PartDescriptionInfoDto> partDescriptionInfoList;  // 각 부분의 이름, 해설, 수집여부 리스트
}
