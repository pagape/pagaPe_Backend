package com.edu.pe.pagaPeBackend.reminder.controller;

import com.edu.pe.pagaPeBackend.reminder.dto.reminder.ReminderRequestDTO;
import com.edu.pe.pagaPeBackend.reminder.dto.reminder.ReminderResponseDTO;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.model.ResponseStatus;
import com.edu.pe.pagaPeBackend.reminder.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @PostMapping
    public ResponseEntity<?> createReminder(@RequestBody ReminderRequestDTO dto) {
        try {
            Reminder reminder = new Reminder();
            reminder.setDescription(dto.getDescription());
            reminder.setSendDateTime(dto.getSendDateTime());
            reminder.setTypeService(dto.getTypeService());
            reminder.setClientWhatsappPhoneNumber(dto.getClientWhatsappPhoneNumber());
            reminder.setIsDebtor(dto.getIsDebtor());

            Reminder saved = reminderService.createReminder(reminder, dto.getClientId());

            ReminderResponseDTO response = toResponseDTO(saved);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return errorResponse("Validación fallida", e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return errorResponse("Error del servidor", "No se pudo crear el recordatorio", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllReminders() {
        try {
            List<ReminderResponseDTO> list = reminderService.getAll()
                    .stream().map(this::toResponseDTO).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse("Error del servidor", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<?> getByClient(@PathVariable Long clientId) {
        try {
            List<ReminderResponseDTO> list = reminderService.getByClient(clientId)
                    .stream().map(this::toResponseDTO).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse("Error del servidor", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/expired")
    public ResponseEntity<?> getExpired() {
        try {
            List<ReminderResponseDTO> list = reminderService.getExpired()
                    .stream().map(this::toResponseDTO).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse("Error del servidor", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/today")
    public ResponseEntity<?> getToday() {
        try {
            List<ReminderResponseDTO> list = reminderService.getExpireToday()
                    .stream().map(this::toResponseDTO).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse("Error del servidor", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcoming(@RequestParam(defaultValue = "3") int days) {
        try {
            List<ReminderResponseDTO> list = reminderService.getExpireInNextDays(days)
                    .stream().map(this::toResponseDTO).collect(Collectors.toList());
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse("Error del servidor", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String newStatusStr = request.get("status");
            if (newStatusStr == null) {
                return errorResponse("Petición inválida", "El cuerpo debe contener el 'status'", HttpStatus.BAD_REQUEST);
            }
            ResponseStatus newStatus = ResponseStatus.valueOf(newStatusStr.toUpperCase());
            reminderService.updateReminderStatus(id, newStatus);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return errorResponse("Valor inválido", "El status '" + request.get("status") + "' no es válido.", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return errorResponse("No encontrado", e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // ===========================
    // Helper Methods
    // ===========================

    private ReminderResponseDTO toResponseDTO(Reminder r) {
        ReminderResponseDTO dto = new ReminderResponseDTO();
        dto.setId(r.getId());
        dto.setDescription(r.getDescription());
        dto.setSendDateTime(r.getSendDateTime());
        dto.setResponseStatus(r.getResponseStatus());
        dto.setTypeService(r.getTypeService());
        dto.setClientWhatsappPhoneNumber(r.getClientWhatsappPhoneNumber());
        dto.setIsDebtor(r.getIsDebtor());
        dto.setClientId(r.getClient().getId());
        dto.setClientName(r.getClient().getUserFirstName() + " " + r.getClient().getUserLastName());
        return dto;
    }

    private ResponseEntity<Map<String, Object>> errorResponse(String error, String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", LocalDateTime.now());
        map.put("message", message);
        map.put("error", error);
        return new ResponseEntity<>(map, status);
    }
}
