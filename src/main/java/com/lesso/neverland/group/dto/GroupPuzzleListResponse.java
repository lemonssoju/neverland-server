package com.lesso.neverland.group.dto;

import java.util.List;

public record GroupPuzzleListResponse(String groupName,
                                      List<GroupPuzzleDto> groupPostList) {}
