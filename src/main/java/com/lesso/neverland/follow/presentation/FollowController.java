package com.lesso.neverland.follow.presentation;

import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.follow.application.FollowService;
import com.lesso.neverland.follow.dto.FollowRequest;
import com.lesso.neverland.follow.dto.FollowerListResponse;
import com.lesso.neverland.follow.dto.FollowingListResponse;
import com.lesso.neverland.profile.dto.MemberInviteListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.constants.RequestURI.follow;

@RestController
@RequiredArgsConstructor
@RequestMapping(follow)
public class FollowController {

    private final FollowService followService;

    // 팔로잉 목록 조회
    @GetMapping("/followingList")
    public BaseResponse<FollowingListResponse> getFollowingList(@RequestParam(required = false) String searchWord) {
        return followService.getFollowingList(searchWord);
    }

    // 팔로워 목록 조회
    @GetMapping("/followerList")
    public BaseResponse<FollowerListResponse> getFollowerList(@RequestParam(required = false) String searchWord) {
        return followService.getFollowerList(searchWord);
    }

    // 맞팔로우 목록 조회
    @GetMapping("/inviteList")
    public BaseResponse<MemberInviteListResponse> getMemberInviteList(@RequestParam(value = "groupIdx", required = false) Long groupIdx, @RequestParam(required = false) String searchWord) {
        if (groupIdx == null) { // 그룹 생성
            return followService.getMemberInviteList(searchWord);
        } else { // 그룹 수정
            return followService.getMemberInviteListEditView(groupIdx, searchWord);
        }
    }

    // 팔로우/취소
    @PostMapping("")
    public BaseResponse<String> follow(@RequestBody FollowRequest followRequest) {
        return followService.follow(followRequest);
    }
}
