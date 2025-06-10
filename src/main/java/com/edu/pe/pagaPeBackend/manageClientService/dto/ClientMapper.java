package com.edu.pe.pagaPeBackend.manageClientService.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ClientMapper {

    public static ClientResponse toClientResponse(Client client) {
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

    public static Client toClient(ClientRequest clientRequest) {
        return Client.builder()
                .userFirstName(clientRequest.getUserFirstName())
                .userLastName(clientRequest.getUserLastName())
                .userEmail(clientRequest.getUserEmail())
                .userPhone(clientRequest.getUserPhone())
                .amount(clientRequest.getAmount())
                .issueDate(clientRequest.getIssueDate())
                .dueDate(clientRequest.getDueDate())
                .estado(clientRequest.getEstado())
                .createdAt(LocalDateTime.now())
                .build();
    }
    
    public static Client updateClientFromRequest(Client client, ClientRequest request) {
        client.setUserFirstName(request.getUserFirstName());
        client.setUserLastName(request.getUserLastName());
        client.setUserEmail(request.getUserEmail());
        client.setUserPhone(request.getUserPhone());
        client.setAmount(request.getAmount());
        client.setIssueDate(request.getIssueDate());
        client.setDueDate(request.getDueDate());
        client.setEstado(request.getEstado());
        client.setUpdatedAt(LocalDateTime.now());
        return client;
    }
} 