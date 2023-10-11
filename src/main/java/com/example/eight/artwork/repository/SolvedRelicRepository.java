package com.example.eight.artwork.repository;

import com.example.eight.artwork.entity.SolvedRelic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedRelicRepository extends JpaRepository<SolvedRelic, Long> {

    SolvedRelic findByUserIdAndRelicId(Long userId, Long relicId);

    boolean existsByUserIdAndRelicId(Long userId, Long relicId);
}
