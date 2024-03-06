package com.lesso.neverland.profile.dto;

public record ProfileModifyViewResponse(String profileImage,
                                        String nickname,
                                        String profileMessage,
                                        String profileMusic,
                                        String profileMusicUrl) {}
