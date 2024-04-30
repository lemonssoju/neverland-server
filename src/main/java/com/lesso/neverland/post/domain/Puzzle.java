package com.lesso.neverland.post.domain;

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

    @Column(nullable = false)
    private String puzzleImage;

    @Column(nullable = false)
    private LocalDate puzzleDate; // 추억 날짜

    @Column(nullable = false)
    private String location; // 추억 장소

    private String backgroundMusic; // 가수 - 제목
    private String backgroundMusicUrl; // 유튜브 링크 url

    @Builder
    public Puzzle(User user, Team team, String title, String content, String puzzleImage, LocalDate puzzleDate, String location, String backgroundMusic, String backgroundMusicUrl) {
        this.user = user;
        this.team = team;
        this.title = title;
        this.content = content;
        this.puzzleImage = puzzleImage;
        this.puzzleDate = puzzleDate;
        this.location = location;
        this.backgroundMusic = backgroundMusic;
        this.backgroundMusicUrl = backgroundMusicUrl;
    }

    public void setUser(User user) {
        this.user = user;
        user.getPuzzles().add(this);
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
}