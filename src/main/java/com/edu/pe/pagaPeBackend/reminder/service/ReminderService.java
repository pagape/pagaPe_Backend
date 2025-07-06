package com.edu.pe.pagaPeBackend.reminder.service;

import com.edu.pe.pagaPeBackend.reminder.dto.ReminderRequest;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.dto.WhatsAppMessageRequest;

import java.util.List;

public interface ReminderService {

    /**
     * Crea un nuevo recordatorio basado en los datos de la solicitud.
     * @param request El DTO con la información para crear el recordatorio.
     * @return La entidad Reminder una vez guardada.
     */
    Reminder createReminder(ReminderRequest request);

    /**
     * Busca y procesa todos los recordatorios pendientes.
     * selecciona los clientes según los filtros
     * y genera los datos para los mensajes de WhatsApp.
     * @return Una lista con los datos de todos los mensajes a enviar.
     */
    List<WhatsAppMessageRequest> processPendingReminders();

    List<Reminder> getAllReminders();
    Reminder getReminderById(Long id);
}