package com.example.eight.gpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GptResponseDto {
    private Long relicId;
    private String relicName;
    private String ElementImage;
    private String ElementName;
    private String ElementDescription;

    public GptResponseDto(Long relicId, String relicName, String ElementImage, String ElementName, String ElementDescription) {
        this.relicId = relicId;
        this.relicName = relicName;
        this.ElementImage = ElementImage;
        this.ElementName = ElementName;
        this.ElementDescription = ElementDescription;
    }

    public Long getRelicId() {
        return relicId;
    }

    public String getRelicName() {
        return relicName;
    }

    public String getElementImage() {
        return ElementImage;
    }

    public String getElementName() {
        return ElementName;
    }

    public String getElementDescription() {
        return ElementDescription;
    }
}
