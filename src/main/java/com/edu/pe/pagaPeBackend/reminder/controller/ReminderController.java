package com.edu.pe.pagaPeBackend.reminder.controller;

import com.edu.pe.pagaPeBackend.WhatssAppHelper.WhatsAppService;
import com.edu.pe.pagaPeBackend.reminder.dto.ReminderMapper;
import com.edu.pe.pagaPeBackend.reminder.dto.ReminderRequest;
import com.edu.pe.pagaPeBackend.reminder.dto.ReminderResponse;
import com.edu.pe.pagaPeBackend.reminder.dto.WhatsAppMessageRequest;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.service.ReminderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService reminderService;
    private final WhatsAppService whatsAppService;

    @PostMapping
    public ResponseEntity<?> createReminder(@RequestBody ReminderRequest request) {
        try {
            Reminder createdReminder = reminderService.createReminder(request);
            ReminderResponse responseDTO = ReminderMapper.toDTO(createdReminder);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return buildErrorResponse("Error al crear el recordatorio: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ReminderResponse>> getAllReminders() {
        List<Reminder> reminders = reminderService.getAllReminders();
        List<ReminderResponse> responseDTOs = reminders.stream()
                .map(ReminderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReminderById(@PathVariable Long id) {
        try {
            Reminder reminder = reminderService.getReminderById(id);
            ReminderResponse responseDTO = ReminderMapper.toDTO(reminder);
            return ResponseEntity.ok(responseDTO);
        } catch (EntityNotFoundException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para ejecutar el proceso de envío de recordatorios manualmente.
     */
    @PostMapping("/process-now")
    public ResponseEntity<?> triggerProcessingNow() {
        try {
            // Paso 1: Genera la lista de mensajes a enviar
            List<WhatsAppMessageRequest> messagesToSend = reminderService.processPendingReminders();

            // Paso 2: Si se generaron mensajes, se los pasa al servicio de envío
            if (messagesToSend != null && !messagesToSend.isEmpty()) {
                whatsAppService.sendMessages(messagesToSend);
            }

            // Paso 3: Construye la respuesta para el usuario
            Map<String, Object> response = new HashMap<>();
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Proceso manual ejecutado. Se inició el intento de envío de los mensajes generados.");
            response.put("messagesGenerated", messagesToSend.size());
            response.put("data", messagesToSend); // Devuelve data para verificarla

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse("Ocurrió un error durante el procesamiento manual: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("message", message);
        errorResponse.put("status", status.value());
        return new ResponseEntity<>(errorResponse, status);
    }
}
