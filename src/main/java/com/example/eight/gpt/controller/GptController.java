package com.example.eight.gpt.controller;

import com.example.eight.global.ResponseDto;
import com.example.eight.artwork.dto.*;
import com.example.eight.artwork.service.ArtworkService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/app/artwork")
public class GptController {
    private final GptService GptService;
    @PostMapping("/elementsinfo")
    public GptResponseDto sendQuestion(@RequestBody QuestionRequestDto requestDto) {
        return chatGptService.askQuestion(requestDto);
    }
}