package com.lesso.neverland.group.presentation;

import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.group.application.GroupService;
import com.lesso.neverland.group.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.lesso.neverland.common.constants.RequestURI.group;

@RestController
@RequiredArgsConstructor
@RequestMapping(group)
public class GroupController {

    private final GroupService groupService;

    // 그룹 목록 조회
    @GetMapping("")
    public BaseResponse<GroupListResponse> getGroupList() {
        return groupService.getGroupList();
    }

    // 그룹 피드 목록 조회
    @GetMapping("/{groupIdx}/posts")
    public BaseResponse<GroupPostListResponse> getGroupPostList(@PathVariable Long groupIdx) {
        return groupService.getGroupPostList(groupIdx);
    }

    // 그룹 피드 상세 조회
    @GetMapping("/{groupIdx}/posts/{postIdx}")
    public BaseResponse<GroupPostResponse> getGroupPostList(@PathVariable Long groupIdx, @PathVariable Long postIdx) {
        return groupService.getGroupPost(groupIdx, postIdx);
    }

    // [관리자] 그룹 수정 화면 조회
    @GetMapping("/{groupIdx}/editView")
    public BaseResponse<GroupEditViewResponse> getGroupEditView(@PathVariable Long groupIdx) {
        return groupService.getGroupEditView(groupIdx);
    }

    // [관리자] 그룹 수정
    @PatchMapping("/{groupIdx}")
    public BaseResponse<String> editGroup(@PathVariable Long groupIdx, @RequestPart MultipartFile image, @RequestPart EditGroupRequest editGroupRequest) {
        try {
            return groupService.editGroup(groupIdx, image, editGroupRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // [관리자] 그룹 삭제
    @PatchMapping("/{groupIdx}/delete")
    public BaseResponse<String> deleteGroup(@PathVariable Long groupIdx) {
        return groupService.deleteGroup(groupIdx);
    }

    // 그룹 나가기
    @PatchMapping("/{groupIdx}/withdraw")
    public BaseResponse<String> withdrawGroup(@PathVariable Long groupIdx) {
        return groupService.withdrawGroup(groupIdx);
    }

    // 그룹 생성
    @PostMapping("")
    public BaseResponse<String> createGroup(@RequestPart MultipartFile image, @RequestPart CreateGroupRequest createGroupRequest) {
        try {
            return groupService.createGroup(image, createGroupRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 그룹 피드 등록
    @PostMapping("/{groupIdx}")
    public BaseResponse<String> createGroupPost(@PathVariable("groupIdx") Long groupIdx, @RequestPart MultipartFile image, @RequestPart GroupPostRequest groupPostRequest) {
        try {
            return groupService.createGroupPost(groupIdx, image, groupPostRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
