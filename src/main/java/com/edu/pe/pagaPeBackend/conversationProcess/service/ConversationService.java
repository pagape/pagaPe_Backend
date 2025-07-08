package com.edu.pe.pagaPeBackend.conversationProcess.service;



import com.edu.pe.pagaPeBackend.conversationProcess.dto.*;
import com.edu.pe.pagaPeBackend.conversationProcess.model.Conversation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConversationService {

    ConversationResponse createConversation(ConversationRequest request);

    ConversationResponse getActiveConversationByPhoneNumber(String phoneNumber);

    List<ConversationResponse> getAllConversations();



    MessageResponse addMessageToConversation(Long conversationId, MessageRequest messageRequest);


    void closeConversation(Long conversationId,  CloseConversationRequest request);

    ConversationMetricsResponse getSentimentMetrics(LocalDateTime startDate, LocalDateTime endDate);


    ConversationMetricsResponse getStatusFinishMetrics(LocalDateTime startDate, LocalDateTime endDate);






}
