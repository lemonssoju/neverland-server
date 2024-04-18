package com.lesso.neverland.user.dto;

import java.util.List;

public record SignupRequest(String loginId,
                            String nickname,
                            String password,
                            String passwordCheck,
                            List<ContentsPreference> contentsPreferences) {

    public record ContentsPreference(String contents,
                                     String preference){}
}
