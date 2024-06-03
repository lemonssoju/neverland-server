package com.lesso.neverland.group.dto;

import java.util.List;

public record GroupProfileResponse(String groupName,
                                   String admin,
                                   Integer startYear,
                                   List<String> memberImageList, // 3개만 조회
                                   Integer memberCount, // 멤버수
                                   Integer puzzleCount,
                                   long dayCount) {}
