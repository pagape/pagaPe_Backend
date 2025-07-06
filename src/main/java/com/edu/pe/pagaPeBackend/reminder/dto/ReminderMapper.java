package com.edu.pe.pagaPeBackend.reminder.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceSimpleResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientSimpleResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReminderMapper {

    public static ReminderResponse toDTO(Reminder reminder) {
        if (reminder == null) {
            return null;
        }

        ServiceSimpleResponse serviceDTO = null;
        if (reminder.getServiceFilter() != null) {
            Service service = reminder.getServiceFilter();
            serviceDTO = ServiceSimpleResponse.builder()
                    .id(service.getId())
                    .nombreServicio(service.getNombreServicio())
                    .build();
        }

        // Mapea la lista de CONTRATOS seleccionados
        List<ClientServiceSimpleResponse> contractDTOs = reminder.getSelectedContracts() != null
                ? reminder.getSelectedContracts().stream()
                .map(ReminderMapper::toClientServiceSimpleDTO) // Usamos un nuevo helper
                .collect(Collectors.toList())
                : Collections.emptyList();


        return ReminderResponse.builder()
                .id(reminder.getId())
                .reminderName(reminder.getReminderName())
                .description(reminder.getDescription())
                .debtorFilter(reminder.getDebtorFilter())
                .relativeDays(reminder.getRelativeDays())
                .companyWhatsappNumber(reminder.getCompanyWhatsappNumber())
                .scheduledDate(reminder.getScheduledDate())
                .status(reminder.getStatus())
                .serviceFilter(serviceDTO)
                .selectedContracts(contractDTOs) // Usamos el campo y la lista corregidos
                .build();
    }

    // Nuevo helper para convertir un ClientService a su DTO simple
    private static ClientServiceSimpleResponse toClientServiceSimpleDTO(ClientService contract) {
        return ClientServiceSimpleResponse.builder()
                .id(contract.getId())
                .amount(contract.getAmount())
                .dueDate(contract.getDueDate())
                .client(toClientSimpleDTO(contract.getClient())) // Reutilizamos el helper de cliente
                .build();
    }

    // El helper para convertir un Client a su DTO simple se queda igual
    private static ClientSimpleResponse toClientSimpleDTO(Client client) {
        return ClientSimpleResponse.builder()
                .id(client.getId())
                .userFirstName(client.getUserFirstName())
                .userLastName(client.getUserLastName())
                .userPhone(client.getUserPhone())
                .build();
    }
}