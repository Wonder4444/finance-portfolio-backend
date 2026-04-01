package com.wonder4.financeportfoliobackend.controller;

import com.wonder4.financeportfoliobackend.dto.AiChatRequest;
import com.wonder4.financeportfoliobackend.dto.AiChatResponse;
import com.wonder4.financeportfoliobackend.service.AiChatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI Chat", description = "Endpoints for AI chat interactions")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @PostMapping("/chat")
    @Operation(
            summary = "Chat with AI",
            description = "Send a message to the AI and receive a response")
    public ResponseEntity<AiChatResponse> chat(@RequestBody AiChatRequest request) {
        AiChatResponse response = aiChatService.chat(request);
        return ResponseEntity.ok(response);
    }
}
