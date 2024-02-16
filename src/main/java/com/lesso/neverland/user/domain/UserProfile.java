package com.lesso.neverland.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @Column(nullable = false, length = 20)
    private String nickname;
    private String profileImage;

    @Column(length = 50)
    private String profileMessage;

    @Builder
    public UserProfile(String nickname) {
        this.nickname = nickname;
    }
}
