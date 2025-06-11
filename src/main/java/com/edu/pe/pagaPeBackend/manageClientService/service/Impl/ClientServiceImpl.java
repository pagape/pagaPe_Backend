package com.edu.pe.pagaPeBackend.manageClientService.service.Impl;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientMapper;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.exception.DuplicateClientPhoneException;
import com.edu.pe.pagaPeBackend.manageClientService.exception.InvalidDataException;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
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

        // Crear el cliente utilizando el mapper para incluir todos los campos
        Client newClient = ClientMapper.toClient(request);

        // Guardar el cliente
        Client savedClient = repository.save(newClient);
        
        return savedClient;
    }

    @Override
    public Client getClientById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public Client updateClient(Long id, ClientRequest userRequest) {
        // Obtener el cliente existente
        Client clienteExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
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
        


        if (userRequest.getActive()!= clienteExistente.getActive()) {
            clienteExistente.setActive(userRequest.getActive());
            hasChanges = true;
        }


        if (!hasChanges) {
            return clienteExistente;
        }
        
        // Guardar los cambios
        Client updatedClient = repository.save(clienteExistente);

        System.out.println(updatedClient);
        return updatedClient;


    }

    @Override
    public void deleteClient(Long id) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        repository.deleteById(id);
    }
    
    @Override
    public List<Client> getAllClients() {
        return repository.findAll();
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
    
    private void validateClientData(ClientRequest request) {
        if (request == null) {
            throw new InvalidDataException("Los datos del cliente no pueden ser nulos");
        }

        if (request.getUserFirstName() == null || request.getUserFirstName().trim().isEmpty()) {
            throw new InvalidDataException("El nombre del cliente es obligatorio");
        }

        if (request.getUserLastName() == null || request.getUserLastName().trim().isEmpty()) {
            throw new InvalidDataException("El apellido del cliente es obligatorio");
        }

        if (request.getUserEmail() == null || request.getUserEmail().trim().isEmpty()) {
            throw new InvalidDataException("El correo electrónico es obligatorio");
        }
        
        if (!isValidEmail(request.getUserEmail())) {
            throw new InvalidDataException("El formato del correo electrónico no es válido");
        }

        if (request.getUserPhone() != null && !request.getUserPhone().isEmpty() && !isValidPhone(request.getUserPhone())) {
            throw new InvalidDataException("El formato del número de teléfono no es válido");
        }
        

    }
    
    private void validateUpdatedClientData(Client client) {
        if (client == null) {
            throw new InvalidDataException("Los datos del cliente no pueden ser nulos");
        }

        if (client.getUserFirstName() == null || client.getUserFirstName().trim().isEmpty()) {
            throw new InvalidDataException("El nombre del cliente es obligatorio");
        }

        if (client.getUserLastName() == null || client.getUserLastName().trim().isEmpty()) {
            throw new InvalidDataException("El apellido del cliente es obligatorio");
        }

        if (client.getUserEmail() == null || client.getUserEmail().trim().isEmpty()) {
            throw new InvalidDataException("El correo electrónico es obligatorio");
        }
        
        if (!isValidEmail(client.getUserEmail())) {
            throw new InvalidDataException("El formato del correo electrónico no es válido");
        }

        if (client.getUserPhone() != null && !client.getUserPhone().isEmpty() && !isValidPhone(client.getUserPhone())) {
            throw new InvalidDataException("El formato del número de teléfono no es válido");
        }
    }
}
