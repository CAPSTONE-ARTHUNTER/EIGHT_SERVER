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
    Element findByName(String name);

    @Override
    <S extends Element> S save(S entity);
}

