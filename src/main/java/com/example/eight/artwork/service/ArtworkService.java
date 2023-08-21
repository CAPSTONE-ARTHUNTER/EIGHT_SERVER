package com.example.eight.artwork.service;

import com.example.eight.artwork.dto.DetectionRequestDto;
import com.example.eight.artwork.dto.DetectionResponseDto;
import com.example.eight.artwork.entity.Element;
import com.example.eight.artwork.entity.SolvedElement;
import com.example.eight.artwork.repository.ArtworkRepository;
import com.example.eight.artwork.repository.SolvedElementRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;
    private final SolvedElementRepository solvedElementRepository;

    // 클라이언트로부터 받은 요청으로 작품 부분에 해당하는 요소(element)를 검증
    public DetectionResponseDto.DetectionData performDetection(DetectionRequestDto requestDto) {
        Element detectedElement = validateAndRecordElement(requestDto);

        if (detectedElement != null) {
            return DetectionResponseDto.DetectionData.builder()
                    .image(detectedElement.getImage())
                    .name(detectedElement.getName())
                    .build();
        } else {
            return null;
        }
    }
    private Element validateAndRecordElement(DetectionRequestDto requestDto) {
        String elementName = requestDto.getName();
        Element element = artworkRepository.findByName(elementName);

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
        solvedElementRepository.save(solvedElement);
    }

}




