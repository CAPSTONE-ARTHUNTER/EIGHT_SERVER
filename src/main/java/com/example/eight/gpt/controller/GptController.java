package com.example.eight.gpt.controller;

import com.example.eight.gpt.service.*;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.AllArgsConstructor;
import com.example.eight.gpt.dto.GptResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/app/gpt")
public class GptController {
    private final ChatService chatService;
    private final ChatgptService chatgptService;

    @PostMapping("/elementsinfo")
    public ResponseEntity<GptResponseDto> test(@RequestBody String question) {
        String answer = chatService.getChatResponse(question);

        GptResponseDto response = new GptResponseDto(answer);
        return ResponseEntity.ok(response);
    }
}
