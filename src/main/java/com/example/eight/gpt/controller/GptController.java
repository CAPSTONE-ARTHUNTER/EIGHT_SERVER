package com.example.eight.gpt.controller;

import com.example.eight.gpt.service.*;
import lombok.AllArgsConstructor;
import com.example.eight.gpt.dto.GptResponseDto;
import com.example.eight.gpt.dto.GptRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/app/gpt")
public class GptController {
    private final ChatService chatService;

    @PostMapping("/elementsinfo")
    public ResponseEntity<GptResponseDto> getElementInfo(@RequestBody GptRequestDto requestDto) {
        GptResponseDto response = chatService.getElementInfo(requestDto.getName());
        return ResponseEntity.ok(response);
    }
}