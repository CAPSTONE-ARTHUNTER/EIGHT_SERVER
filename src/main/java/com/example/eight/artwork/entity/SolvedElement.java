package com.example.eight.artwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "solved_element")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolvedElement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id", nullable = false)
    private Element element;

    @Column(name = "is_solved", nullable = false)
    private boolean isSolved;

    // TODO: user 엔티티 생성 후 주석 해제
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @Column(name = "solved_at")
    private LocalDateTime solvedAt;

    // TODO: user 엔티티 생성 후 추가
    @Builder
    public SolvedElement(Element element, boolean isSolved, LocalDateTime solvedAt) {
        this.element = element;
        this.isSolved = isSolved;
        // this.user = user;
        this.solvedAt = solvedAt;
    }

}
