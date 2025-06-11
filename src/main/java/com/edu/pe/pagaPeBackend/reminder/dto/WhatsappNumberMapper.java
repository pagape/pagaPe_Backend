package com.edu.pe.pagaPeBackend.reminder.dto;

import com.edu.pe.pagaPeBackend.reminder.model.WhatsappNumber;
import org.springframework.stereotype.Component;

@Component
public class WhatsappNumberMapper {
    
    public WhatsappNumber toWhatsappNumber(WhatsappNumberRequest request) {
        return WhatsappNumber.builder()
                .number(request.getNumber())
                .alias(request.getAlias())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
    }
    
    public static WhatsappNumberResponse toWhatsappNumberResponse(WhatsappNumber whatsappNumber) {
        return WhatsappNumberResponse.builder()
                .id(whatsappNumber.getId())
                .number(whatsappNumber.getNumber())
                .alias(whatsappNumber.getAlias())
                .isActive(whatsappNumber.isActive())
                .build();
    }
} 