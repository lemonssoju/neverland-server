package com.lesso.neverland.profile.dto;

public record ProfileEditViewResponse(String profileImage,
                                      String nickname,
                                      String profileMessage,
                                      String profileMusic,
                                      String profileMusicUrl) {}
