package com.example.eight.artwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ElementInfoDto {
    private Long id;            //요소 id
    private String name;        //요소 이름
    private String image;       //요소 이미지
    private boolean isSolved;   //요소 수집여부
}
