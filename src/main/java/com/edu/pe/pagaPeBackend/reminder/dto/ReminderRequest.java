package com.edu.pe.pagaPeBackend.reminder.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class ReminderRequest {
    private String reminderName;
    private String description;
    private Boolean debtorFilter;
    private Long serviceIdFilter;
    private Integer relativeDays;
    private LocalDate scheduledDate;
    private String companyWhatsappNumber;
}