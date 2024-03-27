package com.lesso.neverland.search.dto;

import java.util.List;

public record UserSearchResponse(List<UserSearchDto> userSearchList) implements SearchResponse<UserSearchDto> {
    @Override
    public List<UserSearchDto> getSearchList() {
        return userSearchList;
    }
}
