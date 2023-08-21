package com.example.eight.artwork.entity;

import com.example.eight.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "solved_at")
    private LocalDateTime solvedAt;

    @Builder
    public SolvedElement(Element element, boolean isSolved, User user, LocalDateTime solvedAt) {
        this.element = element;
        this.isSolved = isSolved;
        this.user = user;
        this.solvedAt = solvedAt;
    }

}
