package com.example.eight.artwork.repository;

import com.example.eight.artwork.entity.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkRepository extends JpaRepository<Element, Long> {
    //Element findElementByClassName(String elementClassName);

    //SolvedElement saveSolvedElement(SolvedElement solvedElement);
}