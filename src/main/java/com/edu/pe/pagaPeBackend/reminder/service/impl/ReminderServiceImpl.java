package com.edu.pe.pagaPeBackend.reminder.service.impl;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientServiceRepository;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ServiceRepository;
import com.edu.pe.pagaPeBackend.reminder.dto.ReminderRequest;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.dto.WhatsAppMessageRequest;
import com.edu.pe.pagaPeBackend.reminder.repository.ReminderRepository;
import com.edu.pe.pagaPeBackend.reminder.service.ReminderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("ReminderServiceImpl") // Usando un nombre explícito para el bean
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ClientServiceRepository clientServiceRepository;
    private final ServiceRepository serviceRepository;

    @Override
    @Transactional
    public Reminder createReminder(ReminderRequest request) {
        Service serviceFilter = null;
        if (request.getServiceIdFilter() != null) {
            serviceFilter = serviceRepository.findById(request.getServiceIdFilter())
                    .orElseThrow(() -> new EntityNotFoundException("Servicio con ID " + request.getServiceIdFilter() + " no encontrado."));
        }

        Reminder reminder = Reminder.builder()
                .reminderName(request.getReminderName())
                .description(request.getDescription())
                .debtorFilter(request.getDebtorFilter())
                .serviceFilter(serviceFilter)
                .daysUntilSend(request.getDaysUntilSend())
                .companyWhatsappNumber(request.getCompanyWhatsappNumber())
                // Los campos createdAt, scheduledSendDate y status se asignan con @PrePersist
                .build();

        return reminderRepository.save(reminder);
    }

    @Override
    @Transactional
    public List<WhatsAppMessageRequest> processPendingReminders() {
        // 1. Busca recordatorios pendientes
        List<Reminder> pendingReminders = reminderRepository.findByStatusAndScheduledSendDateLessThanEqual(
                Reminder.ReminderStatus.PENDING, LocalDateTime.now()
        );

        List<WhatsAppMessageRequest> allMessagesToSend = new ArrayList<>();

        for (Reminder reminder : pendingReminders) {
            try {
                // 2. Marca como procesando para evitar re-procesamiento en caso de ejecuciones concurrentes
                reminder.setStatus(Reminder.ReminderStatus.PROCESSING);
                reminderRepository.save(reminder);

                // 3. Obtener los clientes según los filtros del recordatorio
                List<ClientService> matchedContracts = clientServiceRepository.findActiveServicesForReminder(
                        LocalDate.now(),
                        reminder.getDebtorFilter(),
                        reminder.getServiceFilter() != null ? reminder.getServiceFilter().getId() : null
                );

                // Si no hay contratos que coincidan, completa el reminder y continua
                if(matchedContracts.isEmpty()){
                    reminder.setStatus(Reminder.ReminderStatus.COMPLETED);
                    reminderRepository.save(reminder);
                    continue; // Pasa al siguiente reminder
                }

                // 4. Guarda los clientes seleccionados en el recordatorio y genera la lista de mensajes
                List<Client> selectedClients = matchedContracts.stream()
                        .map(ClientService::getClient)
                        .distinct()
                        .collect(Collectors.toList());

                reminder.setSelectedClients(selectedClients);

                // 5. Prepara los datos para la API de WhatsApp
                for (ClientService contract : matchedContracts) {
                    allMessagesToSend.add(mapToWhatsAppMessageRequest(contract, reminder));
                }

                // 6. Marca como completado
                reminder.setStatus(Reminder.ReminderStatus.COMPLETED);
                reminderRepository.save(reminder);

            } catch (Exception e) {
                // En caso de error, marca como fallido para revisión manual
                reminder.setStatus(Reminder.ReminderStatus.FAILED);
                reminderRepository.save(reminder);
                // Aquí loggear el error: log.error("Error procesando reminder " + reminder.getId(), e);
            }
        }
        return allMessagesToSend;
    }

    // Implementación de métodos adicionales
    @Override
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    @Override
    public Reminder getReminderById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reminder con ID " + id + " no encontrado."));
    }

    // Helper para mapear los datos
    private WhatsAppMessageRequest mapToWhatsAppMessageRequest(ClientService contract, Reminder reminder) {
        Client client = contract.getClient();

        // Lógica para determinar el tipo de mensaje
        String messageType = reminder.getDebtorFilter() ? "deuda" : "recordatorio";

        return WhatsAppMessageRequest.builder()
                .nombre(client.getUserFirstName() + " " + client.getUserLastName())
                .numero(client.getUserPhone())
                .servicio(contract.getService().getNombreServicio())
                .monto(String.format("%.2f", contract.getAmount())) // Formatea el BigDecimal a un String con 2 decimales
                .fechaInicio(contract.getIssueDate().toString()) // Convierte LocalDate a String YYYY-MM-DD
                .fechaFin(contract.getDueDate().toString())
                .tipo(messageType)
                .build();
    }
}
