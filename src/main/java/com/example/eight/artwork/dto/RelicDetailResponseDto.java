package com.example.eight.artwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelicDetailResponseDto {
    private Long RelicId;
    private String RelicNameKr;
    private String RelicNameEn;
    private String RelicArtistName;
    private String RelicEra;
    private String RelicSize;
    private String RelicIndex;
    private String RelicDescription;
    private String RelicImageUrl;

}
