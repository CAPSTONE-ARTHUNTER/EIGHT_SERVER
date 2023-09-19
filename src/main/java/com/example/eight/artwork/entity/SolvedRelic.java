package com.example.eight.artwork.entity;

import com.example.eight.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "solved_relic")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolvedRelic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relic_id", nullable = false)
    private Relic relic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "solved_at")
    private LocalDateTime solvedAt;

    @Builder
    public SolvedRelic(Relic relic, User user, LocalDateTime solvedAt) {
        this.relic = relic;
        this.user = user;
        this.solvedAt = solvedAt;
    }

    public void updateSolvedAt(LocalDateTime solvedAt){
        this.solvedAt = solvedAt;
    }

}
