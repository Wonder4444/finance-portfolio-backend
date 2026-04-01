package com.wonder4.financeportfoliobackend.config;

import com.wonder4.financeportfoliobackend.service.AiAssistant;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/** Configuration for AiChat module. */
@Configuration
public class AiConfig {

    @Value("${ai.ollama.base-url}")
    private String baseUrl;

    @Value("${ai.ollama.model-name}")
    private String modelName;

    @Bean
    public ChatLanguageModel ollamaChatModel() {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .timeout(Duration.ofMinutes(5))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        // Retain the last 20 messages for each user chat session
        return memoryId -> MessageWindowChatMemory.withMaxMessages(20);
    }

    @Bean
    public AiAssistant aiAssistant(
            ChatLanguageModel chatLanguageModel, ChatMemoryProvider chatMemoryProvider) {
        return AiServices.builder(AiAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }
}
