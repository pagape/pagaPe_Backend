package com.edu.pe.pagaPeBackend.conversationProcess.dto;



import com.edu.pe.pagaPeBackend.conversationProcess.model.Conversation;
import com.edu.pe.pagaPeBackend.conversationProcess.model.Message;

import java.time.LocalDateTime;

public class MessageMapper {

    public static MessageResponse toResponse(Message entity) {
        return MessageResponse.builder()
                .id(entity.getId())
                .messageContent(entity.getMessageContent())
                .messageType(entity.getMessageType())
                .sender(entity.getSender())
                .timestamp(entity.getTimestamp())
                .conversationId(entity.getConversation().getId())
                .build();
    }

    public static Message toEntity(MessageRequest dto, Conversation conversation) {
        return Message.builder()
                .messageContent(dto.getMessageContent())
                .messageType(dto.getMessageType())
                .sender(dto.getSender())
                .timestamp(LocalDateTime.now()) // Usar tiempo actual
                .conversation(conversation)
                .build();
    }
}
