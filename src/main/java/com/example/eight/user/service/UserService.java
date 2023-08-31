package com.example.eight.user.service;

import com.example.eight.user.entity.User;
import com.example.eight.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            user.updateRefreshToken(refreshToken);
            userRepository.save(user); // 변경 사항을 데이터베이스에 저장
        }
    }
}
