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
                .issueDate(clientService.getIssueDate())
                .dueDate(clientService.getDueDate())
                .paymentFrequency(clientService.getPaymentFrequency())
                .amount(clientService.getAmount())
                .estado(clientService.getEstado())
                .contratoVigente(clientService.getContratoVigente())
                .build();
    }
    
    public ClientService toEntity(ClientServiceRequest request, Client client, Service service) {
        return ClientService.builder()
                .client(client)
                .service(service)
                .dueDate(request.getDueDate())
                .issueDate(request.getIssueDate())
                .amount(request.getAmount())
                .paymentFrequency(request.getPaymentFrequency())
                .contratoVigente(true)
                .estado(true)
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
                .active(client.getActive())
                .build();
    }
    
    private ServiceResponse mapService(Service service) {
        if (service == null) return null;
        return ServiceResponse.builder()
                .id(service.getId())
                .nombreServicio(service.getNombreServicio())
                .descripcion(service.getDescripcion())
                .build();
    }
} 