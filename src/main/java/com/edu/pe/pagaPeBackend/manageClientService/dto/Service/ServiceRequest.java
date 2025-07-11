package com.edu.pe.pagaPeBackend.manageClientService.dto.Service;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequest {
    private String nombreServicio;
    private String descripcion;
    private Double precioBase;
}
