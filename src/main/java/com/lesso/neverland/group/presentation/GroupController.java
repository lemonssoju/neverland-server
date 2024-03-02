package com.lesso.neverland.group.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.group.application.GroupService;
import com.lesso.neverland.group.dto.GroupListResponse;
import com.lesso.neverland.group.dto.GroupPostListResponse;
import com.lesso.neverland.group.dto.GroupPostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lesso.neverland.common.constants.RequestURI.group;

@RestController
@RequiredArgsConstructor
@RequestMapping(group)
public class GroupController {

    private final GroupService groupService;

    // 그룹 목록 조회
    @GetMapping("")
    public BaseResponse<GroupListResponse> getGroupList() {
        try {
            return new BaseResponse<>(groupService.getGroupList());
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 그룹 피드 목록 조회
    @GetMapping("/{groupIdx}/posts")
    public BaseResponse<GroupPostListResponse> getGroupPostList(@PathVariable Long groupIdx) {
        try {
            return new BaseResponse<>(groupService.getGroupPostList(groupIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 그룹 피드 상세 조회
    @GetMapping("/{groupIdx}/posts/{postIdx}")
    public BaseResponse<GroupPostResponse> getGroupPostList(@PathVariable Long groupIdx, @PathVariable Long postIdx) {
        try {
            return new BaseResponse<>(groupService.getGroupPost(groupIdx, postIdx));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
