package com.example.eight.artwork.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.annotation.Id;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Table(name = "Relic")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Relic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "api_id")
    private Long apiId;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "image", nullable = false)
    private String image;

    @NotNull
    @Column(name = "badge_image", nullable = false)
    private String badgeImage;

    @Column(name = "location")
    private String location;

    @NotNull
    @Column(name = "part_num", nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 0")
    private Integer partNum;
}