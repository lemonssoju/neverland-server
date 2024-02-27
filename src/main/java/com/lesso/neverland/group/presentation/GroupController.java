package com.lesso.neverland.group.presentation;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.common.BaseResponse;
import com.lesso.neverland.group.application.GroupService;
import com.lesso.neverland.group.dto.GroupListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
