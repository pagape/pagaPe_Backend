package com.edu.pe.pagaPeBackend.reminder.dto;

import lombok.Getter;

@Getter
public class ReminderRequest {
    private String reminderName;
    private String description;
    private Boolean debtorFilter;
    private Long serviceIdFilter; // Solo necesita el ID del servicio, puede ser nulo
    private Integer daysUntilSend;
    private String companyWhatsappNumber;
}