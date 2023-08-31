package com.example.eight.user.service;

import com.example.eight.user.entity.User;
import com.example.eight.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
//@Transactional
@Service
public class UserService {
    //추가
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
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
