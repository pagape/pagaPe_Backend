package com.edu.pe.pagaPeBackend.manageClientService.service.Impl;

import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientRepository;
import com.edu.pe.pagaPeBackend.manageClientService.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository repository;
    @Override
    public Client createClientService(ClientRequest request) {

        if (repository.findByUserFirstNameAndUserEmail(request.getUserFirstName(), request.getUserEmail()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese nombre y correo .");
        }

        Client newClient = Client.builder()
                .userFirstName(request.getUserFirstName())
                .userLastName(request.getUserLastName())
                .userEmail(request.getUserEmail())
                .userPhone(request.getUserPhone())
                .build();


        return repository.save(newClient);

    }

    @Override
    public Client getClientById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("cliente no encontrado"));
}

    @Override
    public Client updateClient(Long id,Client userRequest) {

        Client clienteExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("cliente  no encontrado"));


        if (userRequest.getUserFirstName() != null ) {
            clienteExistente.setUserFirstName(userRequest.getUserFirstName());
        }
        if (userRequest.getUserLastName() != null ) {
            clienteExistente.setUserLastName(userRequest.getUserLastName());
        }
        if (userRequest.getUserEmail() != null ) {
           clienteExistente.setUserEmail(userRequest.getUserEmail());
        }
        if (userRequest.getUserPhone() != null) {
            clienteExistente.setUserPhone(userRequest.getUserPhone());

        }

        return repository.save(clienteExistente);
    }

    @Override
    public void deleteClient(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Historial de proceso no encontrado");
        }

        repository.deleteById(id);

    }
}
