package com.example.eight.artwork.service;

import com.example.eight.artwork.dto.DetectionRequestDto;
import com.example.eight.artwork.dto.DetectionResponseDto;
import com.example.eight.artwork.dto.PartInfoDto;
import com.example.eight.artwork.dto.PartsResponseDto;
import com.example.eight.artwork.entity.Element;
import com.example.eight.artwork.entity.Part;
import com.example.eight.artwork.entity.Relic;
import com.example.eight.artwork.entity.SolvedElement;
import com.example.eight.artwork.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtworkService {

    private final RelicRepository relicRepository;
    private final ElementRepository elementRepository;
    private final SolvedElementRepository solvedElementRepository;
    private final PartRepository partRepository;
    private final SolvedPartRepository solvedPartRepository;

    // 요소 인식 API
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
        Element element = elementRepository.findByName(elementName);

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

    // 작품 소제목 조회 API
    public PartsResponseDto getArtworkParts(Long relicId) {

        // TODO: 현재 로그인된 사용자 정보 가져오는 로직 추가 필요
        // User currentUser = getCurrentUser();

        // 작품 정보 조회
        Optional<Relic> optionalRelic = relicRepository.findById(relicId);
        if (optionalRelic.isEmpty()) {
            return null;
        }

        Relic relic = optionalRelic.get();

        // 부분 정보 조회 및 변환
        List<Part> parts = partRepository.findByRelic(relic);
        List<PartInfoDto> partInfoDtos = new ArrayList<>();  // 변환된 부분 정보 저장할 리스트

        // 각 부분에 대한 정보 변환
        for (Part part : parts) {
            int solvedPartCount = solvedPartRepository.countByPartAndIsSolved(part, true);
            boolean isPartSolvedByCurrentUser = false;  // 현재 사용자가 해당 부분을 획득했는지 여부

            // TODO: 사용자가 해당 부분을 획득했는지 확인하는 로직 필요
            // isPartSolvedByUser(part, currentUser);

            PartInfoDto partInfoDto = PartInfoDto.builder()
                    .name(part.getName())  // 부분 이름
                    .isSolved(isPartSolvedByCurrentUser)  // 현재 사용자가 해당 부분 획득 여부
                    .build();

            partInfoDtos.add(partInfoDto);  // 변환된 부분 정보 리스트에 추가
        }

        // 획득한 부분 개수
        int totalSolvedPartCount = (int) partInfoDtos.stream().filter(PartInfoDto::isSolved).count();

        PartsResponseDto responseDto = PartsResponseDto.builder()
                .relicName(relic.getName())
                .relicImage(relic.getImage())
                .partInfos(partInfoDtos)
                .totalPartCount(parts.size())
                .totalSolvedPartCount(totalSolvedPartCount)
                .build();

        return responseDto;  // 최종 응답 객체
    }

}




