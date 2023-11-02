package com.example.eight.artwork.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.awt.geom.Point2D;

@Entity
@Getter
@Table(name = "element")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Element implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @NotNull
    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "image", length = 300)
    private String image;

    @Column(name = "point", length = 100)  // "x:10, y:20"
    private String point;

    @Lob
    @Column(name = "audio_description")
    private byte[] audioDescription;

    @Builder
    public Element(Part part, @NotNull String name, String image, String point, byte[] audioDescription) {
        this.part = part;
        this.name = name;
        this.image = image;
        this.point = point;
        this.audioDescription = audioDescription;
    }

}
