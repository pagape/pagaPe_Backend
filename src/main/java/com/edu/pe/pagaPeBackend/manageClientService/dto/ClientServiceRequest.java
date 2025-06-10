package com.edu.pe.pagaPeBackend.manageClientService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientServiceRequest {
    @NotNull
    private Long clientId;
    
    @NotNull
    private Long serviceId;
    
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Byte estado;
    
    @NotNull
    private Boolean contratoVigente;
}
