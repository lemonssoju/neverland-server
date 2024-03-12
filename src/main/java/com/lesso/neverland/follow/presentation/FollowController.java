package com.lesso.neverland.follow.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.follow.application.FollowService;
import com.lesso.neverland.follow.dto.FollowRequest;
import com.lesso.neverland.follow.dto.FollowerListResponse;
import com.lesso.neverland.follow.dto.FollowingListResponse;
import com.lesso.neverland.profile.dto.MemberInviteListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.BaseResponseStatus.SUCCESS;
import static com.lesso.neverland.common.constants.RequestURI.follow;

@RestController
@RequiredArgsConstructor
@RequestMapping(follow)
public class FollowController {

    private final FollowService followService;

    // 팔로잉 목록 조회
    @GetMapping("/followingList")
    public BaseResponse<FollowingListResponse> getFollowingList(@RequestParam(required = false) String searchWord) {
        try {
            return new BaseResponse<>(followService.getFollowingList(searchWord));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 팔로워 목록 조회
    @GetMapping("/followerList")
    public BaseResponse<FollowerListResponse> getFollowerList(@RequestParam(required = false) String searchWord) {
        try {
            return new BaseResponse<>(followService.getFollowerList(searchWord));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 맞팔로우 목록 조회
    @GetMapping("/inviteList")
    public BaseResponse<MemberInviteListResponse> getMemberInviteList(@RequestParam(value = "groupIdx", required = false) Long groupIdx, @RequestParam(required = false) String searchWord) {
        try {
            if (groupIdx == null) { // 그룹 생성
                return new BaseResponse<>(followService.getMemberInviteList(searchWord));
            } else { // 그룹 수정
                return new BaseResponse<>(followService.getMemberInviteListEditView(groupIdx, searchWord));
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 팔로우/취소
    @PostMapping("")
    public BaseResponse<String> follow(@RequestBody FollowRequest followRequest) {
        try {
            followService.follow(followRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
