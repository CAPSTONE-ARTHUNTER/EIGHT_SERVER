package com.example.eight.artwork.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter
@Table(name = "part")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Part implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relic_id", nullable = false)
    private Relic relic;

    @NotNull
    @Column(name = "name", length = 50)
    private String name;

    @NotNull
    @Column(name = "text_description", length = 500)
    private String textDescription;

    @Lob
    @Column(name = "audio_description")
    private byte[] audioDescription;

    @NotNull
    @Column(name = "element_num", nullable = false, columnDefinition = "TINYINT UNSIGNED DEFAULT 0")
    private Integer elementNum;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Element> elements = new ArrayList<>();

    @Builder
    public Part(Relic relic, @NotNull String name, @NotNull String textDescription, byte[] audioDescription,
                @NotNull Integer elementNum, List<Element> elements) {
        this.relic = relic;
        this.name = name;
        this.textDescription = textDescription;
        this.audioDescription = audioDescription;
        this.elementNum = elementNum;
        this.elements = elements;

    }

}
