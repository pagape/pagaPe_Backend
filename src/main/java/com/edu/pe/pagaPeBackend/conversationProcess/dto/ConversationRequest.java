package com.edu.pe.pagaPeBackend.conversationProcess.dto;

import com.edu.pe.pagaPeBackend.conversationProcess.model.Conversation;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ConversationRequest {
    private Long clientId;  //cliente-servicio id
    private Long reminderId;
    private Conversation.ConversationStatus status;
}
