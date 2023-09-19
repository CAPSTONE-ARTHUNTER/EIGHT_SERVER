package com.example.eight.artwork.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Table(name = "relic")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Relic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "api_id")
    private String apiId;

    @NotNull
    @Column(name = "image", length = 300)
    private String image;

    @NotNull
    @Column(name = "badge_image", length = 300)
    private String badgeImage;

    @Column(name = "location", length = 100)
    private String location;

    @NotNull
    @Column(name = "part_num", nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 0")
    private Integer partNum;

    @OneToMany(mappedBy = "relic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Part> parts = new ArrayList<>();

    @Builder
    public Relic(String apiId, @NotNull String image, @NotNull String badgeImage,
                 String location, @NotNull Integer partNum, List<Part> parts) {
        this.apiId = apiId;
        this.image = image;
        this.badgeImage = badgeImage;
        this.location = location;
        this.partNum = partNum;
        this.parts = parts;
    }

}