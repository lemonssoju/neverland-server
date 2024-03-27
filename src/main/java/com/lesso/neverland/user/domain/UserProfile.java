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
    private String profileMusic; // 가수 - 제목
    private String profileMusicUrl; // 유튜브 링크 url

    @Builder
    public UserProfile(String nickname) {
        this.nickname = nickname;
    }

    public void modifyNickname(String nickname) { this.nickname = nickname; }
    public void modifyProfileMessage(String profileMessage) { this.profileMessage = profileMessage; }
    public void modifyProfileMusic(String profileMusic) { this.profileMusic = profileMusic; }
    public void modifyProfileMusicUrl(String profileMusicUrl) { this.profileMusicUrl = profileMusicUrl; }
    public void modifyProfileImage(String profileImage) { this.profileImage = profileImage; }
}
