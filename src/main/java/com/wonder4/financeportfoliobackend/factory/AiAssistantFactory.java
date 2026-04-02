package com.wonder4.financeportfoliobackend.factory;

import com.wonder4.financeportfoliobackend.enums.AiModel;
import com.wonder4.financeportfoliobackend.service.AiAssistant;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("!test")
public class AiAssistantFactory {

    private final String baseUrl;
    private final ChatMemoryProvider chatMemoryProvider;
    private final ContentRetriever contentRetriever;

    private final Map<AiModel, AiAssistant> assistantCache = new ConcurrentHashMap<>();

    public AiAssistantFactory(
            @Value("${ai.ollama.base-url:http://localhost:11434}") String baseUrl,
            ChatMemoryProvider chatMemoryProvider,
            ContentRetriever contentRetriever) {
        this.baseUrl = baseUrl;
        this.chatMemoryProvider = chatMemoryProvider;
        this.contentRetriever = contentRetriever;
    }

    public AiAssistant getAiAssistant(AiModel requestedModel) {
        AiModel model = requestedModel != null ? requestedModel : AiModel.LLAMA_3_2;

        return assistantCache.computeIfAbsent(model, this::createAiAssistant);
    }

    private AiAssistant createAiAssistant(AiModel model) {
        ChatLanguageModel chatLanguageModel =
                OllamaChatModel.builder()
                        .baseUrl(baseUrl)
                        .modelName(model.getModelName())
                        .timeout(Duration.ofMinutes(5))
                        .logRequests(true)
                        .logResponses(true)
                        .build();

        return AiServices.builder(AiAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemoryProvider(chatMemoryProvider)
                .contentRetriever(contentRetriever)
                .build();
    }
}
