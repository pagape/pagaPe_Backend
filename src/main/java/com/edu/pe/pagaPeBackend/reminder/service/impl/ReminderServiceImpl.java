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

@Component("ReminderServiceImpl")
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

        // Validación: Asegura  que la fecha de programación no sea en el pasado.
        if (request.getScheduledDate() == null || request.getScheduledDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de programación del recordatorio no puede ser nula o una fecha pasada.");
        }

        Reminder reminder = Reminder.builder()
                .reminderName(request.getReminderName())
                .description(request.getDescription())
                .debtorFilter(request.getDebtorFilter())
                .serviceFilter(serviceFilter)
                .relativeDays(request.getRelativeDays())
                .scheduledDate(request.getScheduledDate())
                .companyWhatsappNumber(request.getCompanyWhatsappNumber())
                // El estado PENDING se asigna por defecto en la entidad
                .build();

        return reminderRepository.save(reminder);
    }

    @Override
    @Transactional
    public List<WhatsAppMessageRequest> processPendingReminders() {
        // 1. Busca recordatorios programados para HOY que estén PENDIENTES
        List<Reminder> scheduledReminders = reminderRepository.findByStatusAndScheduledDate(
                Reminder.ReminderStatus.PENDING, LocalDate.now()
        );

        List<WhatsAppMessageRequest> allMessagesToSend = new ArrayList<>();

        for (Reminder reminder : scheduledReminders) {
            try {
                // 2. Marca como procesando para evitar re-procesamiento en caso de ejecuciones concurrentes
                reminder.setStatus(Reminder.ReminderStatus.PROCESSING);
                reminderRepository.save(reminder);

                // 3. Calcula la fecha de vencimiento objetivo
                LocalDate targetDueDate;
                if (Boolean.TRUE.equals(reminder.getDebtorFilter())) {
                    targetDueDate = LocalDate.now().minusDays(reminder.getRelativeDays());
                } else {
                    targetDueDate = LocalDate.now().plusDays(reminder.getRelativeDays());
                }

                // 4. Obtener los contratos usando la nueva consulta y la fecha objetivo
                List<ClientService> matchedContracts = clientServiceRepository.findByDueDateAndOptionalService(
                        targetDueDate,
                        reminder.getServiceFilter() != null ? reminder.getServiceFilter().getId() : null
                );

                // Si no hay contratos que coincidan, completa el reminder y continua
                if(matchedContracts.isEmpty()){
                    reminder.setStatus(Reminder.ReminderStatus.COMPLETED);
                    reminderRepository.save(reminder);
                    continue; // Pasa al siguiente reminder
                }

                reminder.setSelectedContracts(matchedContracts);

                // 6. Prepara los datos para la API de WhatsApp
                for (ClientService contract : matchedContracts) {
                    allMessagesToSend.add(mapToWhatsAppMessageRequest(contract, reminder));
                }

                // 7. Marca como completado
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
