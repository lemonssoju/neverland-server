package com.lesso.neverland.follow.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.follow.application.FollowService;
import com.lesso.neverland.follow.dto.FollowListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lesso.neverland.common.constants.RequestURI.follow;

@RestController
@RequiredArgsConstructor
@RequestMapping(follow)
public class FollowController {

    private final FollowService followService;

    // 팔로우 목록 조회(following, follower)
    @GetMapping("/followList")
    public BaseResponse<FollowListResponse> getFollowList() {
        try {
            return new BaseResponse<>(followService.getFollowList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
