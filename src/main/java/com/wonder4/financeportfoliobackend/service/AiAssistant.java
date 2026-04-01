package com.wonder4.financeportfoliobackend.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/** Interface definition for LangChain4j AiService. */
public interface AiAssistant {

    /**
     * Start a new chat with system context injected.
     *
     * @param chatId Unique chat identifier for tracking conversation history
     * @param context The user's holdings context as a string
     * @param message The user's message
     * @return Agent's reply
     */
    @SystemMessage({
        "You are a helpful and professional financial assistant.",
        "The user currently holds the following assets in their portfolio:",
        "{{context}}",
        "Please provide insightful answers based on this context and general financial knowledge."
    })
    String startNewChatWithContext(
            @MemoryId String chatId, @V("context") String context, @UserMessage String message);

    /**
     * Continue an existing chat without re-injecting the system context (it's already in memory).
     *
     * @param chatId The existing chat identifier
     * @param message The user's message
     * @return Agent's reply
     */
    String continueChat(@MemoryId String chatId, @UserMessage String message);
}
