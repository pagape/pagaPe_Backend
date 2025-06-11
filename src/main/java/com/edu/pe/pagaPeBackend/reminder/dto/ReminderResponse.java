package com.edu.pe.pagaPeBackend.reminder.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceResponse;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReminderResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime scheduledDate;
    private ResponseStatus responseStatus;
    private ServiceResponse service;
    private WhatsappNumberResponse whatsappNumber;
    private List<ClientServiceResponse> clientServices;
    private Boolean isDebtor;
    private Integer daysInAdvance;
} 