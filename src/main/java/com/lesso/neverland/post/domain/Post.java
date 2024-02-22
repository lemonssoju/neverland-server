package com.lesso.neverland.post.domain;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.common.enums.Source;
import com.lesso.neverland.team.domain.Team;
import com.lesso.neverland.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user; // 작성자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Source source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team")
    private Team team;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 50)
    private String subtitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Contents contentsType; // 콘텐츠 종류

    private String backgroundMusic; // 가수 - 제목
    private String backgroundMusicUrl; // 유튜브 링크 url

    @Column(nullable = false)
    private String postImage;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "post")
    private List<PostTag> postTags = new ArrayList<>(); // 취향 관련 태그

    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }
}
