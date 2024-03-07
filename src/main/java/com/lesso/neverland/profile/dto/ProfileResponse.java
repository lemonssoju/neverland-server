package com.lesso.neverland.profile.dto;

import java.util.List;

public record ProfileResponse(Long userIdx,
                              String nickname,
                              String profileImage,
                              String profileMessage,
                              String profileMusic,
                              String profileMusicUrl,
                              Integer followingNumber,
                              Integer followerNumber,
                              boolean isMyProfile, // 유저 본인의 프로필인지 여부
                              List<ThumbnailDto> thumbnailList) {}
