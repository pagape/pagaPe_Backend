package com.edu.pe.pagaPeBackend.manageClientService.controller;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientMapper;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.exception.DuplicateClientPhoneException;
import com.edu.pe.pagaPeBackend.manageClientService.exception.InvalidDataException;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.service.ClientService;
import com.edu.pe.pagaPeBackend.user.dto.users.UserDTO;
import com.edu.pe.pagaPeBackend.user.dto.users.UserMapper;
import com.edu.pe.pagaPeBackend.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<?> getAllClients() {
        try {
            List<Client> clients = clientService.getAllClients();
            List<ClientResponse> responses = clients.stream()
                .map(ClientMapper::toClientResponse)
                .collect(Collectors.toList());
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al obtener los clientes: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ClientResponse>> getAllClientsByStatus(@PathVariable Boolean status) {

        List<Client> users = clientService.getAllClientsByStatus(status);
        List<ClientResponse> userDTOs = users.stream()
                .map(ClientMapper::toClientResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody ClientRequest clientRequest) {
        try {
            Client client = clientService.createClientService(clientRequest);
            ClientResponse response = ClientMapper.toClientResponse(client);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException | DuplicateClientPhoneException | InvalidDataException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", determineErrorType(e));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al crear el cliente: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        try {
            Client client = clientService.getClientById(id);
            ClientResponse response = ClientMapper.toClientResponse(client);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "Recurso no encontrado");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al obtener el cliente: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest) {
        try {

            // Actualizar el cliente usando el mapper
            Client updatedClient = clientService.updateClient(id, clientRequest);

            ClientResponse response = ClientMapper.toClientResponse(updatedClient);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DuplicateClientPhoneException | InvalidDataException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", determineErrorType(e));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "Recurso no encontrado");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al actualizar el cliente: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            clientService.desactivateClient(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", "Recurso no encontrado");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al eliminar el cliente: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Determina el tipo de error basado en la excepción
     */
    private String determineErrorType(Exception e) {
        if (e instanceof DuplicateClientPhoneException) {
            return "Número de teléfono duplicado";
        } else if (e instanceof InvalidDataException) {
            return "Validación fallida";
        } else {
            return "Error de validación";
        }
    }
} 