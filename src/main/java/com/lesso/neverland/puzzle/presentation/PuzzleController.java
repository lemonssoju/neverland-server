package com.lesso.neverland.puzzle.presentation;

import com.lesso.neverland.common.base.BaseException;
import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.group.dto.GroupPuzzleListResponse;
import com.lesso.neverland.puzzle.application.PuzzleService;
import com.lesso.neverland.puzzle.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.lesso.neverland.common.base.BaseResponseStatus.IMAGE_UPLOAD_FAIL;
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
    public BaseResponse<PuzzleDetailResponse> getPuzzleDetail(@PathVariable("groupIdx") Long groupIdx, @PathVariable("puzzleIdx") Long puzzleIdx) {
        return puzzleService.getPuzzleDetail(groupIdx, puzzleIdx);
    }

    // 퍼즐 생성
    @PostMapping("")
    public BaseResponse<String> createPuzzle(@PathVariable Long groupIdx, @RequestPart MultipartFile image, @RequestPart CreatePuzzleRequest createPuzzleRequest) {
        try {
            return puzzleService.createPuzzle(groupIdx, image, createPuzzleRequest);
        } catch (IOException e) {
            throw new BaseException(IMAGE_UPLOAD_FAIL);
        }
    }

    // [작성자] 피드 수정 화면 조회
    @GetMapping("/{puzzleIdx}/editView")
    public BaseResponse<PuzzleEditViewResponse> getPuzzleEditView(@PathVariable Long puzzleIdx) {
        return puzzleService.getPuzzleEditView(puzzleIdx);
    }

    // [작성자] 퍼즐 삭제
    @PatchMapping("/{puzzleIdx}/delete")
    public BaseResponse<String> deletePuzzle(@PathVariable("groupIdx") Long groupIdx, @PathVariable("puzzleIdx") Long puzzleIdx) {
        return puzzleService.deletePuzzle(groupIdx, puzzleIdx);
    }

    // 퍼즐러 목록 조회
    @GetMapping("/puzzlerList")
    public BaseResponse<PuzzlerListResponse> getPuzzlerList(@PathVariable Long groupIdx) {
        return puzzleService.getPuzzlerList(groupIdx);
    }

}
