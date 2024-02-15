package com.lesso.neverland.common.openAi;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/gpt3")
public class GptTestController {

    private final OpenAiService openAiService;

    @PostMapping("/request")
    public ResponseEntity<List<CompletionChoice>> sendRequest() {
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("This is test prompt!")
                .model("ada")
                .echo(false)
                .build();
//        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
        return ResponseEntity.ok(openAiService.createCompletion(completionRequest).getChoices());
    }
}