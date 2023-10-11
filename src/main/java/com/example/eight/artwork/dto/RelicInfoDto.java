package com.example.eight.artwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RelicInfoDto {
    private Long art_id;
    private String art_name_kr;
    private String art_name_en;
    private String art_artist_name;
    private String art_era;
    private String art_image_url;
}
