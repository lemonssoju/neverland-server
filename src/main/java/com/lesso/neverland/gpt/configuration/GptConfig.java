package com.lesso.neverland.gpt.configuration;

import com.lesso.neverland.gpt.domain.GptProperties;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class GptConfig {

    private final GptProperties chatProperties;

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(chatProperties.getToken(), Duration.ofSeconds(60));
    }
}
