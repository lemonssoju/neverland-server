package com.lesso.neverland.group.application;

import com.lesso.neverland.common.BaseException;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.group.dto.GroupListDto;
import com.lesso.neverland.group.dto.GroupListResponse;
import com.lesso.neverland.group.repository.GroupRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lesso.neverland.common.BaseResponseStatus.DATABASE_ERROR;
import static com.lesso.neverland.common.BaseResponseStatus.INVALID_USER_IDX;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    // 그룹 목록 조회
    public GroupListResponse getGroupList() throws BaseException {
        try {
            User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            List<Team> groupList = groupRepository.findByUser(user);
            List<GroupListDto> groupListDto = groupList.stream()
                    .map(group -> new GroupListDto(
                            group.getTeamIdx(),
                            group.getTeamImage(),
                            group.getName(),
                            group.getSubName())).collect(Collectors.toList());
            return new GroupListResponse(groupListDto);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
