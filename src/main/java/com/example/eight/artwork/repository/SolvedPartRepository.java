package com.example.eight.artwork.repository;

import com.example.eight.artwork.entity.Part;
import com.example.eight.artwork.entity.SolvedPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedPartRepository extends JpaRepository<SolvedPart, Long> {

    boolean existsByUserIdAndPartId(Long userId, Long partId);
}
