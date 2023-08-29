package com.example.eight.artwork.repository;

import com.example.eight.artwork.entity.Part;
import com.example.eight.artwork.entity.Relic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelicRepository extends JpaRepository<Relic, Long> {

    @Override
    <S extends Relic> S save(S entity);
}
