package com.lesso.neverland.gpt.dto;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public record GptRequest(String model, List<ChatMessage> messages, Double temperature) {

    private static String prefixMessage =
                "이 글들은 한 추억에 대해 여러명이 작성한 글이야. 형태를 보면 큰따옴표와 쉼표로 한 명이 작성한 글을 분리한거야. 즉, 큰따옴표 안에 들어간 글이 한 명이 작성한 글이라는 뜻이야."
                        + "이 글들을 잘 정리해서 100자 정도의 하나의 글로 만들어서 한글과 영어로 각각 작성해줘. 근데 잘 알아야 할 게 이 글들을 작성한 사람들이 다 같이 함께한 일이기 때문에 글을 쓸 때 주체를 우리라고 해야해. 우리 이 때 이랬는데 이런 느낌으로 말이야. 모든 내용이 적절하게 다 들어갔으면 좋겠어. 그냥 이어붙이는게 아니라 적절히 섞어서 새로운 글로 정리해달라는거야. "
                        //+ "그리고 한글로 작성한 글이랑, 영어로 작성한 글 두 개를 다 반환해줘."
                        + "반환할 때 형식은 한글로 작성한 글을 괄호로 묶고 영어로 작성한 글은 또 따로 괄호로 묶어서 보내줘."
                        + "(한글로 작성한 글 100자), (영어로 작성한 글 100자) 이 형식으로 보내달라는 말이야. 각 글을 괄호로 묶고 쉼표로 구분해줘."
                        + "다른 부가적인 말 하지 말고 딱 정리한 글만 반환해줘. 그리고 말투를 일기 쓴 것처럼 ~했어. 느낌으로 작성해줘. 문장이 좀 자연스럽게 이어지게 해줄래? 전체가 하나의 일기처럼 느껴지게 써줘. 그리고 글 안에 따옴표나 줄바꿈 문자 넣지 마."
                        + "전체적으로 구어체 느낌으로 ~다. 말고 ~했었어. 말투로 작성해줘. 말이 좀 이어지게 해서 일기를 작성한다고 생각해."
                    //+ "한글로 작성된 글이랑 영어로 작성된 글을 반드시 줄바꿈 문자로 구분해서 보내줘."
                    ;

    public static ChatCompletionRequest from(String inputText) {
        return ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(new ChatMessage("user", prefixMessage + inputText)))
                .temperature(0.7)
                .build();
    }
}
