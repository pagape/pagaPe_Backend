package com.edu.pe.pagaPeBackend.manageClientService.service.Impl;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientModificationMapper;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientHistoryResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.exception.DuplicateClientPhoneException;
import com.edu.pe.pagaPeBackend.manageClientService.exception.InvalidDataException;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientModification;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientModificationRepository;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientRepository;
import com.edu.pe.pagaPeBackend.manageClientService.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository repository;
    
    @Autowired
    private ClientModificationRepository modificationRepository;
    
    // Patrón para validar correo electrónico
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    
    // Patrón para validar número telefónico (entre 7 y 15 dígitos)
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\d{7,15}$");
    
    @Override
    public Client createClientService(ClientRequest request) {
        // Validar datos
        validateClientData(request);
        
        // Validar que no exista un cliente con el mismo nombre y correo
        if (repository.findByUserFirstNameAndUserEmail(request.getUserFirstName(), request.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese nombre y correo.");
        }
        
        // Validar que no exista un cliente con el mismo número de teléfono
        if (request.getUserPhone() != null && !request.getUserPhone().isEmpty() && 
            repository.findByUserPhone(request.getUserPhone()).isPresent()) {
            throw new DuplicateClientPhoneException("Ya existe un cliente con el número de teléfono: " + request.getUserPhone());
        }

        // Crear el cliente
        Client newClient = Client.builder()
                .userFirstName(request.getUserFirstName())
                .userLastName(request.getUserLastName())
                .userEmail(request.getUserEmail())
                .userPhone(request.getUserPhone())
                .createdAt(LocalDateTime.now())
                .build();

        // Guardar el cliente
        Client savedClient = repository.save(newClient);
        
        // Registrar la creación en el historial
        ClientModification modification = ClientModification.builder()
                .client(savedClient)
                .modificationDate(LocalDateTime.now())
                .modifiedBy("system")
                .action(ClientModification.ModificationAction.CREATION)
                .details("Creación inicial del cliente")
                .build();
        
        modificationRepository.save(modification);
        
        return savedClient;
    }

    @Override
    public Client getClientById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public Client updateClient(Long id, Client userRequest, String updatedBy) {
        // Obtener el cliente existente
        Client clienteExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        // Validar datos actualizados
        validateUpdatedClientData(userRequest);
        
        // Validar que no exista otro cliente con el mismo número de teléfono
        if (userRequest.getUserPhone() != null && !userRequest.getUserPhone().isEmpty()) {
            repository.findByUserPhoneAndIdNot(userRequest.getUserPhone(), id)
                .ifPresent(client -> {
                    throw new DuplicateClientPhoneException("Ya existe otro cliente con el número de teléfono: " + userRequest.getUserPhone());
                });
        }

        // Preparar mensaje para el historial
        StringBuilder changesDetails = new StringBuilder("Actualización de campos: ");
        boolean hasChanges = false;
        
        // Actualizar los campos si no son nulos
        if (userRequest.getUserFirstName() != null) {
            if (!userRequest.getUserFirstName().equals(clienteExistente.getUserFirstName())) {
                changesDetails.append("nombre, ");
                hasChanges = true;
            }
            clienteExistente.setUserFirstName(userRequest.getUserFirstName());
        }
        
        if (userRequest.getUserLastName() != null) {
            if (!userRequest.getUserLastName().equals(clienteExistente.getUserLastName())) {
                changesDetails.append("apellido, ");
                hasChanges = true;
            }
            clienteExistente.setUserLastName(userRequest.getUserLastName());
        }
        
        if (userRequest.getUserEmail() != null) {
            if (!userRequest.getUserEmail().equals(clienteExistente.getUserEmail())) {
                changesDetails.append("email, ");
                hasChanges = true;
            }
            clienteExistente.setUserEmail(userRequest.getUserEmail());
        }
        
        if (userRequest.getUserPhone() != null) {
            if (!userRequest.getUserPhone().equals(clienteExistente.getUserPhone())) {
                changesDetails.append("teléfono, ");
                hasChanges = true;
            }
            clienteExistente.setUserPhone(userRequest.getUserPhone());
        }
        
        // Si no hay cambios, no actualizamos
        if (!hasChanges) {
            return clienteExistente;
        }
        
        // Eliminar la última coma y espacio
        String details = changesDetails.toString();
        if (details.endsWith(", ")) {
            details = details.substring(0, details.length() - 2);
        }
        
        // Actualizar información de auditoría
        clienteExistente.setUpdatedAt(LocalDateTime.now());
        clienteExistente.setUpdatedBy(updatedBy);

        // Guardar los cambios
        Client updatedClient = repository.save(clienteExistente);
        
        // Registrar la modificación en el historial
        ClientModification modification = ClientModification.builder()
                .client(updatedClient)
                .modificationDate(LocalDateTime.now())
                .modifiedBy(updatedBy)
                .action(ClientModification.ModificationAction.UPDATE)
                .details(details)
                .build();
        
        modificationRepository.save(modification);
        
        return updatedClient;
    }

    @Override
    public void deleteClient(Long id) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Registrar la eliminación en el historial antes de borrar
        ClientModification modification = ClientModification.builder()
                .client(client)
                .modificationDate(LocalDateTime.now())
                .modifiedBy("system")
                .action(ClientModification.ModificationAction.DELETION)
                .details("Eliminación del cliente")
                .build();
        
        modificationRepository.save(modification);
        
        repository.deleteById(id);
    }
    
    @Override
    public ClientHistoryResponse getClientHistory(Long clientId) {
        Client client = repository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        List<ClientModification> modifications = modificationRepository.findByClientOrderByModificationDateDesc(client);
        
        return ClientModificationMapper.toClientHistoryResponse(client, modifications);
    }
    
    @Override
    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    @Override
    public boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return true; // El teléfono es opcional
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Valida los datos de un cliente nuevo
     */
    private void validateClientData(ClientRequest request) {
        // Validar datos obligatorios
        if (request.getUserFirstName() == null || request.getUserFirstName().isEmpty()) {
            throw new InvalidDataException("El nombre del usuario es obligatorio");
        }
        
        if (request.getUserFirstName().length() > 50) {
            throw new InvalidDataException("El nombre no debe exceder los 50 caracteres");
        }
        
        if (request.getUserLastName() == null || request.getUserLastName().isEmpty()) {
            throw new InvalidDataException("El apellido del usuario es obligatorio");
        }
        
        if (request.getUserLastName().length() > 50) {
            throw new InvalidDataException("El apellido no debe exceder los 50 caracteres");
        }
        
        if (request.getUserEmail() == null || request.getUserEmail().isEmpty()) {
            throw new InvalidDataException("El email del usuario es obligatorio");
        }
        
        if (request.getUserEmail().length() > 50) {
            throw new InvalidDataException("El email no debe exceder los 50 caracteres");
        }
        
        // Validar formato de email
        if (!isValidEmail(request.getUserEmail())) {
            throw new InvalidDataException("El email debe tener un formato válido");
        }
        
        // Validar formato de teléfono
        if (request.getUserPhone() != null && !request.getUserPhone().isEmpty() && !isValidPhone(request.getUserPhone())) {
            throw new InvalidDataException("El teléfono debe tener entre 7 y 15 dígitos");
        }
    }
    
    /**
     * Valida los datos para actualización de cliente
     */
    private void validateUpdatedClientData(Client client) {
        // Validar longitud de campos si no son nulos
        if (client.getUserFirstName() != null) {
            if (client.getUserFirstName().isEmpty()) {
                throw new InvalidDataException("El nombre del usuario no puede estar vacío");
            }
            if (client.getUserFirstName().length() > 50) {
                throw new InvalidDataException("El nombre no debe exceder los 50 caracteres");
            }
        }
        
        if (client.getUserLastName() != null) {
            if (client.getUserLastName().isEmpty()) {
                throw new InvalidDataException("El apellido del usuario no puede estar vacío");
            }
            if (client.getUserLastName().length() > 50) {
                throw new InvalidDataException("El apellido no debe exceder los 50 caracteres");
            }
        }
        
        if (client.getUserEmail() != null) {
            if (client.getUserEmail().isEmpty()) {
                throw new InvalidDataException("El email del usuario no puede estar vacío");
            }
            if (client.getUserEmail().length() > 50) {
                throw new InvalidDataException("El email no debe exceder los 50 caracteres");
            }
            if (!isValidEmail(client.getUserEmail())) {
                throw new InvalidDataException("El email debe tener un formato válido");
            }
        }
        
        if (client.getUserPhone() != null && !client.getUserPhone().isEmpty() && !isValidPhone(client.getUserPhone())) {
            throw new InvalidDataException("El teléfono debe tener entre 7 y 15 dígitos");
        }
    }
}
