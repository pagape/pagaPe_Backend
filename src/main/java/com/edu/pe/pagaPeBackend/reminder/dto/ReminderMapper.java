package com.edu.pe.pagaPeBackend.reminder.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceSimpleResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientSimpleResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReminderMapper {

    // Convierte una entidad Reminder a un DTO de respuesta
    public static ReminderResponse toDTO(Reminder reminder) {
        if (reminder == null) {
            return null;
        }

        // Mapea el filtro de servicio si existe
        ServiceSimpleResponse serviceDTO = null;
        if (reminder.getServiceFilter() != null) {
            Service service = reminder.getServiceFilter();
            serviceDTO = ServiceSimpleResponse.builder()
                    .id(service.getId())
                    .nombreServicio(service.getNombreServicio())
                    .build();
        }

        // Mapea la lista de clientes seleccionados si existe
        List<ClientSimpleResponse> clientDTOs = reminder.getSelectedClients() != null
                ? reminder.getSelectedClients().stream().map(ReminderMapper::toClientSimpleDTO).collect(Collectors.toList())
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
                .selectedClients(clientDTOs)
                .build();
    }

    // Pequeño helper para convertir un Client a su DTO simple
    private static ClientSimpleResponse toClientSimpleDTO(Client client) {
        return ClientSimpleResponse.builder()
                .id(client.getId())
                .userFirstName(client.getUserFirstName())
                .userLastName(client.getUserLastName())
                .userPhone(client.getUserPhone())
                .build();
    }
}