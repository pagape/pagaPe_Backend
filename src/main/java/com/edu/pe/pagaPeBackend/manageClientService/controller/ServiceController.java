package com.edu.pe.pagaPeBackend.manageClientService.controller;


import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceMapper;
import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceResponse;

import com.edu.pe.pagaPeBackend.manageClientService.exception.DuplicateClientPhoneException;
import com.edu.pe.pagaPeBackend.manageClientService.exception.InvalidDataException;

import com.edu.pe.pagaPeBackend.manageClientService.model.Service;

import com.edu.pe.pagaPeBackend.manageClientService.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServiceService service;
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Service> s = service.getAll();
            List<ServiceResponse> responses = s.stream()
                    .map(ServiceMapper::toResponse)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al obtener los servicios: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ServiceRequest request) {
        try {
            Service s = service.createService(request);
            ServiceResponse response = ServiceMapper.toResponse(s);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException | DuplicateClientPhoneException | InvalidDataException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al crear el cliente: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
