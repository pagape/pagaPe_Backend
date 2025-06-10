package com.edu.pe.pagaPeBackend.manageClientService.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientHistoryResponse {
    private Long clientId;
    private String clientName;
    private List<ModificationDto> modifications;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModificationDto {
        private LocalDateTime date;
        private String user;
        private String action;
        private String details;
    }
} 