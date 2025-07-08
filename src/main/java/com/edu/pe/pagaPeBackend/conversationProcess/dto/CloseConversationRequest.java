package com.edu.pe.pagaPeBackend.conversationProcess.dto;

import com.edu.pe.pagaPeBackend.conversationProcess.model.Conversation;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloseConversationRequest {
    private Conversation.SentimentLabel sentimentLabel;
    private Conversation.StatusFinish statusFinish;
    private Double sentimentScore;
}

