package com.edu.pe.pagaPeBackend.reminder.dto.reminder;

import com.edu.pe.pagaPeBackend.reminder.model.ResponseStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReminderResponseDTO {
    private Long id;
    private String description;
    private LocalDateTime sendDateTime;
    private ResponseStatus responseStatus;
    private String typeService;
    private String clientWhatsappPhoneNumber;
    private Boolean isDebtor;

    // Cliente
    private Long clientId;
    private String clientName;
}
