package com.lesso.neverland.album.domain;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.common.base.BaseEntity;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.puzzle.domain.Puzzle;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puzzle")
    private Puzzle puzzle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team")
    private Team team;

    private String albumImage;

    @Column(nullable = false, length = 1000)
    private String content;

    @OneToMany(mappedBy = "album")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Album(Puzzle puzzle, String albumImage, Team team, String content) {
        this.puzzle = puzzle;
        this.albumImage = albumImage;
        this.team = team;
        this.content = content;
    }

    public void saveAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }
}
