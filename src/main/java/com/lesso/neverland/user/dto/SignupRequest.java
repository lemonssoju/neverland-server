package com.lesso.neverland.user.dto;

import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.domain.UserProfile;

public record SignupRequest(String loginId,
                            String nickname,
                            String password,
                            String passwordCheck) { // TODO: 관심 콘텐츠, 취향 추가하기
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
