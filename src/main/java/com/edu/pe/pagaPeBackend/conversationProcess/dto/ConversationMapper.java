package com.edu.pe.pagaPeBackend.conversationProcess.dto;



import com.edu.pe.pagaPeBackend.conversationProcess.model.Conversation;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;

import java.util.stream.Collectors;

public class ConversationMapper {

    public static ConversationResponse toResponse(Conversation entity) {
        return ConversationResponse.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .clientId(entity.getClient().getId())
                .clientName(entity.getClient().getUserFirstName())
               .serviceName(entity.getReminder().getServiceFilter().getNombreServicio())
                .reminderId(entity.getReminder().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .finishAt(entity.getFinishAt())
                .messages(         entity.getMessages()
                        .stream()
                        .map(MessageMapper::toResponse)
                        .collect(Collectors.toList())
                )
                .sentimentLabel(entity.getSentimentLabel())
                .sentimentScore(entity.getSentimentScore())
                .statusFinish(entity.getStatusFinished())
                .build();
    }

    public static Conversation toEntity(ConversationRequest dto, Client client, Reminder reminder) {
        if (dto == null) return null;

        Conversation entity = new Conversation();
        entity.setClient(client);
        entity.setReminder(reminder);
        entity.setStatus(dto.getStatus());
        return entity;
    }
}
