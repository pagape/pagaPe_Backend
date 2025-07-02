package com.edu.pe.pagaPeBackend.reminder.service.impl;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientRepository;
import com.edu.pe.pagaPeBackend.reminder.dto.reminder.ReminderRequestDTO;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.model.ResponseStatus;
import com.edu.pe.pagaPeBackend.reminder.repository.ReminderRepository;
import com.edu.pe.pagaPeBackend.reminder.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {
    @Autowired
    private ReminderRepository reminderRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    @Transactional
    public Reminder createReminder(ReminderRequestDTO request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + request.getClientId()));

        Reminder reminder = new Reminder();

        reminder.setClient(client);
        reminder.setSendDateTime(request.getSendDateTime());
        reminder.setResponseStatus(ResponseStatus.PENDIENTE);
        reminder.setTypeService(request.getTypeService());

        reminder.setClientWhatsappPhoneNumber(client.getUserPhone());
        reminder.setClientName(client.getUserFirstName() + " " + client.getUserLastName());

        boolean isDebtor;
        if (request.getIsDebtor() != null) {
            isDebtor = request.getIsDebtor();
        } else {
            isDebtor = client.getDueDate().isBefore(LocalDate.now());
        }
        reminder.setIsDebtor(isDebtor);

        String baseMessage = getDynamicTemplate(isDebtor, client);

        String finalMessage = baseMessage;
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            finalMessage += "\n\nNota Adicional: " + request.getDescription().trim();
        }

        reminder.setDescription(finalMessage);
        return reminderRepository.save(reminder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reminder> getByClient(Long clientId) {
        return reminderRepository.findByClientId(clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reminder> getExpired() {
        return reminderRepository.findBySendDateTimeBefore(LocalDateTime.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reminder> getExpireToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return reminderRepository.findBySendDateTimeBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reminder> getExpireInNextDays(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futuro = now.plusDays(days);
        return reminderRepository.findBySendDateTimeBetween(now, futuro);
    }

    @Override
    @Transactional
    public void updateReminderStatus(Long reminderId, ResponseStatus newStatus) {
        // Busca el recordatorio en la BD
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Recordatorio no encontrado con id: " + reminderId));

        // Cambia su estado
        reminder.setResponseStatus(newStatus);

        // Guarda cambios
        reminderRepository.save(reminder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reminder> findRemindersReadyToSend() {
        return reminderRepository.findReadyToSendWithClient(
                LocalDateTime.now(),
                ResponseStatus.PENDIENTE
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reminder> getAll() {
        return reminderRepository.findAll();
    }

    private String getDynamicTemplate(Boolean isDebtor, Client client) {
        // Formateador para la moneda local (Soles)
        String formattedAmount = String.format(new Locale("es", "PE"), "S/ %.2f", client.getAmount());

        // Formateador para las fechas
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDueDate = client.getDueDate().format(dateFormatter);
        String formattedIssueDate = client.getIssueDate().format(dateFormatter);

        if (isDebtor) {
            return String.format(
                    "Estimado/a %s, le recordamos sobre su deuda vencida de %s, emitida el %s y con fecha de vencimiento el %s. Por favor, regularice su pago.",
                    client.getUserFirstName(),
                    formattedAmount,
                    formattedIssueDate,
                    formattedDueDate
            );
        } else {
            return String.format(
                    "Hola %s. Este es un recordatorio amigable sobre su pago de %s (emitido el %s) que vence el %s.",
                    client.getUserFirstName(),
                    formattedAmount,
                    formattedIssueDate,
                    formattedDueDate
            );
        }
    }


}
