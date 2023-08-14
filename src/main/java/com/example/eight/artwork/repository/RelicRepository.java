package com.example.eight.artwork.repository;

import com.example.eight.artwork.entity.Relic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelicRepository extends JpaRepository<Relic, Long> {
    //커스텀 쿼리 method 추가
}