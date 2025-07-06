package com.edu.pe.pagaPeBackend.conversationProcess.dto;


import com.edu.pe.pagaPeBackend.conversationProcess.model.Message;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MessageRequest {
    private Long conversationId;
    private String messageContent;
    private Message.MessageType messageType;
    private Message.SenderType sender;
    private LocalDateTime timestamp;

}