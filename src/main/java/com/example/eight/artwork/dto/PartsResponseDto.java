package com.example.eight.artwork.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartsResponseDto {
    private String relicName;
    private String relicImage;
    private String relicBadgeImage;
    private List<PartInfoDto> partInfos;
    private int totalPartCount;
    private int totalSolvedPartCount;

}
