package com.example.eight.artwork.controller;

import com.example.eight.artwork.dto.DetectionRequestDto;
import com.example.eight.artwork.dto.DetectionResponseDto;
import com.example.eight.artwork.service.ArtworkService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/app/artwork")
public class ArtworkController{
    private final ArtworkService artworkService;

    //요소 인식
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

}
