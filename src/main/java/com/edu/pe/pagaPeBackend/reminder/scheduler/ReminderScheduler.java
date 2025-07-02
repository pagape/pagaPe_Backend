package com.edu.pe.pagaPeBackend.reminder.scheduler;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.reminder.gateway.WhatsAppGateway;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.model.ResponseStatus;
import com.edu.pe.pagaPeBackend.reminder.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

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

        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));

        for (Reminder reminder : remindersToSend) {
            try {
                // Prepara las 4 piezas de texto para la plantilla
                Client client = reminder.getClient();
                String p1_clientName = client.getUserFirstName();
                String p2_amount = String.format(new Locale("es", "PE"), "S/ %.2f", client.getAmount());
                String p3_issueDate = client.getIssueDate().format(dateFormatter);
                String p4_dueDate = client.getDueDate().format(dateFormatter);

                // Intenta enviar el mensaje usando la plantilla
                whatsAppGateway.sendMessage(
                        reminder.getClientWhatsappPhoneNumber(),
                        p1_clientName,
                        p2_amount,
                        p3_issueDate,
                        p4_dueDate
                );

                // Si es exitoso, actualizamos el estado
                System.out.println("--> [Scheduler] Envío real exitoso para reminder " + reminder.getId());
                reminderService.updateReminderStatus(reminder.getId(), ResponseStatus.ENVIADO);

            } catch (Exception e) {
                // Si falla, actualizamos a ERROR
                System.err.println("--> [Scheduler] Error al procesar reminder " + reminder.getId() + ": " + e.getMessage());
                reminderService.updateReminderStatus(reminder.getId(), ResponseStatus.ERROR_DE_ENVIO);
            }
        }
    }
}