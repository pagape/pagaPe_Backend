package com.edu.pe.pagaPeBackend.manageClientService.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import org.springframework.stereotype.Component;

@Component
public class ClientServiceMapper {

    public ClientServiceResponse toResponse(ClientService clientService) {
        return ClientServiceResponse.builder()
                .id(clientService.getId())
                .client(mapClient(clientService.getClient()))
                .service(mapService(clientService.getService()))
                .fechaInicio(clientService.getFechaInicio())
                .fechaFin(clientService.getFechaFin())
                .estado(clientService.getEstado())
                .contratoVigente(clientService.isContratoVigente())
                .build();
    }
    
    public ClientService toEntity(ClientServiceRequest request, Client client, Service service) {
        return ClientService.builder()
                .client(client)
                .service(service)
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .estado(request.getEstado())
                .contratoVigente(request.getContratoVigente())
                .build();
    }
    
    private ClientResponse mapClient(Client client) {
        if (client == null) return null;
        return ClientResponse.builder()
                .id(client.getId())
                .userFirstName(client.getUserFirstName())
                .userLastName(client.getUserLastName())
                .userEmail(client.getUserEmail())
                .userPhone(client.getUserPhone())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .updatedBy(client.getUpdatedBy())
                .amount(client.getAmount())
                .issueDate(client.getIssueDate())
                .dueDate(client.getDueDate())
                .estado(client.getEstado())
                .clientServiceId(client.getClientService() != null ? client.getClientService().getId() : null)
                .build();
    }
    
    private ServiceResponse mapService(Service service) {
        if (service == null) return null;
        return ServiceResponse.builder()
                .id(service.getId())
                .nombreServicio(service.getNombreServicio())
                .descripcion(service.getDescripcion())
                .precioBase(service.getPrecioBase())
                .build();
    }
} 