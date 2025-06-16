package com.edu.pe.pagaPeBackend.reminder.service.impl;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientRepository;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.model.ResponseStatus;
import com.edu.pe.pagaPeBackend.reminder.repository.ReminderRepository;
import com.edu.pe.pagaPeBackend.reminder.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {
    @Autowired
    private ReminderRepository reminderRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Reminder createReminder(Reminder reminder, Long clientId) {
        // Validar fecha futura
        if (reminder.getSendDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del recordatorio debe ser futura.");
        }

        // Asignar cliente
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        reminder.setClient(client);

        reminder.setClientName(client.getUserFirstName() + " " + client.getUserLastName());

        // Asignar plantilla si no hay descripción escrita
        if (reminder.getDescription() == null || reminder.getDescription().isBlank()) {
            String template = getTemplate(reminder.getIsDebtor());
            reminder.setDescription(template);
        }

        // Estado por defecto
        reminder.setResponseStatus(ResponseStatus.PENDIENTE);

        return reminderRepository.save(reminder);
    }

    @Override
    public List<Reminder> getByClient(Long clientId) {
        return reminderRepository.findByClientId(clientId);
    }

    @Override
    public List<Reminder> getExpired() {
        return reminderRepository.findBySendDateTimeBefore(LocalDateTime.now());
    }

    @Override
    public List<Reminder> getExpireToday() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(LocalTime.MAX);
        return reminderRepository.findBySendDateTimeBetween(start, end);
    }

    @Override
    public List<Reminder> getExpireInNextDays(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futuro = now.plusDays(days);
        return reminderRepository.findBySendDateTimeBetween(now, futuro);
    }
    @Override
    public List<Reminder> getAll() {
        return reminderRepository.findAll();
    }

    private String getTemplate(Boolean isDebtor){
        if (isDebtor) {
            return "Estimado cliente, se le recuerda que tiene pagos pendientes.";
        } else {
            return "Estimado cliente, su pago está próximo a vencer.";
        }
    }

}
