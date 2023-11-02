package com.example.eight.gpt.service;

import com.example.eight.artwork.entity.Relic;
import com.example.eight.artwork.entity.Element;
import com.example.eight.artwork.repository.ElementRepository;
import com.example.eight.artwork.repository.RelicRepository;
import com.example.eight.artwork.service.ArtworkService;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RelicRepository relicRepository;
    private final ElementRepository elementRepository;
    private final ArtworkService artworkService;
    private final ChatgptService chatgptService;
    private String answer;

    // 로그 확인
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    // GPT 해설 조회 API

    // STEP1: 요소 이름으로 DB에서 요소 찾기
    public Element findElementByName(String name) {
        return elementRepository.findByName(name);
    }

    // STEP2: 요소가 속한 작품 찾아서 작품의 전체 해설 가져오기
    @Transactional
    public String getRelicDescriptionForElement(Element element) {
        // 요소가 속한 Part 및 Relic 정보 가져오기
        Relic relic = element.getPart().getRelic();

        Long relicId = relic.getId();
        return artworkService.getRelicDescription(relicId);
    }

    // STEP3: 요소 이름과 작품 해설을 사용하여 GPT에 정보 요청
    public String generateDescription(String name, String relicDescription) {
        String prompt = name +
                "부분에 대한 정보를 알고 싶어. 작품 전체 해설을 줄테니까 정보 알려줘. 이게 전체 해설이야:"
                + relicDescription ;
        return chatgptService.sendMessage(prompt);
    }

    // STEP4: GPT 응답 반환
    public String getElementInfo(String elementName) {
        Element element = findElementByName(elementName);

        // 요소가 존재하지 않을 시
        if (element == null) {
            return "Element not found.";
        }

        String relicDescription = getRelicDescriptionForElement(element);
        String description = generateDescription(elementName, relicDescription);

        logger.info("Received GPT response: {}", description);

        return description;
    }

}