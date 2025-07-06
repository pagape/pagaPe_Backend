package com.edu.pe.pagaPeBackend.conversationProcess.dto;


import com.edu.pe.pagaPeBackend.conversationProcess.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {

    private Long id;
    private String messageContent;
    private Message.MessageType messageType;
    private Message.SenderType sender;
    private LocalDateTime timestamp;
    private Long conversationId;

}
