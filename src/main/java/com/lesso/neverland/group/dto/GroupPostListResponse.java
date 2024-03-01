package com.lesso.neverland.group.dto;

import java.util.List;

public record GroupPostListResponse(String groupName,
                                    List<GroupPostDto> groupPostList) {}
