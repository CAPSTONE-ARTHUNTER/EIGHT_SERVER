package com.example.eight.artwork.service;

import com.example.eight.artwork.dto.*;
import com.example.eight.artwork.entity.*;
import com.example.eight.artwork.repository.*;

import com.example.eight.user.entity.User;
import com.example.eight.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ArtworkService {

    private final RelicRepository relicRepository;
    private final ElementRepository elementRepository;
    private final SolvedElementRepository solvedElementRepository;
    private final PartRepository partRepository;
    private final SolvedPartRepository solvedPartRepository;
    private final SolvedRelicRepository solvedRelicRepository;
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
        // 로그인한 유저
        User loginUser = userService.getAuthentication();

        // 인식한 요소의 name이 DB에 있다면 solvedElement 테이블에 기록
        String elementName = requestDto.getName();
        Element element = elementRepository.findByName(elementName);
        if (element != null) {
            saveSolvedElement(loginUser, element);

            // 유저가 해당 part의 모든 element를 수집했다면 solvedPart 테이블에 기록
            Part part = element.getPart();
            if (isPartSolved(loginUser, part)) {
                saveSolvedPart(loginUser, part);
                log.info("부분 수집 완료 - 부분 name : {}", part.getName());

                // 유저가 해당 relic의 모든 part를 수집했다면 solvedRelic 테이블에 기록
                Relic relic = part.getRelic();
                if(isRelicSolved(loginUser, relic)){
                    saveSolvedRelic(loginUser, relic);
                    log.info("작품 수집 완료 - 작품 id : {}", relic.getId());
                }
            }
        }

        return element;
    }

    private void saveSolvedElement(User loginUser, Element element) {
        // 유저가 해당 element를 이미 수집한 적이 있다면, solvedAt만 업데이트
        SolvedElement solvedElement = solvedElementRepository.findByUserIdAndElementId(loginUser.getId(), element.getId());
        if( solvedElement != null){
            log.info("이미 수집했던 Element 입니다. 수집시각만 업데이트 합니다.");
            solvedElement.updateSolvedAt(LocalDateTime.now());
        }
        // 해당 element를 처음 수집하는 경우, 새로 수집기록 저장
        else {
            log.info("처음 수집하는 Element 입니다. 데이터를 새로 저장합니다.");
            SolvedElement newSolvedElement = SolvedElement.builder()
                    .element(element)
                    .user(loginUser)
                    .solvedAt(LocalDateTime.now())
                    .build();
            solvedElementRepository.save(newSolvedElement);
        }
    }

    private void saveSolvedPart(User loginUser, Part part){
        // 유저가 해당 part를 이미 수집한 적이 있다면, solvedAt만 업데이트
        SolvedPart solvedPart = solvedPartRepository.findByUserIdAndPartId(loginUser.getId(), part.getId());
        if( solvedPart != null){
            log.info("이미 수집했던 Part 입니다. 수집시각만 업데이트 합니다.");
            solvedPart.updateSolvedAt(LocalDateTime.now());
        }
        // 해당 part를 처음 수집하는 경우, 새로 수집기록 저장
        else {
            SolvedPart newSolvedPart = SolvedPart.builder()
                    .part(part)
                    .user(loginUser)
                    .solvedAt(LocalDateTime.now())
                    .build();

            solvedPartRepository.save(newSolvedPart);
        }
    }

    private void saveSolvedRelic(User loginUser, Relic relic){
        // 유저가 해당 relic을 이미 수집한 적이 있다면, solvedAt만 업데이트
        SolvedRelic solvedRelic = solvedRelicRepository.findByUserIdAndRelicId(loginUser.getId(), relic.getId());
        if( solvedRelic != null){
            log.info("이미 수집했던 Relic 입니다. 수집시각만 업데이트 합니다.");
            solvedRelic.updateSolvedAt(LocalDateTime.now());
        }
        // 해당 relic을 처음 수집하는 경우, 새로 수집기록 저장
        else {
            SolvedRelic newSolvedRelic = SolvedRelic.builder()
                    .relic(relic)
                    .user(loginUser)
                    .solvedAt(LocalDateTime.now())
                    .build();

            solvedRelicRepository.save(newSolvedRelic);
        }
    }

    private boolean isPartSolved(User loginUser, Part part){
        int solvedElementNum = getSolvedElementNum(loginUser.getId(),part.getId());   // 유저가 해당 part 중 수집한 element 개수
        int elementNum = part.getElementNum();  // 해당 part의 총 element 개수

        // 해당 part의 총 요소 개수와 유저가 수집한 요소 개수 동일한지 여부 리턴
        return (solvedElementNum == elementNum);
    }

    private boolean isRelicSolved(User loginUser, Relic relic){
        int solvedPartNum = getSolvedPartNum(loginUser.getId(), relic.getId());   // 유저가 해당 relic 중 수집한 part 개수
        int partNum = relic.getPartNum();   // 해당 relic의 총 part 개수

        // 해당 relic의 부분 개수와 유저가 수집한 부분 개수가 동일한지 여부 리턴
        return (solvedPartNum == partNum);
    }

    // 작품 소제목 조회 API
    public PartsResponseDto getArtworkParts(Long relicId) {
        // 현재 로그인된 사용자 정보
        User loginUser = userService.getAuthentication();

        // 작품 정보 조회
        Optional<Relic> optionalRelic = relicRepository.findById(relicId);
        if (optionalRelic.isEmpty()) {
            return null;
        }

        Relic relic = optionalRelic.get();

        // 공공 API에서 작품 이미지 URI, 작품 이름 가져오기
        String relicImage = relic.getImage();    // 이미지는 DB에 저장
        String relicName = getRelicInfoByAPI(relicId, "nameKr");

        // 부분 정보 조회 및 변환
        List<Part> parts = partRepository.findByRelic(relic);
        List<PartInfoDto> partInfoDtos = new ArrayList<>();  // 변환된 부분 정보 저장할 리스트

        // 각 부분에 대한 정보 변환
        for (Part part : parts) {
            boolean part_isSolved = solvedPartRepository.existsByUserIdAndPartId(loginUser.getId(), part.getId());

            PartInfoDto partInfoDto = PartInfoDto.builder()
                    .name(part.getName())  // 부분 이름
                    .isSolved(part_isSolved)  // 현재 사용자가 해당 부분 획득 여부
                    .build();

            partInfoDtos.add(partInfoDto);  // 변환된 부분 정보 리스트에 추가
        }

        // 획득한 부분 개수
        int totalSolvedPartCount = (int) partInfoDtos.stream().filter(PartInfoDto::isSolved).count();

        PartsResponseDto responseDto = PartsResponseDto.builder()
                .relicName(relicName)
                .relicImage(relicImage)  // 작품 이미지 URI 설정
                .relicBadgeImage(relic.getBadgeImage())
                .partInfos(partInfoDtos)
                .totalPartCount(parts.size())
                .totalSolvedPartCount(totalSolvedPartCount)
                .build();

        return responseDto;  // 최종 응답 객체
    }

    // 부분의 요소 조회 API (카메라 페이지)
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

        // 공공 API에서 작품 이름 가져오기
        String relicName = getRelicInfoByAPI(relicId, "nameKr");

        // 2. 최종 DTO 생성
        ElementResponseDto elementResponseDto = ElementResponseDto.builder()
                .relicName(relicName)
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
    }

    // relic의 part별 해설 찾아 반환하는 메소드
    public PartDescriptionResponseDto getPartDescription(Long relicId){
        // 현재 로그인한 유저
        User loginUser = userService.getAuthentication();
        // 작품
        Relic relic = findRelic(relicId);

        // 1. 작품의 부분 리스트 가져와서, 각 부분마다 해설 DTO 생성
        List<Part> partList = partRepository.findByRelic(relic);
        List<PartDescriptionInfoDto> partDescriptionInfoDtoList = new ArrayList<>();

        for(Part part: partList){
            // 로그인한 유저의 각 part 수집 완료여부 확인
            boolean part_isSolved = solvedPartRepository.existsByUserIdAndPartId(loginUser.getId(), part.getId());

            PartDescriptionInfoDto partDescriptionInfoDto = PartDescriptionInfoDto.builder()
                    .name(part.getName())
                    .description(part.getTextDescription())
                    .isSolved(part_isSolved)
                    .build();

            // 요소 상세정보를 요소 리스트에 추가
            partDescriptionInfoDtoList.add(partDescriptionInfoDto);
        }

        // 2. 공공 API에서 작품 정보 가져오기
        String relicImage = relic.getImage();   // 이미지는 DB에 저장
        String relicName = getRelicInfoByAPI(relicId,"nameKr");
        String author = getRelicInfoByAPI(relicId,"author");
        String nationality = getRelicInfoByAPI(relicId,"nationalityName2");

        // 3. 최종 DTO 생성
        PartDescriptionResponseDto partDescriptionResponseDto = PartDescriptionResponseDto.builder()
                .relicId(relic.getId())
                .relicImage(relicImage)
                .relicName(relicName)
                .author(author)
                .nationality(nationality)
                .partNum(relic.getPartNum())
                .partDescriptionInfoList(partDescriptionInfoDtoList)
                .build();

        return partDescriptionResponseDto;
    }

    // 부분의 해설 및 요소정보 조회 API
    public PartDescriptionAndElementResponseDto getPartDescriptionAndElement(Long relicId, Long partId){
        // 현재 로그인한 유저
        User loginUser = userService.getAuthentication();
        // 작품과 부분
        Relic relic = findRelic(relicId);
        Part part = findPart(partId);

        // 1. 부분 수집여부 확인
        boolean part_isSolved = solvedPartRepository.existsByUserIdAndPartId(loginUser.getId(), partId);
        // 2. 수집한 요소 수 가져오기
        int solvedElementNum = getSolvedElementNum(loginUser.getId(), partId);

        // 3. 부분의 요소 리스트 가져와서, 각 요소마다 요소정보 DTO 생성
        List<Element> elementList = elementRepository.findByPart(part);
        List<ElementInfoAndPointDto> elementInfoAndPointDtoList = new ArrayList<>();

        for(Element element: elementList){
            // 로그인한 유저의 각 요소 수집 완료여부 확인
            boolean element_isSolved = solvedElementRepository.existsByUserIdAndElementId(loginUser.getId(), element.getId());

            ElementInfoAndPointDto elementInfoAndPointDto = ElementInfoAndPointDto.builder()
                    .id(element.getId())
                    .point(element.getPoint())
                    .isSolved(element_isSolved)
                    .build();

            // 요소 정보를 요소 리스트에 추가
            elementInfoAndPointDtoList.add(elementInfoAndPointDto);
        }

        // 4. 공공 API에서 작품 정보 가져오기
        String relicImage = relic.getImage();   // 이미지는 DB에 저장
        String relicName = getRelicInfoByAPI(relicId,"nameKr");

        // 5. 최종 DTO 생성
        PartDescriptionAndElementResponseDto partDescriptionAndElementResponseDto = PartDescriptionAndElementResponseDto.builder()
                // 작품 정보
                .relicId(relic.getId())
                .relicImage(relicImage)
                .relicName(relicName)
                // 부분 정보
                .partName(part.getName())
                .partDescription(part.getTextDescription())
                .part_isSolved(part_isSolved)
                // 요소 정보
                .elementNum(part.getElementNum())
                .elementSolvedNum(solvedElementNum)
                .elementList(elementInfoAndPointDtoList)
                .build();

        return partDescriptionAndElementResponseDto;
    }

    // 유저가 수집완료한 요소 수 가져오는 메소드
    private int getSolvedElementNum(Long userId, Long partId) {
        // 부분 찾기
        Part part = findPart(partId);
        // 부분의 모든 요소 list
        List<Element> elementList = elementRepository.findByPart(part);

        return (int) elementList.stream()
                // 유저의 각 요소 수집 완료여부
                .filter(element -> solvedElementRepository.existsByUserIdAndElementId(userId, element.getId()))
                .count();   // true인 것만 카운트
    }

    // 유저가 수집완료한 부분 수 가져오는 메소드
    private int getSolvedPartNum(Long userId, Long relicId) {
        // 작품 찾기
        Relic relic = findRelic(relicId);
        // 작품의 모든 부분 list
        List<Part> partList = partRepository.findByRelic(relic);

        return (int) partList.stream()
                // 유저의 각 부분 수집 완료여부
                .filter(part -> solvedPartRepository.existsByUserIdAndPartId(userId, part.getId()))
                .count();   // true인 것만 카운트
    }

    // 공공 API로 작품의 target 정보 가져오는 메소드
    public String getRelicInfoByAPI(Long relicId, String target) {
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
                    String targetInfo = firstItem.get(target).asText();

                    return targetInfo;
                } else {
                    // API 응답에서 이미지 URI를 찾을 수 없는 경우
                    return String.format("Artwork %s not found in API response.", target);
                }
            } catch (Exception e) {
                // 예외 처리
                e.printStackTrace();
                return String.format("Error occurred while fetching %s: ", target) + e.getMessage();
            }
        } else {
            // 작품이 발견되지 않았을 때
            return "Relic not found for ID: " + relicId;
        }
    }

    // 태그 인식 API
    public TagResponseDto performTagRecognition(TagRequestDto requestDto) {
        String detectedTag = requestDto.getText();
        // 응답 객체 생성 전 변수 초기화
        String bestMatchingApiId = null;
        Long dbId = null;
        String name = null;

        // 태그와 유사한 작품명 찾기
        List<Long> allArtworkApiIds = getAllArtworkApiIds();    // db에 저장된 모든 작품의 api id 리스트
        bestMatchingApiId = findBestMatchingId(detectedTag, allArtworkApiIds);  // 찾은 작품의 api id

        if (bestMatchingApiId != null) {
            // 찾은 api id로 작품 객체 가져오기
            Optional<Relic> optionalRelic = relicRepository.findByApiId(bestMatchingApiId);
            Relic relic = optionalRelic.get();

            // db의 작품 id 가져오기
            dbId = relic.getId();
            // 작품명 가져오기
            name = getRelicInfoByAPI(dbId, "nameKr");
        }

        // 응답 객체 생성
        if (name != null) {
            return TagResponseDto.builder()
                    .id(dbId)
                    .name(name)  // 유사한 작품명 사용
                    .build();
        } else {
            return null;  // 유사한 작품명을 찾지 못한 경우
        }
    }

    // 작품의 모든 apiID 가져오기
    private List<Long> getAllArtworkApiIds() {
        List<Relic> relics = relicRepository.findAll();
        List<Long> artworkIds = new ArrayList<>();

        for (Relic relic : relics) {
            artworkIds.add(relic.getId());
        }

        return artworkIds;
    }

    // 유사한 작품명 찾기
    // Jaro-Winkler 유사도 계산
    private String findBestMatchingId(String detectedTag, List<Long> relicApiIds) {
        String bestMatchingId = null;
        double maxSimilarity = 0.0;

        JaroWinklerDistance jaroWinklerDistance = new JaroWinklerDistance();

        for (Long id : relicApiIds) {
            String foundName = getRelicInfoByAPI(id,"nameKr");  // 작품명
            String foundApiId = getRelicInfoByAPI(id,"id");     // 공공 api의 작품 id

            // 작품명으로 유사도 계산
            double similarity = jaroWinklerDistance.apply(detectedTag, foundName);
            if (similarity > maxSimilarity) {
                // 더 큰 유사도를 찾았을 경우 값을 업데이트하고 api id 저장
                maxSimilarity = similarity;
                bestMatchingId = foundApiId;

            }
        }

        return bestMatchingId;  // 가장 유사한 작품명의 api id 반환
    }

}



