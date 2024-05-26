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
        List<Team> groupList = groupRepository.findByAdminAndStatusEquals(user, ACTIVE);
        List<GroupListDto> groupListDto = groupList.stream()
                .map(group -> {
                    String startYear = group.getStartDate().format(DateTimeFormatter.ofPattern("yyyy"));
                    return new GroupListDto(group.getTeamIdx(), group.getTeamImage(), startYear, group.getName(),
                            group.getUserTeams().size(), group.getAdmin().getProfile().getNickname(), calculateRecentUpdate(group));
                }).toList();
        return new BaseResponse<>(new GroupListResponse(groupListDto));
    }

    private String calculateRecentUpdate(Team group) {
        Puzzle recentPuzzle = puzzleRepository.findTopByTeamAndStatusEqualsOrderByCreatedDateDesc(group, ACTIVE);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate puzzleCreatedDate = recentPuzzle.getCreatedDate();
        long daysBetween = ChronoUnit.DAYS.between(puzzleCreatedDate, today);

        return daysBetween + "일 전";
    }

    // 그룹 프로필 조회
    public BaseResponse<GroupProfileResponse> getGroupProfile(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        List<String> memberImageList = group.getUserTeams().stream()
                .map(userTeam -> userTeam.getUser().getProfile().getProfileImage())
                .limit(3)
                .toList();

        Integer puzzleCount = puzzleRepository.countByTeam(group);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        long dayCount = ChronoUnit.DAYS.between(group.getStartDate(), today);

        GroupProfileResponse profile = new GroupProfileResponse(group.getName(), group.getStartDate().getYear(), memberImageList,
                group.getUserTeams().size(), puzzleCount, dayCount);
        return new BaseResponse<>(profile);
    }


    //TODO: 퍼즐 도메인 하위로 이동
//    // 그룹 상세 조회
//    public BaseResponse<GroupProfileResponse> getGroupDetail() {
//
//    }
//
//    // 그룹 퍼즐 목록 조회
//    public BaseResponse<GroupPuzzleListResponse> getGroupPuzzleList(Long groupIdx) {
//        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
//        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
//        List<Puzzle> groupPuzzleList = puzzleRepository.findByTeamAndStatusEqualsOrderByCreatedDateDesc(group, ACTIVE);
//
//        List<GroupPuzzleDto> groupPuzzleListDto = groupPuzzleList.stream()
//                .map(groupPuzzle -> new GroupPuzzleDto(
//                        groupPuzzle.getTitle(),
//                        groupPuzzle.getPuzzleImage(),
//                        groupPuzzle.getUser().getProfile().getNickname(),
//                        groupPuzzle.getCreatedDate().toString(),
//                        groupPuzzle.getLocation())).collect(Collectors.toList());
//        return new BaseResponse<>(new GroupPuzzleListResponse(group.getName(), groupPuzzleListDto));
//    }
//
//    // TODO: 퍼즐 도메인 하위로 이동
//    // 그룹 퍼즐 상세 조회
//    public BaseResponse<GroupPuzzleResponse> getGroupPuzzle(Long groupIdx, Long puzzleIdx) {
//        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
//        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));
//        if (!puzzle.getTeam().equals(group)) throw new BaseException(NO_GROUP_PUZZLE);
//
//        return new BaseResponse<>(new GroupPuzzleResponse(puzzle.getTitle(), puzzle.getContent(), puzzle.getCreatedDate(),
//                puzzle.getUser().getProfile().getNickname(), puzzle.getBackgroundMusic(), puzzle.getBackgroundMusicUrl(),
//                puzzle.getPuzzleImage()));
//    }

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
        userTeamRepository.save(userTeam);

        return new BaseResponse<>(SUCCESS);
    }

    // 그룹 생성
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> createGroup(MultipartFile image, CreateGroupRequest createGroupRequest) throws IOException {
        User admin = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        // upload image
        String imagePath = imageService.uploadImage("group", image);

        YearMonth startDate = YearMonth.parse(createGroupRequest.startDate(), DateTimeFormatter.ofPattern("yyyy-MM"));

        // create random joinCode
        Integer joinCode;
        do {
            joinCode = new Random().nextInt(10000); // 100000(포함)부터 999999(포함) 사이의 랜덤한 숫자 생성
        } while (groupRepository.existsByJoinCode(joinCode));

        Team group = new Team(admin, createGroupRequest.name(), imagePath, startDate, joinCode);
        groupRepository.save(group);

        UserTeam newUserTeam = new UserTeam(admin, group);
        newUserTeam.setUser(admin);
        newUserTeam.setTeam(group);
        userTeamRepository.save(newUserTeam);

//        for (Long memberIdx : createGroupRequest.memberList()) {
//            User member = userRepository.findById(memberIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
//            UserTeam userTeam = new UserTeam(member, group);
//            userTeam.setUser(member);
//            userTeam.setTeam(group);
//            userTeamRepository.save(userTeam);
//        }
        return new BaseResponse<>(SUCCESS);
    }

    // [관리자] 그룹 초대하기
    public BaseResponse<GroupInviteResponse> inviteGroup(Long groupIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        validateAdmin(user, group);

        return new BaseResponse<>(new GroupInviteResponse(group.getJoinCode()));
    }


    // TODO: 퍼즐 도메인 하위로 이동
//    // 그룹 피드 등록
//    public BaseResponse<String> createGroupPuzzle(Long groupIdx, MultipartFile image, GroupPuzzleRequest groupPuzzleRequest) throws IOException {
//        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
//        User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
//
//        // upload image
//        String imagePath = imageService.uploadImage("group", image);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
//        formatter = formatter.withLocale(Locale.KOREA);
//        LocalDate puzzleDate = LocalDate.parse(groupPuzzleRequest.puzzleDate(), formatter);
//
//        Puzzle puzzle = new Puzzle(writer, group, groupPuzzleRequest.title(), groupPuzzleRequest.content(),
//                imagePath, puzzleDate, groupPuzzleRequest.location(), groupPuzzleRequest.backgroundMusic(), groupPuzzleRequest.backgroundMusicUrl());
//        puzzleRepository.save(puzzle);
//        return new BaseResponse<>(SUCCESS);
//    }

    private void validateAdmin(User user, Team group) {
        if (!group.getAdmin().equals(user)) throw new BaseException(NO_GROUP_ADMIN);
    }

    private UserTeam validateMember(User user, Team group) {
        if (user.equals(group.getAdmin())) throw new BaseException(GROUP_ADMIN);

        return Optional.ofNullable(userTeamRepository.findByUserAndTeam(user, group))
                .orElseThrow(() -> new BaseException(NO_GROUP_MEMBER));
    }
}
