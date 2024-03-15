package com.lesso.neverland.search.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.search.application.SearchService;
import com.lesso.neverland.search.dto.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.constants.RequestURI.search;

@RestController
@RequiredArgsConstructor
@RequestMapping(search)
public class SearchController {

    private final SearchService searchService;

    // 검색(제목, 내용, 태그)
    // 닉네임으로 유저 검색(맨 앞에 @ 붙이기)
    @PostMapping("")
    public BaseResponse<SearchResponse<?>> searchKeyword(@RequestParam String keyword) {
        try {
            if (keyword.startsWith("@")) { // user 검색
                String nickname = keyword.substring(1);
                return new BaseResponse<>(searchService.searchUser(nickname));
            } else if (keyword.startsWith("#")) { // 태그 검색
                String tag = keyword.substring(1);
                return new BaseResponse<>(searchService.searchTag(tag));
            } else { // post(title, content) 검색
                return new BaseResponse<>(searchService.searchPost(keyword));
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
