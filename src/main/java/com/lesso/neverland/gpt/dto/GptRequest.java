package com.lesso.neverland.gpt.dto;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public record GptRequest(String model, List<ChatMessage> messages, Double temperature) {

    private static String prefixMessage =
            "이 글들은 한 추억에 대해 여러명이 작성한 글이야. 형태를 보면 큰따옴표와 쉼표로 한 명이 작성한 글을 분리한거야. 즉, 큰따옴표 안에 들어간 글이 한 명이 작성한 글이라는 뜻이야."
                    +"이 글들을 잘 정리해서 100자 정도의 하나의 글로 만들어줘. 모든 내용이 적절하게 다 들어갔으면 좋겠어." +
                    "다른 부가적인 말 하지 말고 딱 내가 부탁한 100자의 정리한 글만 반환해줘.";

    public static ChatCompletionRequest from(String text) {
        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(new ChatMessage("user", prefixMessage + text)))
                .temperature(0.7)
                .build();
    }
}
