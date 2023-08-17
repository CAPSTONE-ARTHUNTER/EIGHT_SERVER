package com.example.eight.artwork.service;

import com.example.eight.artwork.dto.DetectionRequestDto;
import com.example.eight.artwork.dto.DetectionResponseDto;
import com.example.eight.artwork.entity.Element;
import com.example.eight.artwork.entity.SolvedElement;
import com.example.eight.artwork.repository.ArtworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;

    public DetectionResponseDto.DetectionData performDetection(DetectionRequestDto requestDto) {
        // 클라이언트로부터 받은 요청으로 작품 부분에 해당하는 요소(element)를 검증
        Element detectedElement = validateAndRecordElement(requestDto);

        return DetectionResponseDto.DetectionData.builder()
                .image(detectedElement.getImage())
                .name(detectedElement.getName())
                .build();
    }

    private Element validateAndRecordElement(DetectionRequestDto requestDto) {
        String elementClassName = requestDto.getPredictions().get(0).getElementClassName();
        Element element = artworkRepository.findElementByClassName(elementClassName);

        if (element != null) {
            saveSolvedElement(element);
        }

        return element;
    }

    private void saveSolvedElement(Element element) {
        SolvedElement solvedElement = SolvedElement.builder()
                .element(element)
                .isSolved(true)
                .solvedAt(LocalDateTime.now())
                .build();
        artworkRepository.saveSolvedElement(solvedElement.getElement(), solvedElement.isSolved(), solvedElement.getSolvedAt());
    }

}




