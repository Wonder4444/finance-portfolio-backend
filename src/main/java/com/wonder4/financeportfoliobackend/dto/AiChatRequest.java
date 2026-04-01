package com.wonder4.financeportfoliobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiChatRequest {

    private Long userId;

    // Optional. If null/empty, starts a new conversation
    private String chatId;

    private String message;
}
