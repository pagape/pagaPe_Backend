package com.edu.pe.pagaPeBackend.reminder.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceSimpleResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientSimpleResponse;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReminderResponse {
    private Long id;
    private String reminderName;
    private String description;
    private Boolean debtorFilter;
    private Integer daysUntilSend;
    private String companyWhatsappNumber;
    private LocalDateTime createdAt;
    private LocalDateTime scheduledSendDate;
    private Reminder.ReminderStatus status;

    // En lugar de los objetos completos, usamos nuestros DTOs simples
    private ServiceSimpleResponse serviceFilter;
    private List<ClientSimpleResponse> selectedClients;
}
