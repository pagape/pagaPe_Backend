package com.edu.pe.pagaPeBackend.reminder.service.impl;

import com.edu.pe.pagaPeBackend.conversationProcess.dto.ConversationRequest;
import com.edu.pe.pagaPeBackend.conversationProcess.dto.ConversationResponse;
import com.edu.pe.pagaPeBackend.conversationProcess.model.Conversation;
import com.edu.pe.pagaPeBackend.conversationProcess.service.ConversationService;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientServiceRepository;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ServiceRepository;
import com.edu.pe.pagaPeBackend.reminder.dto.ReminderRequest;
import com.edu.pe.pagaPeBackend.reminder.dto.WhatsAppMessageRequest;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.repository.ReminderRepository;
import com.edu.pe.pagaPeBackend.reminder.service.ReminderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("ReminderServiceImpl")
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ClientServiceRepository clientServiceRepository;
    private final ServiceRepository serviceRepository;
    private final ConversationService conversationService;


    @Override
    @Transactional
    public Reminder createReminder(ReminderRequest request) {
        Service serviceFilter = null;
        if (request.getServiceIdFilter() != null) {
            serviceFilter = serviceRepository.findById(request.getServiceIdFilter())
                    .orElseThrow(() -> new EntityNotFoundException("Servicio con ID " + request.getServiceIdFilter() + " no encontrado."));
        }

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
                .build();

        return reminderRepository.save(reminder);
    }


    @Override
    @Transactional
    public List<WhatsAppMessageRequest> processPendingReminders() {
        List<Reminder> scheduledReminders = reminderRepository.findByStatusAndScheduledDate(
                Reminder.ReminderStatus.PENDING, LocalDate.now()
        );

        List<WhatsAppMessageRequest> allMessagesToSend = new ArrayList<>();

        for (Reminder reminder : scheduledReminders) {
            try {
                reminder.setStatus(Reminder.ReminderStatus.PROCESSING);
                reminderRepository.save(reminder);

                List<ClientService> matchedContracts = clientServiceRepository.findByDueDateAndOptionalService(
                        calculateTargetDueDate(reminder),
                        reminder.getServiceFilter() != null ? reminder.getServiceFilter().getId() : null
                );

                if (matchedContracts.isEmpty()) {
                    reminder.setStatus(Reminder.ReminderStatus.COMPLETED);
                    reminderRepository.save(reminder);
                    continue;
                }

                reminder.setSelectedContracts(matchedContracts);

                List<Client> uniqueClients = matchedContracts.stream()
                        .map(ClientService::getClient)
                        .distinct()
                        .collect(Collectors.toList());

                Map<Long, Long> clientConversationMap = new HashMap<>();

                for (Client client : uniqueClients) {
                    ConversationRequest conversationRequest = ConversationRequest.builder()
                            .clientId(client.getId())
                            .reminderId(reminder.getId())
                            .status(Conversation.ConversationStatus.ABIERTA)
                            .build();

                    ConversationResponse createdConversation = conversationService.createConversation(conversationRequest);

                    clientConversationMap.put(client.getId(), createdConversation.getId());
                }


                for (ClientService contract : matchedContracts) {
                    Long conversationId = clientConversationMap.get(contract.getClient().getId());
                    allMessagesToSend.add(mapToWhatsAppMessageRequest(contract, reminder, conversationId));
                }

                reminder.setStatus(Reminder.ReminderStatus.COMPLETED);
                reminderRepository.save(reminder);

            } catch (Exception e) {
                reminder.setStatus(Reminder.ReminderStatus.FAILED);
                reminderRepository.save(reminder);
                // log.error("Error procesando reminder " + reminder.getId(), e);
            }
        }
        return allMessagesToSend;
    }

    @Override
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    @Override
    public Reminder getReminderById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reminder con ID " + id + " no encontrado."));
    }

    private LocalDate calculateTargetDueDate(Reminder reminder) {
        // La lógica de cálculo con LocalDate.now() se mantiene, como pediste.
        if (Boolean.TRUE.equals(reminder.getDebtorFilter())) {
            return LocalDate.now().minusDays(reminder.getRelativeDays());
        } else {
            return LocalDate.now().plusDays(reminder.getRelativeDays());
        }
    }

    private WhatsAppMessageRequest mapToWhatsAppMessageRequest(ClientService contract, Reminder reminder, Long conversationId) {
        Client client = contract.getClient();
        String messageType = Boolean.TRUE.equals(reminder.getDebtorFilter()) ? "deuda" : "recordatorio";

        return WhatsAppMessageRequest.builder()
                .nombre(client.getUserFirstName() + " " + client.getUserLastName())
                .numero(client.getUserPhone())
                .servicio(contract.getService().getNombreServicio())
                .monto(String.format("%.2f", contract.getAmount()))
                .fechaInicio(contract.getIssueDate().toString())
                .fechaFin(contract.getDueDate().toString())
                .tipo(messageType)
                .conversationId(conversationId) // <-- Aquí se asigna el ID
                .build();
    }
}