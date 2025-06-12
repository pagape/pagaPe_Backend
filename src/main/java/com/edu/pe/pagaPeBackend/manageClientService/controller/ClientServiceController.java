package com.edu.pe.pagaPeBackend.manageClientService.controller;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceMapper;
import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.exception.DuplicateClientPhoneException;
import com.edu.pe.pagaPeBackend.manageClientService.exception.InvalidDataException;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import com.edu.pe.pagaPeBackend.manageClientService.service.ClientService;
import com.edu.pe.pagaPeBackend.manageClientService.service.ClientServiceService;
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
@RequestMapping("/api/client-service")
public class ClientServiceController {

    @Autowired
    private ClientServiceService service;
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<ClientServiceResponse> s = service.getAll();
            return new ResponseEntity<>(s, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al obtener los servicios: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientServiceResponse> getById(@PathVariable Long id) {
        ClientServiceResponse response = service.getCSById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ClientServiceRequest request) {
        try {
            ClientServiceResponse s = service.createClientService(request);
            return new ResponseEntity<>(s, HttpStatus.CREATED);
        } catch (IllegalArgumentException | DuplicateClientPhoneException | InvalidDataException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al crear el cliente-servicio: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientServiceResponse> update(
            @PathVariable Long id,
            @RequestBody ClientServiceRequest request
    ) {
        ClientServiceResponse response = service.updateCS(request,id);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCS(id);
        return ResponseEntity.noContent().build();
    }
}
