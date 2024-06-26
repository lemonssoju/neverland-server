package com.lesso.neverland.gpt.presentation;

import com.lesso.neverland.common.base.BaseResponse;
import com.lesso.neverland.gpt.application.GptService;
import com.lesso.neverland.gpt.dto.GptResponseDto;
import com.lesso.neverland.puzzle.dto.CompletePuzzleRequest;
import com.lesso.neverland.gpt.dto.GptResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/gpt3")
public class GptController {

    private final GptService gptService;

    @PostMapping("/completePuzzle")
    public BaseResponse<GptResponseDto> completePuzzle(@RequestBody CompletePuzzleRequest completePuzzleRequest) {
        GptResponse response = gptService.completion(gptService.toText(completePuzzleRequest.puzzleTextList()));
        return new BaseResponse<>(gptService.parseResponse(response.messages().get(0).message()));
    }
}