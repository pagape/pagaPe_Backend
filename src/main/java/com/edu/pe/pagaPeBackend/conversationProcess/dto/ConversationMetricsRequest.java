package com.edu.pe.pagaPeBackend.conversationProcess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationMetricsRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}