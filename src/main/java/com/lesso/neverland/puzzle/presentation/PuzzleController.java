package com.lesso.neverland.puzzle.presentation;

import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.puzzle.application.PuzzleService;
import com.lesso.neverland.puzzle.dto.PuzzleEditViewResponse;
import com.lesso.neverland.puzzle.dto.MyPuzzleListResponse;
import com.lesso.neverland.puzzle.dto.PuzzleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.constants.RequestURI.puzzle;

@RestController
@RequiredArgsConstructor
@RequestMapping(puzzle)
public class PuzzleController {
    private final PuzzleService puzzleService;

    // 피드 상세 조회
    @GetMapping("/{puzzleIdx}")
    public BaseResponse<PuzzleResponse> getPuzzle(@PathVariable Long postIdx) {
        return puzzleService.getPuzzle(postIdx);
    }

    // [작성자] 피드 수정 화면 조회
    @GetMapping("/{puzzleIdx}/editView")
    public BaseResponse<PuzzleEditViewResponse> getPuzzleEditView(@PathVariable Long puzzleIdx) {
        return puzzleService.getPuzzleEditView(puzzleIdx);
    }

    // [작성자] 피드 삭제
    @PatchMapping("/{puzzleIdx}/delete")
    public BaseResponse<String> deletePuzzle(@PathVariable Long puzzleIdx) {
        return puzzleService.deletePuzzle(puzzleIdx);
    }

    // [유저] 작성한 글 목록 조회
    @GetMapping("/myPuzzles")
    public BaseResponse<MyPuzzleListResponse> getMyPuzzleList() {
        return puzzleService.getMyPuzzleList();
    }
}