package com.lesso.neverland.search.dto;

import java.util.List;

public record SearchViewResponse(List<RecommendedSearchDto> recommendedSearchList,
                                 List<RecentSearchDto> recentSearchList) {}
