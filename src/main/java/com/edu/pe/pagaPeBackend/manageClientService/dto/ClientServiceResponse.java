package com.edu.pe.pagaPeBackend.manageClientService.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientServiceResponse {
    private Long id;
    private ClientResponse client;
    private ServiceResponse service;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Byte estado;
    private Boolean contratoVigente;
    
    // Helper method to get estado as readable string
    public String getEstadoAsString() {
        if (estado == null) return null;
        switch (estado) {
            case 1: return "Activo";
            case 2: return "Suspendido";
            case 3: return "Cancelado";
            default: return "Desconocido";
        }
    }
}
