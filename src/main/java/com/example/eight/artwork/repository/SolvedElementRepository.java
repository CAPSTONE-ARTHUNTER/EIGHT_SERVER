package com.example.eight.artwork.repository;

import com.example.eight.artwork.entity.SolvedElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedElementRepository extends JpaRepository<SolvedElement, Long> {

    boolean existsByUserIdAndElementId(Long userId, Long elementId);

    SolvedElement findByUserIdAndElementId(Long userId, Long elementId);
}