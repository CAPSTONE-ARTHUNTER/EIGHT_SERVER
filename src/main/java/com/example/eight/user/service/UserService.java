package com.example.eight.user.service;

import com.example.eight.user.Role;
import com.example.eight.user.SocialType;
import com.example.eight.global.oauth2.dto.UserInfoDto;
import com.example.eight.user.dto.UserProfileDto;
import com.example.eight.user.entity.User;
import com.example.eight.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // 현재 로그인한 Authentication 가져오기
    public User getAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String userEmail = principal.getUsername();

        log.info("Authentication에서 가져온 유저 이메일: {}", userEmail);

        // 로그인한 User 객체 반환
        return userRepository.findByEmail(userEmail).orElse(null);
    }


    public UserProfileDto getUserProfile() {

        User user = getAuthentication();    // 현재 로그인한 유저 정보

        return UserProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .picture(user.getPicture())
                .exp(user.getExp())
                .socialType(user.getSocialType())
                .build();
    }

    public void createOrUpdateUser(String email, String name, String picture, SocialType socialType) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        // 해당 유저가 이미 로그인한 적이 있다면 정보만 업데이트
        if (userOptional.isPresent()) {
            user = userOptional.get();
            log.info("로그인만 진행합니다.");
            user.update(name, picture);
        }
        // 최초 로그인이라면 User 생성
        else {
            log.info("회원가입 후 로그인합니다.");
            user = createUser(email, name, picture, socialType);
        }

        userRepository.save(user);
    }

    public User createUser(String email, String name, String picture, SocialType socialType){

        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.USER)   // TODO: 일단 USER로 설정
                .socialType(SocialType.GOOGLE)      //TODO: 일단 GOOGLE로 설정
                .build();
    }

    public UserInfoDto parseUserInfo(String userInfo) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(userInfo);
            String userEmail = jsonNode.get("email").asText();
            String userName = jsonNode.get("name").asText();
            String userPicture = jsonNode.get("picture").asText();
            return new UserInfoDto(userEmail, userName, userPicture);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
