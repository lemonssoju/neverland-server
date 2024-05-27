package com.lesso.neverland.puzzle.presentation;

import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.group.dto.GroupPuzzleListResponse;
import com.lesso.neverland.puzzle.application.PuzzleService;
import com.lesso.neverland.puzzle.dto.PuzzleEditViewResponse;
import com.lesso.neverland.puzzle.dto.MyPuzzleListResponse;
import com.lesso.neverland.puzzle.dto.PuzzleDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.lesso.neverland.common.constants.RequestURI.puzzle;

@RestController
@RequiredArgsConstructor
@RequestMapping(puzzle)
public class PuzzleController {
    private final PuzzleService puzzleService;

    // 퍼즐 목록 조회
    @GetMapping("")
    public BaseResponse<GroupPuzzleListResponse> getGroupPuzzles(@PathVariable Long groupIdx) {
        return puzzleService.getGroupPuzzleList(groupIdx);
    }

    // 퍼즐 상세 조회
    @GetMapping("/{puzzleIdx}")
    public BaseResponse<PuzzleDetailResponse> getPuzzleDetail(@PathVariable Long groupIdx, @PathVariable Long puzzleIdx) {
        return puzzleService.getPuzzleDetail(groupIdx, puzzleIdx);
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
