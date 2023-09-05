package com.example.eight.artwork.controller;

import com.example.eight.artwork.dto.*;
import com.example.eight.artwork.service.ArtworkService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/app/artwork")
public class ArtworkController{
    private final ArtworkService artworkService;

    // 요소 인식
    @PostMapping("/detection")
    public ResponseEntity<DetectionResponseDto> performDetection(@RequestBody DetectionRequestDto requestDto) {
        DetectionResponseDto.DetectionData detectionData = artworkService.performDetection(requestDto);

        DetectionResponseDto responseDto = DetectionResponseDto.builder()
                .status("success")
                .message("Object detection successful")
                .data(detectionData)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    // 작품 부분 정보 조회 (퍼즐 페이지)
    @GetMapping("/partsinfo/{relic_id}")
    public ResponseEntity<PartsResponseDto> getArtworkParts(@PathVariable("relic_id") Long relicId) {
        PartsResponseDto responseDto = artworkService.getArtworkParts(relicId); // relicId를 전달
        return ResponseEntity.ok(responseDto);
    }

    // 태그 인식
    @PostMapping("/recognize-tag")
    public ResponseEntity<TagResponseDto> recognizeTag(@RequestBody TagRequestDto requestDto) {
        TagResponseDto tagResponseDto = artworkService.performTagRecognition(requestDto);

        if (tagResponseDto != null) {
            return ResponseEntity.ok(tagResponseDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
