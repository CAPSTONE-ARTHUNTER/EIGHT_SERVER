package com.example.eight.gpt.controller;

import com.example.eight.gpt.service.*;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.AllArgsConstructor;
import com.example.eight.gpt.dto.GptResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/app/gpt")
public class GptController {
    private final ChatService chatService;
    private final ChatgptService chatgptService;

    @PostMapping("/elementsinfo")
    public String test(@RequestBody String question){
        String answer = chatService.getChatResponse(question);
        // JSON 형식의 응답 문자열 생성
        String Response = "{\"answer\":\"" + answer + "\"}";
        return Response;
    }
}