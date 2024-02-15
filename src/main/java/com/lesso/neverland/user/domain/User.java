package com.lesso.neverland.user.domain;

import com.lesso.neverland.comment.domain.Comment;
import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.guestMemo.domain.GuestMemo;
import com.lesso.neverland.history.domain.SearchHistory;
import com.lesso.neverland.interest.domain.Interest;
import com.lesso.neverland.post.domain.Post;
import com.lesso.neverland.post.domain.PostLike;
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
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Embedded
    private UserProfile profile;

    @OneToMany(mappedBy = "user")
    private List<Interest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Thumbnail> thumbnails = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserGroup> userGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<GuestMemo> memos = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SearchHistory> searchHistories = new ArrayList<>();
}
