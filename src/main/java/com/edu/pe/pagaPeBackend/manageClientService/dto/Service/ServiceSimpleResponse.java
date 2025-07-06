package com.edu.pe.pagaPeBackend.manageClientService.dto.Service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceSimpleResponse {
    private Long id;
    private String nombreServicio;
}
