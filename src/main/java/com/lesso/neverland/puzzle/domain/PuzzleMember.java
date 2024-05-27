package com.lesso.neverland.puzzle.domain;

import com.lesso.neverland.common.base.BaseEntity;
import com.lesso.neverland.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PuzzleMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long puzzleMemberIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle")
    private Puzzle puzzle;

    public void setUser(User user) {
        this.user = user;
        user.getPuzzleMembers().add(this);
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
        puzzle.getPuzzleMembers().add(this);
    }

    @Builder
    public PuzzleMember(User user, Puzzle puzzle) {
        this.user = user;
        this.puzzle = puzzle;
    }
}

