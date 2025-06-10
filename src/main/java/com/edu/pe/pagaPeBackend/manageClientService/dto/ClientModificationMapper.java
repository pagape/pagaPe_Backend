package com.edu.pe.pagaPeBackend.manageClientService.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientHistoryResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientModification;

import java.util.List;
import java.util.stream.Collectors;

public class ClientModificationMapper {

    public static ClientHistoryResponse toClientHistoryResponse(Client client, List<ClientModification> modifications) {
        return ClientHistoryResponse.builder()
                .clientId(client.getId())
                .clientName(client.getUserFirstName() + " " + client.getUserLastName())
                .modifications(modifications.stream()
                        .map(ClientModificationMapper::toModificationDto)
                        .collect(Collectors.toList()))
                .build();
    }
    
    private static ClientHistoryResponse.ModificationDto toModificationDto(ClientModification modification) {
        return ClientHistoryResponse.ModificationDto.builder()
                .date(modification.getModificationDate())
                .user(modification.getModifiedBy())
                .action(modification.getAction().name())
                .details(modification.getDetails())
                .build();
    }
} 