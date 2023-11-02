package com.example.eight.gpt.service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatgptService chatgptService;
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private String answer;


    public String getChatResponse(String prompt) {
        answer = chatgptService.sendMessage(prompt);
        logger.info("Received GPT response: {}", answer);
        return answer;

    }
}