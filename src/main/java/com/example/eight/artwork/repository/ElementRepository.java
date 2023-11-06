package com.example.eight.artwork.repository;

import com.example.eight.artwork.entity.Element;
import com.example.eight.artwork.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElementRepository extends JpaRepository<Element, Long> {
    Element findByName(String name);

    Element findByNameKr(String name);


    // 부분에 속하는 element 리스트 조회
    List<Element> findByPart(Part part);    

    @Override
    <S extends Element> S save(S entity);
}

