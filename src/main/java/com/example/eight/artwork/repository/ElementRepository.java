package com.example.eight.artwork.repository;

import com.example.eight.artwork.entity.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElementRepository extends JpaRepository<Element, Long> {
    Element findByName(String name);

    @Override
    <S extends Element> S save(S entity);
}

