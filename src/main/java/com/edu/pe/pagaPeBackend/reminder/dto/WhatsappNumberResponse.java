package com.edu.pe.pagaPeBackend.reminder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhatsappNumberResponse {
    private Long id;
    private String number;
    private String alias;
    private Boolean isActive;
} 