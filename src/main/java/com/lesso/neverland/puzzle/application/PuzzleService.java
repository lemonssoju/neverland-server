package com.lesso.neverland.puzzle.application;

import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.puzzle.domain.Puzzle;
import com.lesso.neverland.puzzle.dto.*;
import com.lesso.neverland.puzzle.repository.PuzzleRepository;
import com.lesso.neverland.user.application.AuthService;
import com.lesso.neverland.user.application.UserService;
import com.lesso.neverland.user.domain.User;
import com.lesso.neverland.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lesso.neverland.common.base.BaseResponseStatus.*;
import static com.lesso.neverland.common.constants.Constants.ACTIVE;
import static com.lesso.neverland.common.constants.Constants.INACTIVE;

@Service
@RequiredArgsConstructor
public class PuzzleService {

    private final AuthService authService;
    private final UserService userService;
    private final PuzzleRepository puzzleRepository;
    private final UserRepository userRepository;

    // 퍼즐 상세 조회
    public BaseResponse<PuzzleResponse> getPuzzle(Long puzzleIdx) {
        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));
        if (puzzle.getStatus().equals(INACTIVE)) throw new BaseException(ALREADY_DELETED_PUZZLE);

        return new BaseResponse<>(new PuzzleResponse(puzzle.getTitle(), puzzle.getContent(), puzzle.getCreatedDate(),
                puzzle.getUser().getUserIdx(), puzzle.getUser().getProfile().getNickname(), puzzle.getBackgroundMusic(), puzzle.getBackgroundMusicUrl(),
                puzzle.getPuzzleImage()));
    }

    // [작성자] 피드 수정 화면 조회
    public BaseResponse<PuzzleEditViewResponse> getPuzzleEditView(Long puzzleIdx) {
        User user = userRepository.findById(userService.getUserIdxWithValidation()).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        Puzzle puzzle = puzzleRepository.findById(puzzleIdx).orElseThrow(() -> new BaseException(INVALID_PUZZLE_IDX));
        validateWriter(user, puzzle);

        return new BaseResponse<>(new PuzzleEditViewResponse(puzzle.getTitle(), puzzle.getBackgroundMusic(),
                puzzle.getBackgroundMusicUrl(), puzzle.getPuzzleImage(), puzzle.getContent()));
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
