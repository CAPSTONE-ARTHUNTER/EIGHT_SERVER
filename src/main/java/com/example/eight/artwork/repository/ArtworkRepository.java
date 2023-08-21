package com.example.eight.artwork.repository;

import com.example.eight.artwork.entity.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface ArtworkRepository extends JpaRepository<Element, Long> {
    @Query("SELECT e FROM Element e WHERE e.name = :name")
    Element findByName(String name);

    @Query("INSERT INTO SolvedElement(element, isSolved, solvedAt) VALUES (:element, :isSolved, :solvedAt)")
    void saveSolvedElement(@Param("element") Element element, @Param("isSolved") boolean isSolved, @Param("solvedAt") LocalDateTime solvedAt);
}

