package com.lesso.neverland.user.dto;

import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.domain.UserProfile;

import java.util.List;

public record SignupRequest(String loginId,
                            String nickname,
                            String password,
                            String passwordCheck,
                            List<ContentsPreference> contentsPreferences) {

    public record ContentsPreference(String contents,
                                     String preference){}

    public User toUser(String encodedPassword) {

        UserProfile profile = UserProfile.builder()
                .nickname(nickname)
                .build();
        return User.builder()
                .loginId(this.loginId)
                .password(encodedPassword)
                .profile(profile)
                .build();
    }
}
