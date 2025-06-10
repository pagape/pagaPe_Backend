package com.edu.pe.pagaPeBackend.manageClientService.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;

import java.time.LocalDateTime;

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
                .build();
    }

    public static Client toClient(ClientRequest clientRequest) {
        return Client.builder()
                .userFirstName(clientRequest.getUserFirstName())
                .userLastName(clientRequest.getUserLastName())
                .userEmail(clientRequest.getUserEmail())
                .userPhone(clientRequest.getUserPhone())
                .createdAt(LocalDateTime.now())
                .build();
    }
} 