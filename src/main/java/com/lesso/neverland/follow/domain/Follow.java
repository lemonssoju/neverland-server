package com.lesso.neverland.follow.domain;

import com.lesso.neverland.common.BaseEntity;
import com.lesso.neverland.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user")
    private User followingUser; // 팔로우하는 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_user")
    private User followedUser; // 팔로잉 당하는 유저
}
