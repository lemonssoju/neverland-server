package com.lesso.neverland.openAi;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gpt3")
public class GptTestController {

    @PostMapping("/question")
    public void sendRequest() {
        OpenAiService service = new OpenAiService("token"); // 추후 토큰 대체
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("This is test prompt!")
                .model("ada")
                .echo(true)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
    }
}