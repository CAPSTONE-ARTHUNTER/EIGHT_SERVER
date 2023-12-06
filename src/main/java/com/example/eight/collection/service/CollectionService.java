package com.example.eight.collection.service;

import com.example.eight.artwork.dto.PartInfoDto;
import com.example.eight.artwork.entity.Part;
import com.example.eight.artwork.entity.Relic;
import com.example.eight.artwork.repository.PartRepository;
import com.example.eight.artwork.repository.RelicRepository;
import com.example.eight.artwork.repository.SolvedPartRepository;
import com.example.eight.artwork.repository.SolvedRelicRepository;
import com.example.eight.artwork.service.ArtworkService;
import com.example.eight.collection.dto.*;
import com.example.eight.user.entity.User;
import com.example.eight.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CollectionService {

    private final UserService userService;
    private final RelicRepository relicRepository;
    private final SolvedRelicRepository solvedRelicRepository;
    private final PartRepository partRepository;
    private final SolvedPartRepository solvedPartRepository;
    private final ArtworkService artworkService;

    // 수집 오버뷰 조회 API
    public OverviewResponseDto getOverview(){
        // 현재 로그인한 유저 정보
        User user = userService.getAuthentication();

        // 수집한 작품 리스트 가져오기
        List<Relic> collectedRelicList = getCollectedRelicList(user.getId());

        // 수집한 작품의 id와 badge 이미지 리스트 생성
        List<OverviewDto> overviewDtoList = new ArrayList<>();
        for(Relic relic: collectedRelicList){
            OverviewDto overviewDto = OverviewDto.builder()
                    .relicId(relic.getId())
                    .badgeImage(relic.getBadgeImage())
                    .build();

            overviewDtoList.add(overviewDto); // dto 리스트에 추가
        }

        // 오버뷰 응답 생성
        int solvedRelicNum = getCollectionNum(user.getId());
        OverviewResponseDto overviewResponseDto = OverviewResponseDto.builder()
                .userImage(user.getPicture())
                .userExp(user.getExp())
                .solvedRelicNum(solvedRelicNum)
                .badgeList(overviewDtoList)
                .build();

        return overviewResponseDto;
    }

    // 수집한 작품 리스트 반환하는 메소드
    public List<Relic> getCollectedRelicList(Long userId){
        List<Relic> collectionList = new ArrayList<Relic>();      // 도감

        // 해당 유저의 수집한 relic 리스트 가져오기
        List<Relic> relicList = relicRepository.findAll();
        for(Relic relic: relicList) {
            boolean isSolved = solvedRelicRepository.existsByUserIdAndRelicId(userId, relic.getId());

            if(isSolved == false){
                continue;
            }
            collectionList.add(relic);
        }

        return collectionList;
    }

    // 유저가 수집완료한 작품 수 가져오는 메소드
    private int getCollectionNum(Long userId) {
        int count = 0;
        List<Relic> relicList = relicRepository.findAll();  // 모든 작품의 list

        // 수집한 개수 카운트
        for(Relic relic: relicList){
            boolean isSolved = solvedRelicRepository.existsByUserIdAndRelicId(userId, relic.getId());
            if(isSolved){
                count++;
            }
        }

        return count;
    }

    // 도감 조회 API
    public CollectionResponseDto getCollection(){
        // 현재 로그인한 유저 정보
        User user = userService.getAuthentication();

        // 수집한 작품 리스트 가져오기
        List<Relic> collectedRelicList = getCollectedRelicList(user.getId());

        // 수집한 작품의 id와 작품 이미지 리스트 생성
        List<CollectionDto> collectionDtoList = new ArrayList<>();
        for(Relic relic: collectedRelicList){
            CollectionDto collectionDto = CollectionDto.builder()
                    .relicId(relic.getId())
                    .relicImage(relic.getImage())
                    .build();

            collectionDtoList.add(collectionDto); // dto 리스트에 추가
        }

        // 도감 응답 생성
        int totalRelicNum = (int) relicRepository.count();  // 모든 작품의 개수
        CollectionResponseDto collectionResponseDto = CollectionResponseDto.builder()
                .totalRelicNum(totalRelicNum)
                .solvedRelicList(collectionDtoList)
                .build();

        return collectionResponseDto;
    }

    // 챌린지 조회 API
    public ChallengeResponseDto getChallengeList() {
        // 현재 로그인한 유저 정보
        User user = userService.getAuthentication();

        // 전체 작품 리스트 가져오기
        List<Relic> RelicList = relicRepository.findAll();
        // 각 작품의 정보, partList, 수집여부 가져오기
        List<ChallengeDto> challengeDtoList = new ArrayList<>();
        for (Relic relic : RelicList) {
            Long relicId = relic.getId();
            Long userId = user.getId();

            int solvedPartNum = artworkService.getSolvedPartNum(userId, relicId);
            List<PartInfoDto> partList = getPartList(userId, relicId);  // part의 목록과 정보, 수집여부 가져오기

            ChallengeDto challengeDto = ChallengeDto.builder()
                    .relicId(relicId)
                    .relicName(artworkService.getRelicInfoByAPI(relicId, "nameKr"))
                    .badgeImage(relic.getBadgeImage())
                    .partNum(relic.getPartNum())
                    .solvedPartNum(solvedPartNum)
                    .partList(partList)
                    .build();

            challengeDtoList.add(challengeDto); // dto 리스트에 추가
        }

        // 챌린지 조희 응답 생성
        ChallengeResponseDto challengeResponseDto = ChallengeResponseDto.builder()
                .challengeList(challengeDtoList)
                .build();

        return challengeResponseDto;
    }

    // part List 가져오기
    public List<PartInfoDto> getPartList(Long userId, Long relicId){
        // relic Id로 부분 찾기
        List<Part> parts = partRepository.findByRelicId(relicId);
        List<PartInfoDto> partInfoDtoList = new ArrayList<>();

        // 각 부분의 세부 정보
        for (Part part : parts) {
            boolean part_isSolved = solvedPartRepository.existsByUserIdAndPartId(userId, part.getId());

            PartInfoDto partInfoDto = PartInfoDto.builder()
                    .partId(part.getId())
                    .name(part.getName())
                    .isSolved(part_isSolved)  // 부분 수집여부
                    .build();

            partInfoDtoList.add(partInfoDto);  // dto 리스트에 추가
        }

        return partInfoDtoList;
    }
}
