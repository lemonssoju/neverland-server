package com.lesso.neverland.puzzle.domain;

import com.lesso.neverland.common.base.BaseEntity;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.lesso.neverland.common.constants.Constants.ACTIVE;
import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Puzzle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long puzzleIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user; // 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team")
    private Team team;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String puzzleImage;

    @Column(nullable = false)
    private LocalDate puzzleDate; // 추억 날짜

    @Embedded
    private PuzzleLocation location; // 추억 장소

    @OneToMany(mappedBy = "puzzle")
    private List<PuzzlePiece> puzzlePieces = new ArrayList<>();

    @OneToMany(mappedBy = "puzzle")
    private List<PuzzleMember> puzzleMembers = new ArrayList<>(); // 해당 퍼즐에 참여한 멤버 목록

    @Builder
    public Puzzle(User user, Team team, String title, String content, LocalDate puzzleDate, PuzzleLocation location) {
        this.user = user;
        this.team = team;
        this.title = title;
        this.content = content;
        this.puzzleDate = puzzleDate;
        this.location = location;
        this.setStatus(ACTIVE);
    }

    public void setUser(User user) {
        this.user = user;
        user.getPuzzles().add(this);
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
    public void addPuzzleImage(String puzzleImage) { this.puzzleImage = puzzleImage; }
    public void modifyImage(String puzzleImage) {this.puzzleImage = puzzleImage;}
    public void editContent(String content) { this.content = content;}
}
