package com.lesso.neverland.post.domain;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.common.enums.Source;
import com.lesso.neverland.group.domain.Group;
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
    @JoinColumn(name = "group")
    private Group group;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 50)
    private String subtitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Contents contentsType;

    private String backgroundMusic;

    @Column(nullable = false)
    private String postImage;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "post")
    private List<PostTag> postTags = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }
}
