package com.example.eight.collection.controller;

import com.example.eight.collection.dto.ChallengeResponseDto;
import com.example.eight.collection.dto.CollectionResponseDto;
import com.example.eight.collection.dto.OverviewResponseDto;
import com.example.eight.collection.service.CollectionService;
import com.example.eight.global.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/app/collection")
public class CollectionController {

    
    private final CollectionService collectionService;

    // 수집 오버뷰 조회 API
    @GetMapping("/overview")
    public ResponseEntity<ResponseDto> getCollectionOverview(){
        OverviewResponseDto overviewResponseDto = collectionService.getOverview();

        ResponseDto responseDto = ResponseDto.builder()
                .status("success")
                .message("Get Overview successful")
                .data(overviewResponseDto)
                .build();

        return ResponseEntity.ok(responseDto);

    }

    // 도감 조회 API
    @GetMapping("")
    public ResponseEntity<ResponseDto> getCollection(){
        CollectionResponseDto collectionResponseDto = collectionService.getCollection();

        ResponseDto responseDto = ResponseDto.builder()
                .status("success")
                .message("Get Collection successful")
                .data(collectionResponseDto)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    // 도감 도전과제 조회 API
    @GetMapping("/challenge")
    public ResponseEntity<ChallengeResponseDto> getChallenge(){
        ChallengeResponseDto challengeResponseDto = collectionService.getChallengeList();

        return ResponseEntity.ok(challengeResponseDto);
    }

}