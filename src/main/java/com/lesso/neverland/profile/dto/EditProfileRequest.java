package com.lesso.neverland.profile.dto;

public record EditProfileRequest(String nickname,
                                 String profileMessage,
                                 String profileMusic,
                                 String profileMusicUrl) {}
