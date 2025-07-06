package com.edu.pe.pagaPeBackend.conversationProcess.service;



import com.edu.pe.pagaPeBackend.conversationProcess.dto.*;
import com.edu.pe.pagaPeBackend.conversationProcess.model.Conversation;

import java.util.List;
import java.util.Optional;

public interface ConversationService {

    ConversationResponse createConversation(ConversationRequest request);

    ConversationResponse getActiveConversationByPhoneNumber(String phoneNumber);


    MessageResponse addMessageToConversation(Long conversationId, MessageRequest messageRequest);


    void closeConversation(Long conversationId,  CloseConversationRequest request);




}
