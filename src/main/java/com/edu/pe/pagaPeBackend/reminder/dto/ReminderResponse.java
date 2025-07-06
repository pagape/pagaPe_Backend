package com.edu.pe.pagaPeBackend.reminder.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceSimpleResponse;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReminderResponse {
    private Long id;
    private String reminderName;
    private String description;
    private Boolean debtorFilter;
    private Integer relativeDays;
    private LocalDate scheduledDate;
    private String companyWhatsappNumber;
    private Reminder.ReminderStatus status;

    private ServiceSimpleResponse serviceFilter;
    private List<ClientServiceSimpleResponse> selectedContracts;
}
