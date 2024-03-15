package com.lesso.neverland.search.dto;

import java.util.List;

public interface SearchResponse<T> {
    List<T> getSearchList();
}
