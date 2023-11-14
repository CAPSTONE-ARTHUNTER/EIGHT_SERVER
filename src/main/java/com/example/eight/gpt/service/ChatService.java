package com.example.eight.gpt.service;

import com.example.eight.artwork.entity.Part;
import com.example.eight.artwork.entity.Relic;
import com.example.eight.artwork.entity.Element;
import com.example.eight.artwork.repository.ElementRepository;
import com.example.eight.artwork.service.ArtworkService;
import com.example.eight.gpt.dto.GptResponseDto;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ElementRepository elementRepository;
    private final ArtworkService artworkService;
    private final ChatgptService chatgptService;

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

        logger.info("getRelicDescriptionForElement에서 받은 element: {}", element);

        if (element == null) {
            return "Element not found.";
        }

        // 요소가 속한 부분 및 작품 정보 가져오기
        Relic relic = element.getPart().getRelic();
        Long relicId = relic.getId();

        // 작품의 전체 해설 가져오기
        return artworkService.getRelicDescription(relicId);
    }

    // STEP3: 요소 영문명으로 국문명 찾기
    public String findElementNameKr(String elementName) {
        Element element = findElementByName(elementName);
        if (element != null) {
            return element.getNameKr();
        }
        return null; // 요소를 찾지 못한 경우
    }

    // STEP4: 요소 이름과 작품 해설을 사용하여 GPT에 정보 요청 + 작품 정보 찾아와 응답 객체 만들기
    public GptResponseDto generateDescription(String nameKr, String relicDescription) {

        String prompt = "작품 전체해설에서 " + nameKr + " 정보 찾아줘."
                + "전체해설:\n" + relicDescription;
        String elementDescription = chatgptService.sendMessage(prompt);

        // 작품 정보 불러오기
        Element element = elementRepository.findByNameKr(nameKr);
        String elementImage = element.getImage();
        Relic relic = element.getPart().getRelic();
        Long relicId = relic.getId();
        String relicName = relic.getNameEn();

        // 응답 객체 반환
        return new GptResponseDto(relicId, relicName, elementImage, nameKr, elementDescription);
    }

    // STEP5: GPT 응답 반환
    public GptResponseDto getElementInfo(String elementName) {
        String nameKr = findElementNameKr(elementName);

        if (nameKr != null) {
            // GPT 요청 보내기
            String relicDescription = getRelicDescriptionForElement(findElementByName(elementName));
            return generateDescription(nameKr, relicDescription);
        }
        return new GptResponseDto(null, null, null, null, "Error: Element not found.");
    }

}