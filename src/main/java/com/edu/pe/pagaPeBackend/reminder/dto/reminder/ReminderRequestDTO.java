package com.edu.pe.pagaPeBackend.reminder.dto.reminder;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReminderRequestDTO {
    private String description;
    private LocalDateTime sendDateTime;
    private String typeService;
    private String clientWhatsappPhoneNumber;
    private Boolean isDebtor;
    private Long clientId;
}
