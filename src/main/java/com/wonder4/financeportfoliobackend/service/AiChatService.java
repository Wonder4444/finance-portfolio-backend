package com.wonder4.financeportfoliobackend.service;

import com.wonder4.financeportfoliobackend.dto.AiChatRequest;
import com.wonder4.financeportfoliobackend.dto.AiChatResponse;
import com.wonder4.financeportfoliobackend.dto.UserHoldingWithInfoDTO;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AiChatService {

    private final AiAssistant aiAssistant;
    private final UserHoldingService userHoldingService;

    public AiChatService(AiAssistant aiAssistant, UserHoldingService userHoldingService) {
        this.aiAssistant = aiAssistant;
        this.userHoldingService = userHoldingService;
    }

    public AiChatResponse chat(AiChatRequest request) {
        String chatId = request.getChatId();
        String reply;

        if (!StringUtils.hasText(chatId)) {
            // New conversation
            chatId = UUID.randomUUID().toString();
            String holdingsContext = buildHoldingsContext(request.getUserId());
            reply =
                    aiAssistant.startNewChatWithContext(
                            chatId, holdingsContext, request.getMessage());
        } else {
            // Continue existing conversation
            reply = aiAssistant.continueChat(chatId, request.getMessage());
        }

        return AiChatResponse.builder().chatId(chatId).reply(reply).build();
    }

    private String buildHoldingsContext(Long userId) {
        if (userId == null) {
            return "The user currently has no holdings or the user is unknown.";
        }

        List<UserHoldingWithInfoDTO> holdings = userHoldingService.listByUserIdWithInfo(userId);

        if (holdings == null || holdings.isEmpty()) {
            return "The user currently has no holdings in their portfolio.";
        }

        return holdings.stream()
                .map(
                        h ->
                                String.format(
                                        "- Asset ID: %d (Symbol: %s, Name: %s), Quantity: %s, Average Cost: %s, Current Price: %s",
                                        h.getAssetId(),
                                        h.getSymbol() != null ? h.getSymbol() : "Unknown",
                                        h.getFullName() != null ? h.getFullName() : "Unknown",
                                        h.getQuantity() != null ? h.getQuantity().toString() : "0",
                                        h.getAvgCost() != null ? h.getAvgCost().toString() : "0",
                                        h.getCurrentPrice() != null
                                                ? h.getCurrentPrice().toString()
                                                : "0"))
                .collect(Collectors.joining("\n"));
    }
}
