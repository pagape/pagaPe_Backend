package com.edu.pe.pagaPeBackend.receipt.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import com.edu.pe.pagaPeBackend.receipt.model.Receipt;
import org.springframework.stereotype.Component;

@Component
public class ReceiptMapper {

    public Receipt toEntity(ReceiptRequest request) {
        return Receipt.builder()
                .issueDate(request.getIssueDate())
                .dueDate(request.getDueDate())
                .amount(request.getAmount())
                .estado(request.getEstado())
                .build();
    }
    
    public ReceiptResponse toResponse(Receipt receipt) {
        return ReceiptResponse.builder()
                .id(receipt.getId())
                .issueDate(receipt.getIssueDate())
                .dueDate(receipt.getDueDate())
                .amount(receipt.getAmount())
                .estado(receipt.getEstado())
                .clientService(receipt.getClienteServicio() != null ? mapClientService(receipt.getClienteServicio()) : null)
                .client(receipt.getClient() != null ? mapClient(receipt.getClient()) : null)
                .build();
    }
    
    private ClientServiceResponse mapClientService(ClientService clientService) {
        return ClientServiceResponse.builder()
                .id(clientService.getId())
                .fechaInicio(clientService.getFechaInicio())
                .fechaFin(clientService.getFechaFin())
                .estado(clientService.getEstado())
                .contratoVigente(clientService.isContratoVigente())
                .build();
    }
    
    private ClientResponse mapClient(Client client) {
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
} 