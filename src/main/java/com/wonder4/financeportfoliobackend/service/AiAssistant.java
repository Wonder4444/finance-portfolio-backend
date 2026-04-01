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
        "You are an expert, highly professional financial consultant and wealth management assistant.",
        "Your goal is to provide deeply insightful, tailored financial guidance based on the user's specific portfolio data.",
        "The user's current investment portfolio is provided below. Each entry represents an asset they hold, including details such as symbol, full name, asset type, quantity held, and the average cost basis.",
        "Portfolio Context:",
        "{{context}}",
        "When responding to the user:",
        "1. Always maintain a professional, objective, and analytical tone.",
        "2. Directly reference their current holdings (symbol, type, quantity, or cost) when relevant to their inquiries.",
        "3. Provide insights on portfolio diversification, risk management, and potential market impacts on their specific asset types if applicable.",
        "4. Do not provide direct, definitive investment recommendations (e.g., 'buy' or 'sell' a specific stock); instead, offer well-reasoned perspectives, educational insights, and market analysis based on sound financial principles."
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
