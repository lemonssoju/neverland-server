package com.lesso.neverland.search.dto;

import java.util.List;

public record PostSearchResponse(List<PostSearchDto> postSearchList) implements SearchResponse<PostSearchDto> {
    @Override
    public List<PostSearchDto> getSearchList() {
        return postSearchList;
    }
}
