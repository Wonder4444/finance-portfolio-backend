package com.wonder4.financeportfoliobackend.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Configuration for AiChat module. */
@Configuration
@Profile("!test")
public class AiConfig {

    @Value("${ai.ollama.base-url}")
    private String baseUrl;

    @Bean
    public WebSearchEngine tavilyWebSearchEngine(@Value("${ai.tavily.api-key}") String apiKey) {
        return TavilyWebSearchEngine.builder().apiKey(apiKey).build();
    }

    @Bean
    public ContentRetriever webSearchContentRetriever(WebSearchEngine webSearchEngine) {
        return WebSearchContentRetriever.builder()
                .webSearchEngine(webSearchEngine)
                .maxResults(3)
                .build();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider(
            @Value("${ai.chat.max-messages:20}") int maxMessages) {
        // Retain the last maxMessages for each user chat session
        return memoryId -> MessageWindowChatMemory.withMaxMessages(maxMessages);
    }
}
