package com.lesso.neverland.group.presentation;

import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.group.application.GroupService;
import com.lesso.neverland.group.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        return new BaseResponse<>(groupService.getGroupList());
    }

    // 그룹 피드 목록 조회
    @GetMapping("/{groupIdx}/posts")
    public BaseResponse<GroupPostListResponse> getGroupPostList(@PathVariable Long groupIdx) {
        return new BaseResponse<>(groupService.getGroupPostList(groupIdx));
    }

    // 그룹 피드 상세 조회
    @GetMapping("/{groupIdx}/posts/{postIdx}")
    public BaseResponse<GroupPostResponse> getGroupPostList(@PathVariable Long groupIdx, @PathVariable Long postIdx) {
        return new BaseResponse<>(groupService.getGroupPost(groupIdx, postIdx));
    }

    // [관리자] 그룹 수정 화면 조회
    @GetMapping("/{groupIdx}/editView")
    public BaseResponse<GroupEditViewResponse> getGroupEditView(@PathVariable Long groupIdx) {
        return new BaseResponse<>(groupService.getGroupEditView(groupIdx));
    }

    // [관리자] 그룹 수정
    @PatchMapping("/{groupIdx}")
    public BaseResponse<String> editGroup(@PathVariable Long groupIdx, @RequestPart MultipartFile image, @RequestPart EditGroupRequest editGroupRequest) {
        try {
            groupService.editGroup(groupIdx, image, editGroupRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // [관리자] 그룹 삭제
    @PatchMapping("/{groupIdx}/delete")
    public BaseResponse<String> deleteGroup(@PathVariable Long groupIdx) {
        groupService.deleteGroup(groupIdx);
        return new BaseResponse<>(SUCCESS);
    }

    // 그룹 나가기
    @PatchMapping("/{groupIdx}/withdraw")
    public BaseResponse<String> withdrawGroup(@PathVariable Long groupIdx) {
        groupService.withdrawGroup(groupIdx);
        return new BaseResponse<>(SUCCESS);
    }

    // 그룹 생성
    @PostMapping("")
    public BaseResponse<String> createGroup(@RequestPart MultipartFile image, @RequestPart CreateGroupRequest createGroupRequest) {
        try {
            groupService.createGroup(image, createGroupRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 그룹 피드 등록
    @PostMapping("/{groupIdx}")
    public BaseResponse<String> createGroupPost(@PathVariable("groupIdx") Long groupIdx, @RequestPart MultipartFile image, @RequestPart GroupPostRequest groupPostRequest) {
        try {
            groupService.createGroupPost(groupIdx, image, groupPostRequest);
            return new BaseResponse<>(SUCCESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
