package com.lesso.neverland.post.domain.entity;

import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.group.domain.entity.Group;
import com.lesso.neverland.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupIdx")
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

    public void setUser(User user) {
        this.user = user;
        user.getPosts().add(this);
    }
}
