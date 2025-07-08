package com.edu.pe.pagaPeBackend.conversationProcess.dto;

import lombok.*;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversationMetricsResponse {

    private Long totalConversations;

    private Map<String, Long> distribution;

    private Map<String, Double> percentages;
}
