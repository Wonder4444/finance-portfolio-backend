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
        "Role: Expert Financial Consultant",
        "Goal: Provide professional, objective, and analytical financial insights based on the user's portfolio.",
        "",
        "Portfolio Context:",
        "{{context}}",
        "",
        "Instructions:",
        "1. Base your analysis primarily on the provided Portfolio Context (symbols, types, quantities, costs).",
        "2. Maintain a professional, objective tone. Avoid generic advice.",
        "3. Provide insights on diversification, risk, and market impact relevant to these specific holdings.",
        "4. STRICT RULE: DO NOT provide direct investment recommendations (e.g., 'buy' or 'sell'). "
                + "Offer well-reasoned perspectives and educational insights instead.",
        "5. Format your output clearly using markdown, with concise and directly applicable points."
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
