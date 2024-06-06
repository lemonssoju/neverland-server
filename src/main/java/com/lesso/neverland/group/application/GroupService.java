package com.lesso.neverland.group.application;

import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.common.image.ImageService;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.group.dto.*;
import com.lesso.neverland.group.repository.GroupRepository;
import com.lesso.neverland.puzzle.domain.Puzzle;
import com.lesso.neverland.puzzle.repository.PuzzleRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.domain.UserTeam;
import com.lesso.neverland.user.repository.UserRepository;
import com.lesso.neverland.user.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

import static com.lesso.neverland.common.base.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PuzzleRepository puzzleRepository;
    private final ImageService imageService;
    private final UserTeamRepository userTeamRepository;

    // 그룹 목록 조회
    public BaseResponse<GroupListResponse> getGroupList() {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        List<Team> groupList_admin = groupRepository.findByAdminAndStatusEquals(user, ACTIVE);
        List<Team> groupList_member = user.getUserTeams().stream()
                .filter(userTeam -> "active".equals(userTeam.getStatus()))
                .map(UserTeam::getTeam).toList();
        List<Team> combinedGroupList = Stream.concat(groupList_admin.stream(), groupList_member.stream()).distinct().toList();

        List<GroupListDto> groupListDto = combinedGroupList.stream()
                .map(group -> new GroupListDto(
                        group.getTeamIdx(),
                        group.getTeamImage(),
                        group.getStartDate().format(DateTimeFormatter.ofPattern("yyyy")),
                        group.getName(),
                        group.getUserTeams().size(),
                        group.getAdmin().getProfile().getNickname(),
                        calculateRecentUpdate(group)))
                .sorted(Comparator.comparing((GroupListDto groupDto) -> calculateDaysSinceRecentUpdate(groupDto.groupIdx()))
                        .thenComparing(groupDto -> groupRepository.findById(groupDto.groupIdx())
                                .map(Team::getCreatedDate).orElse(LocalDate.MIN), Comparator.reverseOrder())).toList();
        return new BaseResponse<>(new GroupListResponse(groupListDto));
    }

    // GroupListDto 정렬을 위한 최근 업로드 일자 계산
    private long calculateDaysSinceRecentUpdate(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        Puzzle recentPuzzle = puzzleRepository.findTopByTeamAndStatusEqualsOrderByCreatedDateDesc(group, ACTIVE);
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        if (recentPuzzle == null) return ChronoUnit.DAYS.between(group.getCreatedDate(), today); // 그룹 생성일과 오늘 사이 일수
        LocalDate puzzleCreatedDate = recentPuzzle.getCreatedDate();
        return ChronoUnit.DAYS.between(puzzleCreatedDate, today);
    }

    private String calculateRecentUpdate(Team group) {
        Puzzle recentPuzzle = puzzleRepository.findTopByTeamAndStatusEqualsOrderByCreatedDateDesc(group, ACTIVE);
        if (recentPuzzle == null) return "";
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate puzzleCreatedDate = recentPuzzle.getCreatedDate();
        long daysBetween = ChronoUnit.DAYS.between(puzzleCreatedDate, today);

        return daysBetween + "일 전";
    }

    // 그룹 프로필 조회
    public BaseResponse<GroupProfileResponse> getGroupProfile(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
//        List<String> memberImageList = group.getUserTeams().stream()
//                .map(userTeam -> userTeam.getUser().getProfile().getProfileImage())
//                .limit(3).toList();

        Integer puzzleCount = puzzleRepository.countByTeam(group);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        YearMonth startYearMonth = group.getStartDate();
        LocalDate startLocalDate = startYearMonth.atDay(1);

        long dayCount = ChronoUnit.DAYS.between(startLocalDate, today);

        GroupProfileResponse profile = new GroupProfileResponse(group.getName(), group.getAdmin().getProfile().getNickname(), group.getStartDate().getYear(), getMemberImageList(group),
                group.getUserTeams().size(), puzzleCount, dayCount);
        return new BaseResponse<>(profile);
    }

    private List<String> getMemberImageList(Team group) {
        List<String> imageList = new ArrayList<>();
        imageList.add(group.getAdmin().getProfile().getProfileImage());

        List<String> memberImages = group.getUserTeams().stream()
                .filter(userTeam -> "active".equals(userTeam.getStatus()))
                .filter(userTeam -> !userTeam.getUser().equals(group.getAdmin()))
                .map(userTeam -> userTeam.getUser().getProfile().getProfileImage())
                .limit(2)
                .toList();
        imageList.addAll(memberImages);
        return imageList;
    }

    // [관리자] 그룹 수정 화면 조회
    public BaseResponse<GroupEditViewResponse> getGroupEditView(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        validateAdmin(user, group);

        return new BaseResponse<>(new GroupEditViewResponse(group.getName(), group.getTeamImage(), group.getStartDate().toString()));
    }

    // [관리자] 그룹 수정
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> editGroup(Long groupIdx, MultipartFile image, EditGroupRequest editGroupRequest) throws IOException {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        validateAdmin(user, group);

        if (editGroupRequest.name() != null) {
            if (!editGroupRequest.name().equals("") && !editGroupRequest.name().equals(" "))
                group.modifyName(editGroupRequest.name());
            else throw new BaseException(BLANK_GROUP_NAME);
        } if (editGroupRequest.startDate() != null) {
            if (!editGroupRequest.startDate().equals("") && !editGroupRequest.startDate().equals(" ")) {
                YearMonth startDate = YearMonth.parse(editGroupRequest.startDate(), DateTimeFormatter.ofPattern("yyyy-MM"));
                group.modifyStartDate(startDate);
            } else throw new BaseException(BLANK_GROUP_START_DATE);
        } if (editGroupRequest.memberList() != null) {
            List<UserTeam> originalMemberList = group.getUserTeams();
            userTeamRepository.deleteAll(originalMemberList);

            for(Long userIdx : editGroupRequest.memberList()) {
                User member = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
                UserTeam userTeam = UserTeam.builder()
                        .user(member)
                        .team(group).build();
                userTeam.setUser(member);
                userTeam.setTeam(group);
                userTeamRepository.save(userTeam);
            }
        }

        if (image != null) {//TODO: 이미지 삭제 및 업로드 설정 후 동작 확인하기
            // delete previous image
            imageService.deleteImage(group.getTeamImage());

            // upload new image
            String imagePath = imageService.uploadImage("group", image);
            group.modifyImage(imagePath);
        } else throw new BaseException(NULL_GROUP_IMAGE);
        groupRepository.save(group);
        return new BaseResponse<>(SUCCESS);
    }

    // [관리자] 그룹 삭제
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> deleteGroup(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        validateAdmin(user, group);

        List<Puzzle> groupPuzzleList = puzzleRepository.findByTeamAndStatusEquals(group, ACTIVE);
        List<UserTeam> userTeamsSaveList = new ArrayList<>();

        for (UserTeam userTeam : group.getUserTeams()) {
            userTeam.delete();
            userTeamsSaveList.add(userTeam);
        }
        puzzleRepository.saveAll(groupPuzzleList);
        userTeamRepository.saveAll(userTeamsSaveList);

        group.delete();
        groupRepository.save(group);
        return new BaseResponse<>(SUCCESS);
    }

    // [멤버] 그룹 나가기
    public BaseResponse<String> withdrawGroup(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        UserTeam userTeam = validateMember(user, group);
        userTeam.delete();
        //userTeam.removeTeam(group);
        //userTeam.removeUser(user);
        userTeamRepository.save(userTeam);

        return new BaseResponse<>(SUCCESS);
    }

    // 그룹 생성
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<CreateGroupResponse> createGroup(MultipartFile image, CreateGroupRequest createGroupRequest) throws IOException {
        User admin = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        // upload image
        String imagePath = imageService.uploadImage("group", image);

        YearMonth startDate = YearMonth.parse(createGroupRequest.startDate(), DateTimeFormatter.ofPattern("yyyy-MM"));

        // create random joinCode
        Integer joinCode;
        do {
            joinCode = new Random().nextInt(10000);
        } while (groupRepository.existsByJoinCode(joinCode));

        Team group = new Team(admin, createGroupRequest.name(), imagePath, startDate, joinCode);
        groupRepository.save(group);

        UserTeam newUserTeam = new UserTeam(admin, group);
        newUserTeam.setUser(admin);
        newUserTeam.setTeam(group);
        userTeamRepository.save(newUserTeam);

        CreateGroupResponse createGroupResponse = new CreateGroupResponse(joinCode);
        return new BaseResponse<>(createGroupResponse);
    }

    // [관리자] 그룹 초대하기
    public BaseResponse<GroupInviteResponse> inviteGroup(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        validateAdmin(user, group);

        return new BaseResponse<>(new GroupInviteResponse(group.getJoinCode()));
    }

    // [멤버] 그룹 입장하기
    public BaseResponse<GroupJoinResponse> joinGroup(JoinGroupRequest joinGroupRequest) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        Team group = groupRepository.findByJoinCode(joinGroupRequest.joinCode()).orElseThrow(() -> new BaseException(NO_MATCH_GROUP));
        if (user.equals(group.getAdmin())) throw new BaseException(GROUP_ADMIN);

        UserTeam newUserTeam = new UserTeam(user, group);
        newUserTeam.setUser(user);
        newUserTeam.setTeam(group);
        userTeamRepository.save(newUserTeam);

        return new BaseResponse<>(new GroupJoinResponse(group.getTeamIdx()));
    }

    private void validateAdmin(User user, Team group) {
        if (!group.getAdmin().equals(user)) throw new BaseException(NO_GROUP_ADMIN);
    }

    private UserTeam validateMember(User user, Team group) {
        if (user.equals(group.getAdmin())) throw new BaseException(GROUP_ADMIN);

        return Optional.ofNullable(userTeamRepository.findByUserAndTeam(user, group))
                .orElseThrow(() -> new BaseException(NO_GROUP_MEMBER));
    }
}
