package com.example.eight.user.repository;

import com.example.eight.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // email로 유저 조회
    Optional<User> findByEmail(String email);
}
