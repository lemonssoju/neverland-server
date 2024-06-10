package com.lesso.neverland.group.presentation;

import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.group.application.GroupService;
import com.lesso.neverland.group.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.lesso.neverland.common.base.BaseResponseStatus.IMAGE_UPLOAD_FAIL;
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

    // 그룹 프로필 조회
    @GetMapping("/{groupIdx}/profile")
    public BaseResponse<GroupProfileResponse> getGroupProfile(@PathVariable Long groupIdx) {
        return groupService.getGroupProfile(groupIdx);
    }

    // [관리자] 그룹 수정 화면 조회
    @GetMapping("/{groupIdx}/editView")
    public BaseResponse<GroupEditViewResponse> getGroupEditView(@PathVariable Long groupIdx) {
        return groupService.getGroupEditView(groupIdx);
    }

    // [관리자] 그룹 수정
    @PatchMapping("/{groupIdx}/edit")
    public BaseResponse<String> editGroup(@PathVariable Long groupIdx, @RequestPart(required = false) MultipartFile image, @RequestPart EditGroupRequest editGroupRequest) {
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

    // [멤버] 그룹 나가기
    @PatchMapping("/{groupIdx}/withdraw")
    public BaseResponse<String> withdrawGroup(@PathVariable Long groupIdx) {
        return groupService.withdrawGroup(groupIdx);
    }

    // 그룹 생성
    @PostMapping("/create")
    public BaseResponse<CreateGroupResponse> createGroup(@RequestPart MultipartFile image, @RequestPart CreateGroupRequest createGroupRequest) {
        try {
            return groupService.createGroup(image, createGroupRequest);
        } catch (IOException e) {
            throw new BaseException(IMAGE_UPLOAD_FAIL);
        }
    }

    // [관리자] 그룹 초대하기
    @PostMapping("/{groupIdx}/invite")
    public BaseResponse<GroupInviteResponse> inviteGroup(@PathVariable Long groupIdx) {
        return groupService.inviteGroup(groupIdx);
    }

    // [멤버] 그룹 입장하기
    @PostMapping("/join")
    public BaseResponse<GroupJoinResponse> joinGroup(@RequestBody JoinGroupRequest joinGroupRequest) {
        return groupService.joinGroup(joinGroupRequest);
    }
}
