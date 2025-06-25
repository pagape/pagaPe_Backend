package com.edu.pe.pagaPeBackend.reminder.scheduler;

import com.edu.pe.pagaPeBackend.reminder.gateway.WhatsAppGateway;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.model.ResponseStatus;
import com.edu.pe.pagaPeBackend.reminder.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReminderScheduler {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private WhatsAppGateway whatsAppGateway;

    // Se ejecuta cada minuto, todos los días
    @Scheduled(cron = "0 * * * * *")
    public void processPendingReminders() {
        System.out.println("--> [Scheduler] Buscando recordatorios para enviar... " + LocalDateTime.now());

        List<Reminder> remindersToSend = reminderService.findRemindersReadyToSend();

        if (remindersToSend.isEmpty()) {
            System.out.println("--> [Scheduler] No hay recordatorios pendientes.");
            return;
        }

        System.out.println("--> [Scheduler] Se encontraron " + remindersToSend.size() + " recordatorios para procesar.");

        for (Reminder reminder : remindersToSend) {
            try {
                // 1. Intentamos enviar el mensaje PRIMERO
                whatsAppGateway.sendMessage(
                        reminder.getClientWhatsappPhoneNumber(),
                        reminder.getDescription()
                );

                // 2. SI Y SOLO SI el envío fue exitoso, actualizamos el estado a ENVIADO
                System.out.println("--> [Scheduler] Envío exitoso para reminder " + reminder.getId() + ". Actualizando estado.");
                reminderService.updateReminderStatus(reminder.getId(), ResponseStatus.ENVIADO);

            } catch (Exception e) {
                // 3. SI algo falló en el envío, actualizamos el estado a ERROR
                System.err.println("--> [Scheduler] Error al procesar reminder " + reminder.getId() + ": " + e.getMessage());
                reminderService.updateReminderStatus(reminder.getId(), ResponseStatus.ERROR_DE_ENVIO);
            }
        }
    }
}