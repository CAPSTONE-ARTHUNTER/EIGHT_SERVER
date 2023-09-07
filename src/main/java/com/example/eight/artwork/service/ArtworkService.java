package com.example.eight.artwork.service;

import com.example.eight.artwork.dto.*;
import com.example.eight.artwork.entity.Element;
import com.example.eight.artwork.entity.Part;
import com.example.eight.artwork.entity.Relic;
import com.example.eight.artwork.entity.SolvedElement;
import com.example.eight.artwork.repository.*;

import com.example.eight.user.entity.User;
import com.example.eight.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtworkService {

    private final RelicRepository relicRepository;
    private final ElementRepository elementRepository;
    private final SolvedElementRepository solvedElementRepository;
    private final PartRepository partRepository;
    private final SolvedPartRepository solvedPartRepository;
    private final UserService userService;

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

        // 작품 이미지 URI 가져오기
        String relicImageUri = getRelicImageUri(relicId);

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
                .relicImage(relicImageUri)  // 작품 이미지 URI 설정
                .relicBadgeImage(relic.getBadgeImage())
                .partInfos(partInfoDtos)
                .totalPartCount(parts.size())
                .totalSolvedPartCount(totalSolvedPartCount)
                .build();

        return responseDto;  // 최종 응답 객체
    }

    // part의 각 element 정보와 수집여부 조회 API
    public ElementResponseDto getElementDetail(Long relicId, Long partId) {
        // 현재 로그인한 유저
        User loginUser = userService.getAuthentication();

        // 쿼리 파라미터로 작품과 부분 찾기
        Relic relic = findRelic(relicId);
        Part part = findPart(partId);

        // 1. 부분의 요소 리스트 가져와서, 각 요소마다 상세정보 DTO 생성
        List<Element> elementList = elementRepository.findByPart(part);

        List<ElementInfoDto> elementInfoDtoList = new ArrayList<>();
        for(Element element: elementList){
            // 로그인한 유저의 각 element 수집 완료여부 확인
            boolean element_isSolved = solvedElementRepository.existsByUserIdAndElementId(loginUser.getId(), element.getId());

            ElementInfoDto elementInfoDto = ElementInfoDto.builder()
                    .id(element.getId())
                    .name(element.getName())
                    .image(element.getImage())
                    .isSolved(element_isSolved)
                    .build();

            // 요소 상세정보를 요소 리스트에 추가
            elementInfoDtoList.add(elementInfoDto);
        }

        // 2. 최종 DTO 생성
        ElementResponseDto elementResponseDto = ElementResponseDto.builder()
                .relicName(relic.getName())
                .partName(part.getName())
                .elementInfoList(elementInfoDtoList)
                .build();

        return elementResponseDto;
    }

    // relic id로 Relic 객체 찾아 반환하는 메소드
    private Relic findRelic(Long relicId) {
        return relicRepository.findById(relicId)
                .orElseThrow(() -> new EntityNotFoundException("작품을 찾을 수 없습니다."));
    }

    // part id로 Part 객체 찾아 반환하는 메소드
    private Part findPart(Long partId) {
        return partRepository.findById(partId)
                .orElseThrow(() -> new EntityNotFoundException("부분을 찾을 수 없습니다."));
    // 작품 이미지 URI 가져오는 메서드
    public String getRelicImageUri(Long relicId) {
        // 작품 id로 작품 찾기
        Optional<Relic> relicOptional = relicRepository.findById(relicId);

        if (relicOptional.isPresent()) {
            Relic relic = relicOptional.get();

            try {
                String serviceKey = "enter the service key";

                // API 요청 URL 생성
                String apiUrl = "http://www.emuseum.go.kr/openapi/relic/detail?serviceKey=" + URLEncoder.encode(serviceKey, "UTF-8") + "&id=" + relic.getApiId();

                URI uri = new URI(apiUrl);
                RestTemplate restTemplate = new RestTemplate();
                String apiResponse = restTemplate.getForObject(uri, String.class);

                // API 응답 파싱하여 이미지 uri 가져오기
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(apiResponse);
                JsonNode listNode = jsonNode.get("list");

                if (listNode != null && listNode.isArray() && listNode.size() > 0) {
                    JsonNode firstItem = listNode.get(0);
                    String artworkImageUrl = firstItem.get("imgUri").asText();

                    return artworkImageUrl;
                } else {
                    // API 응답에서 이미지 URI를 찾을 수 없는 경우
                    return "Artwork image not found in API response.";
                }
            } catch (Exception e) {
                // 예외 처리
                e.printStackTrace();
                return "Error occurred while fetching image URI: " + e.getMessage();
            }
        } else {
            // 작품이 발견되지 않았을 때
            return "Relic not found for ID: " + relicId;
        }
    }

    // 태그 인식 API
    public TagResponseDto performTagRecognition(TagRequestDto requestDto) {
        String detectedTag = requestDto.getText();

        // 데이터베이스에서 태그와 일치하는 작품 정보 조회
        Relic relic = relicRepository.findByName(detectedTag);

        // 응답 객체 생성 전 relicId 변수 초기화
        Long relicId;
        String bestMatchingName = null;

        // 데이터베이스에 태그와 일치하는 작품명이 있는 경우
        if (relic != null) {
            // ID 설정
            relicId = relic.getId();
            bestMatchingName = detectedTag;
        } else {
            // 데이터베이스에 태그와 일치하는 작품명이 없을 때, 유사한 작품명 찾기
            List<String> allArtworkNames = getAllArtworkNames();
            bestMatchingName = findBestMatchingName(detectedTag, allArtworkNames);

            if (bestMatchingName != null) {
                // ID 설정
                relic = relicRepository.findByName(bestMatchingName);
                if (relic != null) {
                    relicId = relic.getId();
                } else {
                    relicId = null;
                }
            } else {
                // 유사한 작품명도 없을 경우
                relicId = null;
            }
        }

        // 응답 객체 생성
        if (relicId != null) {
            return TagResponseDto.builder()
                    .id(relicId)
                    .name(bestMatchingName)  // 유사한 작품명 사용
                    .build();
        } else {
            return null;  // 유사한 작품명을 찾지 못한 경우
        }
    }

    // 모든 작품명 가져오기
    private List<String> getAllArtworkNames() {
        List<Relic> relics = relicRepository.findAll();
        List<String> artworkNames = new ArrayList<>();

        for (Relic relic : relics) {
            artworkNames.add(relic.getName());
        }

        return artworkNames;
    }

    // 유사한 작품명 찾기
    // Jaro-Winkler 유사도 계산
    private String findBestMatchingName(String detectedTag, List<String> artworkNames) {
        String bestMatchingName = null;
        double maxSimilarity = 0.0;

        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

        for (String name : artworkNames) {
            // 유사도 계산
            double similarity = jaroWinklerDistance.apply(detectedTag, name);
            if (similarity > maxSimilarity) {
                // 더 큰 유사도를 찾았을 경우 값을 업데이트하고 작품명 저장
                maxSimilarity = similarity;
                bestMatchingName = name;
            }
        }

        return bestMatchingName;  // 가장 유사한 작품명 반환
    }

}



