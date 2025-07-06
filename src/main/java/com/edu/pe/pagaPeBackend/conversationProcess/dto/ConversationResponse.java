package com.edu.pe.pagaPeBackend.conversationProcess.dto;

import com.edu.pe.pagaPeBackend.conversationProcess.model.Conversation;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ConversationResponse {
    private Long id;
    private Conversation.ConversationStatus status;
    private Long clientId;  // id del cliente-servicio
    private Long reminderId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime finishAt;
    private List<MessageResponse> messages;
}
