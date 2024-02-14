package com.lesso.neverland.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {

    @Column(nullable = false, length = 20)
    private String nickname;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false, length = 50)
    private String profileMessage;
}
