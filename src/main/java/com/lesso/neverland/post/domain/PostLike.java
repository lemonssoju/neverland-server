package com.lesso.neverland.post.domain;

import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.lesso.neverland.common.constants.Constants.ACTIVE;
import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postLikeIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    public void setPost(Post post) {
        this.post = post;
        post.getPostLikes().add(this);
    }

    public void setUser(User user) {
        this.user = user;
        user.getPostLikes().add(this);
    }

    public void delete() {
        this.setStatus(INACTIVE);
        this.post.getPostLikes().remove(this);
        this.user.getPostLikes().remove(this);
    }

    public void add() {
        this.setStatus(ACTIVE);
        setPost(this.post);
        setUser(this.user);
    }

    @Builder
    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}
