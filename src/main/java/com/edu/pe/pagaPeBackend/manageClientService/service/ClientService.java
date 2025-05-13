package com.edu.pe.pagaPeBackend.manageClientService.service;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;

import java.util.List;

public interface ClientService {
    public abstract Client createClientService(ClientRequest request);
    public abstract Client getClientById(Long id);

    public abstract Client updateClient(Long id,Client client);
    public abstract void deleteClient(Long id);

}
