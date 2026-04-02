package com.wonder4.financeportfoliobackend.service;

import com.wonder4.financeportfoliobackend.dto.AiChatRequest;
import com.wonder4.financeportfoliobackend.dto.AiChatResponse;
import com.wonder4.financeportfoliobackend.dto.UserHoldingWithInfoDTO;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@Service
@Profile("!test")
public class AiChatService {

    private final AiAssistant aiAssistant;
    private final UserHoldingService userHoldingService;
    private final ObjectMapper objectMapper;

    public AiChatService(
            AiAssistant aiAssistant,
            UserHoldingService userHoldingService,
            ObjectMapper objectMapper) {
        this.aiAssistant = aiAssistant;
        this.userHoldingService = userHoldingService;
        this.objectMapper = objectMapper;
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

        return objectMapper.writeValueAsString(holdings);
    }
}
