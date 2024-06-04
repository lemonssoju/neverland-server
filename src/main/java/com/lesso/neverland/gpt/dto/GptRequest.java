package com.lesso.neverland.gpt.dto;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public record GptRequest(String model, List<ChatMessage> messages, Double temperature) {

    private static String prefixMessage =
                        "이 글들은 한 추억에 대해 여러 명이 작성한 글이야. 큰따옴표와 쉼표로 각자 작성한 글을 분리해서 보내. 즉, 큰따옴표 안에 들어간 글이 한 명이 작성한 글이라는 뜻이야."
                        + "이 글들을 잘 정리해서 100자 정도의 하나의 글로 한글로 작성해줘. 그리고 정리된 글에서 주체를 우리라고 표현해줘. 모든 내용이 빠지지 않고 적절하게 다 들어갔으면 좋겠어. 그냥 이어 붙이는게 아니라 적절히 섞어서 새로운 글로 정리해 달라는거야."
                        + "전체적으로 구어체 느낌으로 ~다. 말고 ~했었어. 말투로 작성해줘. 말이 좀 이어지게 해서 일기를 작성한다고 생각해."
                                + "반환할 때는 네가 정리한 글(한글로 작성), 정리한 글을 영어로 변환한 글을 JSON 형식으로 보내줘."
                                + "다른 설명 없이 JSON만 반환해주고, JSON의 키는 prompt(String), description(String)으로 구성되어 있어. description에는 한글로 정리한 글을 담아주고, prompt에는 description을 영어로 변환한 글을 담아줘."
                                + "다른 부가적인 설명하지 말고 딱 JSON만 반환해줘.";

    public static ChatCompletionRequest from(String inputText) {
        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(new ChatMessage("user", prefixMessage + inputText)))
                .temperature(0.7)
                .build();
    }
}
