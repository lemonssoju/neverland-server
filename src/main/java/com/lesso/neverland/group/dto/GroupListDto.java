package com.lesso.neverland.group.dto;

public record GroupListDto(Long groupIdx,
                           String groupImage,
                           String startYear,
                           String name,
                           Integer memberCount,
                           String admin,
                           String recentUpdate) {}
