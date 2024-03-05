package com.lesso.neverland.group.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.group.application.GroupService;
import com.lesso.neverland.group.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.lesso.neverland.common.BaseResponseStatus.SUCCESS;
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

    // [관리자] 그룹 수정
    @PatchMapping("/{groupIdx}")
    public BaseResponse<String> modifyGroup(@PathVariable Long groupIdx, @RequestPart MultipartFile image, @RequestPart ModifyGroupRequest modifyGroupRequest) {
        try {
            groupService.modifyGroup(groupIdx, image, modifyGroupRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // [관리자] 그룹 삭제
    @PatchMapping("/{groupIdx}/delete")
    public BaseResponse<String> deleteGroup(@PathVariable Long groupIdx) {
        try {
            groupService.deleteGroup(groupIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 그룹 나가기
    @PatchMapping("/{groupIdx}/withdraw")
    public BaseResponse<String> withdrawGroup(@PathVariable Long groupIdx) {
        try {
            groupService.withdrawGroup(groupIdx);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 그룹 생성
    @PostMapping("")
    public BaseResponse<String> createGroup(@RequestPart MultipartFile image, @RequestPart CreateGroupRequest createGroupRequest) {
        try {
            groupService.createGroup(image, createGroupRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
