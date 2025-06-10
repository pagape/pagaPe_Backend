package com.edu.pe.pagaPeBackend.manageClientService.dto.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private Long id;
    private String nombreServicio;
    private String descripcion;
    private Double precioBase;
}
