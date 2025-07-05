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
            
            // Escenario 4: Lista vacía - mostrar mensaje de "sin registros"
            if (clients.isEmpty()) {
                Map<String, Object> emptyResponse = new HashMap<>();
                emptyResponse.put("timestamp", java.time.LocalDateTime.now());
                emptyResponse.put("message", "No hay clientes registrados");
                emptyResponse.put("clients", clients);
                emptyResponse.put("totalClients", 0);
                return new ResponseEntity<>(emptyResponse, HttpStatus.OK);
            }
            
            List<ClientResponse> responses = clients.stream()
                .map(ClientMapper::toClientResponse)
                .collect(Collectors.toList());
            
            // Respuesta exitosa con datos
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("timestamp", java.time.LocalDateTime.now());
            successResponse.put("message", "Clientes obtenidos exitosamente");
            successResponse.put("clients", responses);
            successResponse.put("totalClients", responses.size());
            
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
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

    @GetMapping("/search")
    public ResponseEntity<?> searchClients(@RequestParam(required = false) String query) {
        try {
            List<Client> clients = clientService.searchClients(query);
            
            // Manejo de búsqueda sin resultados
            if (clients.isEmpty()) {
                Map<String, Object> emptyResponse = new HashMap<>();
                emptyResponse.put("timestamp", java.time.LocalDateTime.now());
                emptyResponse.put("message", query == null || query.trim().isEmpty() ? 
                    "No hay clientes registrados" : 
                    "No se encontraron clientes que coincidan con: " + query);
                emptyResponse.put("clients", clients);
                emptyResponse.put("totalClients", 0);
                emptyResponse.put("searchQuery", query);
                return new ResponseEntity<>(emptyResponse, HttpStatus.OK);
            }
            
            List<ClientResponse> responses = clients.stream()
                .map(ClientMapper::toClientResponse)
                .collect(Collectors.toList());
            
            // Respuesta exitosa con resultados
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("timestamp", java.time.LocalDateTime.now());
            successResponse.put("message", "Búsqueda completada exitosamente");
            successResponse.put("clients", responses);
            successResponse.put("totalClients", responses.size());
            successResponse.put("searchQuery", query);
            
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al buscar clientes: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterClientsByLetter(@RequestParam String letter) {
        try {
            List<Client> clients = clientService.filterClientsByLetter(letter);
            
            // Manejo de filtro sin resultados
            if (clients.isEmpty()) {
                Map<String, Object> emptyResponse = new HashMap<>();
                emptyResponse.put("timestamp", java.time.LocalDateTime.now());
                emptyResponse.put("message", "No se encontraron clientes que empiecen con la letra: " + letter.toUpperCase());
                emptyResponse.put("clients", clients);
                emptyResponse.put("totalClients", 0);
                emptyResponse.put("filterLetter", letter.toUpperCase());
                return new ResponseEntity<>(emptyResponse, HttpStatus.OK);
            }
            
            List<ClientResponse> responses = clients.stream()
                .map(ClientMapper::toClientResponse)
                .collect(Collectors.toList());
            
            // Respuesta exitosa con resultados filtrados
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("timestamp", java.time.LocalDateTime.now());
            successResponse.put("message", "Filtrado completado exitosamente");
            successResponse.put("clients", responses);
            successResponse.put("totalClients", responses.size());
            successResponse.put("filterLetter", letter.toUpperCase());
            
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", java.time.LocalDateTime.now());
            errorResponse.put("message", "Error al filtrar clientes: " + e.getMessage());
            errorResponse.put("error", "Error del servidor");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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