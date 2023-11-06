package com.example.eight.collection.dto;

import com.example.eight.artwork.dto.PartInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeDto {
    private Long relicId;
    private String relicName;
    private String badgeImage;
    private int partNum;
    private int solvedPartNum;
    private List<PartInfoDto> partList;
}
