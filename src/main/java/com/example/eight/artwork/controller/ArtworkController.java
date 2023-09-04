package com.example.eight.artwork.controller;

import com.example.eight.user.dto.ResponseDto;
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
    @GetMapping("/partsinfo")
    public ResponseEntity<PartsResponseDto> getArtworkParts(@RequestParam Long id) {
        PartsResponseDto responseDto = artworkService.getArtworkParts(id);
        return ResponseEntity.ok(responseDto);
    }

    // 부분에 속하는 요소 리스트 & 수집여부 조회
    @GetMapping("/elements")
    public ResponseEntity<ResponseDto> getArtworkElements(@RequestParam Long relicId, @RequestParam Long partId){
        // 각 요소 정보 및 수집여부 응답 생성
        ElementResponseDto elementResponseDto = artworkService.getElementDetail(relicId, partId);

        ResponseDto responseDto = ResponseDto.builder()
                .status("success")
                .message("Get detail for each element of part successful")
                .data(elementResponseDto)
                .build();

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
