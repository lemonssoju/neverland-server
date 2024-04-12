package com.lesso.neverland.gpt.application;

import com.lesso.neverland.gpt.dto.CompletePuzzleResponse;
import com.lesso.neverland.gpt.dto.GptRequest;
import com.lesso.neverland.gpt.dto.GptResponse;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GptService {

    private final OpenAiService openAiService;

    @Transactional
    public GptResponse completion(String inputText) {
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(GptRequest.from(inputText));
        return GptResponse.of(chatCompletion);
    }

    public String toText(List<String> puzzleTextList) {
        StringBuilder result = new StringBuilder();
        int index = 0;
        for (String puzzleText : puzzleTextList) {
            result.append("\"").append(puzzleText).append("\"");
            if (index < puzzleTextList.size() - 1) {
                result.append(", ");
            }
            index++;
        }
        return result.toString();
    }

    public CompletePuzzleResponse parseResponse(String response) {
        // 영어 부분 추출을 위한 정규 표현식 패턴 (괄호로 묶인 부분)
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(response);

        String englishPart = "";
        if (matcher.find()) {
            // 괄호 안의 영어 부분 추출
            englishPart = matcher.group(1);
        }

        // 영어 부분을 제외한 나머지 문자열 (한글 부분)
        String koreanPart = response.replace("(" + englishPart + ")", "").trim();

        // 추출된 한글 부분과 영어 부분으로 CompletePuzzleResponse 객체 생성
        return new CompletePuzzleResponse(englishPart, koreanPart);
    }
}
