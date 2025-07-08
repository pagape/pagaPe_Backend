package com.edu.pe.pagaPeBackend.conversationProcess.controller;


import com.edu.pe.pagaPeBackend.conversationProcess.dto.*;
import com.edu.pe.pagaPeBackend.conversationProcess.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    /**
     * Crear conversación
     * POST /conversations
     */
    @PostMapping
    public ResponseEntity<ConversationResponse> createConversation(
            @RequestBody ConversationRequest request) {

        ConversationResponse response = conversationService.createConversation(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Buscar conversación activa por teléfono
     * GET /conversations/active?phoneNumber=51987654321
     */
    @GetMapping("/active")
    public ResponseEntity<ConversationResponse> getActiveConversation(
            @RequestParam String phoneNumber) {

        ConversationResponse response =
                conversationService.getActiveConversationByPhoneNumber(phoneNumber);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getAllConversations() {
        List<ConversationResponse> list = conversationService.getAllConversations();
        return ResponseEntity.ok(list);
    }


    /**
     * Añadir mensaje a una conversación
     * POST /conversations/{conversationId}/messages
     */
    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<MessageResponse> addMessageToConversation(
            @PathVariable Long conversationId,
            @RequestBody MessageRequest messageRequest) {

        MessageResponse response = conversationService
                .addMessageToConversation(conversationId, messageRequest);

        return ResponseEntity.ok(response);
    }

    /**
     * Cerrar conversación
     * PUT /conversations/{conversationId}/close
     */
    @PutMapping("/{conversationId}/close")
    public ResponseEntity<Void> closeConversation(
            @PathVariable Long conversationId,
            @RequestBody CloseConversationRequest request) {

        conversationService.closeConversation(conversationId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/metrics/status-finish")
    public ResponseEntity<ConversationMetricsResponse> getStatusFinishMetrics() {
        return ResponseEntity.ok(conversationService.getStatusFinishMetrics());
    }

    @GetMapping("/metrics/sentiment")
    public ResponseEntity<ConversationMetricsResponse> getSentimentMetrics() {
        return ResponseEntity.ok(conversationService.getSentimentMetrics());
    }


}
