package com.lesso.neverland.puzzle.application;

import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.common.image.ImageService;
import com.lesso.neverland.group.domain.Team;
import com.lesso.neverland.group.dto.GroupPuzzleDto;
import com.lesso.neverland.group.dto.GroupPuzzleListResponse;
import com.lesso.neverland.group.repository.GroupRepository;
import com.lesso.neverland.puzzle.domain.Puzzle;
import com.lesso.neverland.puzzle.domain.PuzzleMember;
import com.lesso.neverland.puzzle.dto.*;
import com.lesso.neverland.puzzle.repository.PuzzleMemberRepository;
import com.lesso.neverland.puzzle.repository.PuzzleRepository;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.lesso.neverland.common.base.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;
import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Service
@RequiredArgsConstructor
public class PuzzleService {
    private final UserService userService;
    private final PuzzleRepository puzzleRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ImageService imageService;
    private final PuzzleMemberRepository puzzleMemberRepository;

    // 퍼즐 목록 조회
    public BaseResponse<GroupPuzzleListResponse> getGroupPuzzleList(Long groupIdx) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));

        List<Puzzle> groupPuzzleList = puzzleRepository.findByTeamAndStatusEqualsOrderByCreatedDateDesc(group, ACTIVE);
        List<GroupPuzzleDto> groupPuzzleListDto = getGroupPuzzleDtos(groupPuzzleList);

        return new BaseResponse<>(new GroupPuzzleListResponse(groupPuzzleListDto));
    }

    @NotNull
    private static List<GroupPuzzleDto> getGroupPuzzleDtos(List<Puzzle> groupPuzzleList) {
        return groupPuzzleList.stream()
                .map(puzzle -> new GroupPuzzleDto(
                        puzzle.getPuzzleIdx(),
                        puzzle.getTitle(),
                        puzzle.getPuzzleImage(),
                        puzzle.getUser().getProfile().getNickname(),
                        puzzle.getCreatedDate().toString(),
                        puzzle.getLocation())).toList();
    }

    // 퍼즐 상세 조회
    public BaseResponse<PuzzleDetailResponse> getPuzzleDetail(Long groupIdx, Long puzzleIdx) {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));
        if (!puzzle.getTeam().equals(group)) throw new BaseException(NO_GROUP_PUZZLE);
        if (puzzle.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_PUZZLE);

        PuzzleDetailResponse puzzleDetail = new PuzzleDetailResponse(puzzle.getLocation(), puzzle.getPuzzleImage(),
                puzzle.getPuzzleDate().toString(), puzzle.getUser().getProfile().getNickname(), puzzle.getTitle(), puzzle.getContent(),
                getMemberImageList(puzzle), puzzle.getPuzzleMembers().size(), getPuzzlePieceList(puzzle));
        return new BaseResponse<>(puzzleDetail);
    }

    // 퍼즐피스 목록 조회
    private List<PuzzlePieceDto> getPuzzlePieceList(Puzzle puzzle) {
        return puzzle.getPuzzlePieces().stream()
                .map(puzzlePiece -> new PuzzlePieceDto(
                        puzzlePiece.getUser().getProfile().getNickname(),
                        puzzlePiece.getUser().getProfile().getProfileImage(),
                        puzzlePiece.getContent()
                )).toList();
    }

    // puzzleMember 3명의 프로필 이미지 조회
    private List<String> getMemberImageList(Puzzle puzzle) {
        return puzzle.getPuzzleMembers().stream()
                .map(puzzleMember -> puzzleMember.getUser().getProfile().getProfileImage())
                .limit(3)
                .toList();
    }

    // 퍼즐 생성
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> createPuzzle(Long groupIdx, MultipartFile image, CreatePuzzleRequest createPuzzleRequest) throws IOException {
        Team group = groupRepository.findById(groupIdx).orElseThrow(() -> new BaseException(INVALID_GROUP_IDX));
        User writer = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        String imagePath = imageService.uploadImage("group", image);
        LocalDate puzzleDate = convertToLocalDate(createPuzzleRequest.puzzleDate());

        Puzzle newPuzzle = createPuzzle(createPuzzleRequest, group, writer, imagePath, puzzleDate);
        addPuzzleMember(createPuzzleRequest, newPuzzle);

        return new BaseResponse<>(SUCCESS);
    }

    // Puzzle 생성 시 PuzzleMember entity 함께 생성
    private void addPuzzleMember(CreatePuzzleRequest createPuzzleRequest, Puzzle newPuzzle) {
        for (Long userIdx : createPuzzleRequest.puzzlerList()) {
            User member = userRepository.findById(userIdx).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
            PuzzleMember puzzleMember = PuzzleMember.builder()
                    .user(member)
                    .puzzle(newPuzzle).build();
            puzzleMember.setUser(member);
            puzzleMember.setPuzzle(newPuzzle);

            puzzleMemberRepository.save(puzzleMember);
        }
    }

    // String -> LocalDate
    private static LocalDate convertToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.KOREA);

        return LocalDate.parse(date, formatter);
    }

    // puzzle entity 생성
    private Puzzle createPuzzle(CreatePuzzleRequest createPuzzleRequest, Team group, User writer, String imagePath, LocalDate puzzleDate) {
        Puzzle puzzle = Puzzle.builder()
                .user(writer)
                .team(group)
                .title(createPuzzleRequest.title())
                .content(createPuzzleRequest.content())
                .puzzleImage(imagePath)
                .puzzleDate(puzzleDate)
                .location(createPuzzleRequest.location()).build();
        puzzleRepository.save(puzzle);

        return puzzle;
    }

    // [작성자] 피드 수정 화면 조회
    public BaseResponse<PuzzleEditViewResponse> getPuzzleEditView(Long puzzleIdx) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));
        validateWriter(user, puzzle);

        return new BaseResponse<>(new PuzzleEditViewResponse(puzzle.getTitle(), puzzle.getPuzzleImage(), puzzle.getContent()));
    }

    // [작성자] 피드 삭제
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<String> deletePuzzle(Long puzzleIdx) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));
        validateWriter(user, puzzle);

        puzzle.delete();
        puzzleRepository.save(puzzle);
        return new BaseResponse<>(SUCCESS);
    }

    // 작성한 글 목록 조회
    public BaseResponse<MyPuzzleListResponse> getMyPuzzleList() {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));

        List<Puzzle> myPuzzles = puzzleRepository.findByUserAndStatusEquals(user, ACTIVE);
        List<MyPuzzleDto> myPuzzleDtoList = convertToMyPuzzleDtoList(myPuzzles);
        return new BaseResponse<>(new MyPuzzleListResponse(myPuzzleDtoList));
    }

    // MyPuzzleDto로 가공
    private List<MyPuzzleDto> convertToMyPuzzleDtoList(List<Puzzle> puzzleList) {
        return puzzleList.stream()
                .map(puzzle -> new MyPuzzleDto(
                        puzzle.getPuzzleImage(),
                        puzzle.getTitle(),
                        puzzle.getCreatedDate())).toList();
    }

    // 작성자 validation
    private static void validateWriter(User user, Puzzle puzzle) {
        if (!puzzle.getUser().equals(user)) throw new BaseException(NO_PUZZLE_WRITER);
        if (puzzle.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_PUZZLE);
    }
}
