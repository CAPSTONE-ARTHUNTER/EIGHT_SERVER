package com.example.eight.artwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "solved_part")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolvedPart implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column(name = "is_solved", nullable = false)
    private boolean isSolved;

    @Column(name = "solved_element_num", nullable = false)
    private int solvedElementNum;

    // TODO: user 엔티티 생성 후 주석 해제
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @Column(name = "solved_at")
    private LocalDateTime solvedAt;

    // TODO: user 엔티티 생성 후 추가
    @Builder
    public SolvedPart(Part part, boolean isSolved, int solvedElementNum, LocalDateTime solvedAt) {
        this.part = part;
        this.isSolved = isSolved;
        this.solvedElementNum = solvedElementNum;
        // this.user = user;
        this.solvedAt = solvedAt;
    }

}
