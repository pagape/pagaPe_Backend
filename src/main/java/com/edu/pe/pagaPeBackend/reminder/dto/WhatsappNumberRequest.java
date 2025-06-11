package com.edu.pe.pagaPeBackend.reminder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WhatsappNumberRequest {
    
    @NotBlank(message = "El número de WhatsApp es obligatorio")
    @Size(max = 20, message = "El número no debe exceder los 20 caracteres")
    @Pattern(regexp = "^\\d+$", message = "El número debe contener solo dígitos")
    private String number;
    
    @Size(max = 100, message = "El alias no debe exceder los 100 caracteres")
    private String alias;
    
    private Boolean isActive;
} 