package com.edu.pe.pagaPeBackend.conversationProcess.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CloseConversationRequest {
    private String sentimentLabel;
    private Double sentimentScore;
}

