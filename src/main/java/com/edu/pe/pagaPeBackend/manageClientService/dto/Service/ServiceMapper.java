package com.edu.pe.pagaPeBackend.manageClientService.dto.Service;

import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public ServiceResponse toResponse(Service service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .nombreServicio(service.getNombreServicio())
                .descripcion(service.getDescripcion())

                .build();
    }
    
    public Service toEntity(ServiceRequest request) {
        return Service.builder()
                .nombreServicio(request.getNombreServicio())
                .descripcion(request.getDescripcion())
                .build();
    }
    
    public Service updateFromRequest(Service service, ServiceRequest request) {
        service.setNombreServicio(request.getNombreServicio());
        service.setDescripcion(request.getDescripcion());
        return service;
    }
} 