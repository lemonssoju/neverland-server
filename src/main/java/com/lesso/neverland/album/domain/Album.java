package com.lesso.neverland.album.domain;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.common.base.BaseEntity;
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
    private String albumImage;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "album")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Album(Puzzle puzzle, String content) {
        this.puzzle = puzzle;
        this.content = content;
    }
}
