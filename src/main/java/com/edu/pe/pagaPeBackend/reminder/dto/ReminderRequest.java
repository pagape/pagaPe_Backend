package com.edu.pe.pagaPeBackend.reminder.dto;

import com.edu.pe.pagaPeBackend.reminder.model.Reminder.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReminderRequest {
    
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 100, message = "El título no debe exceder los 100 caracteres")
    private String title;
    
    private String description;
    
    @NotNull(message = "La fecha programada es obligatoria")
    private LocalDateTime scheduledDate;
    
    private ResponseStatus responseStatus;
    
    @NotNull(message = "El ID del servicio es obligatorio")
    private Long serviceId;
    
    @NotNull(message = "El ID del número de WhatsApp es obligatorio")
    private Long whatsappNumberId;
    
    private List<Long> clientServiceIds;
    
    private Boolean isDebtor;
    
    private Integer daysInAdvance;
} 