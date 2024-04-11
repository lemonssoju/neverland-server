package com.lesso.neverland.gpt.application;

import com.lesso.neverland.gpt.dto.GptRequest;
import com.lesso.neverland.gpt.dto.GptResponse;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GptService {

    private final OpenAiService openAiService;

    @Transactional
    public GptResponse completion(String text) {
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(GptRequest.from(text));
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
}
