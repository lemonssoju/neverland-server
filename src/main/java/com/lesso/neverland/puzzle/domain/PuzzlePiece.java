package com.lesso.neverland.puzzle.domain;

import com.lesso.neverland.common.base.BaseEntity;
import com.lesso.neverland.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PuzzlePiece extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long puzzlePieceIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle")
    private Puzzle puzzle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user; // 작성자

    @Column(nullable = false)
    private String content;

    @Builder
    public PuzzlePiece(Puzzle puzzle, User user, String content) {
        this.puzzle = puzzle;
        this.user = user;
        this.content = content;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        puzzle.getPuzzlePieces().add(this);
    }

    public void setUser(User user) {
        this.user = user;
        user.getPuzzlePieces().add(this);
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
}
