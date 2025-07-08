package com.edu.pe.pagaPeBackend.tasks;

import com.edu.pe.pagaPeBackend.WhatssAppHelper.WhatsAppService;
import com.edu.pe.pagaPeBackend.reminder.dto.WhatsAppMessageRequest;
import com.edu.pe.pagaPeBackend.reminder.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderTaskScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReminderTaskScheduler.class);

    private final ReminderService reminderService;
    private final WhatsAppService whatsAppService;

    @Scheduled(cron = "0 * * * * ?", zone = "America/Lima")
    public void runDailyReminderProcessing() {
        log.info("Iniciando tarea programada de procesamiento de recordatorios a las {}", LocalDateTime.now());
        try {
            // 1. Genera la lista de mensajes a enviar
            List<WhatsAppMessageRequest> messagesToSend = reminderService.processPendingReminders();

            // 2. Si hay mensajes para enviar, llama al servicio de WhatsApp
            if (messagesToSend != null && !messagesToSend.isEmpty()) {
                log.info("Lista de mensajes generados: {}", messagesToSend);
                log.info("Se generaron {} mensajes. Enviando a la API de WhatsApp...", messagesToSend.size());
                whatsAppService.sendMessages(messagesToSend);
            } else {
                log.info("No se generaron mensajes en esta ejecución.");
            }

        } catch (Exception e) {
            log.error("Error durante la ejecución programada de recordatorios.", e);
        }
    }
}