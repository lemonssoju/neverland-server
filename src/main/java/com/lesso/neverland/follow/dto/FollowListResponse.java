package com.lesso.neverland.follow.dto;

import java.util.List;

public record FollowListResponse(List<FollowDto> followingList,
                                 List<FollowDto> followedList) {}
