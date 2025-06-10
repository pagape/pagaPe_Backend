package com.edu.pe.pagaPeBackend.manageClientService.service;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientHistoryResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;

import java.util.List;

public interface ClientService {
    public abstract Client createClientService(ClientRequest request);
    public abstract Client getClientById(Long id);

    public abstract Client updateClient(Long id, ClientRequest request, String updatedBy);
    public abstract void deleteClient(Long id);
    
    /**
     * Obtiene el historial de modificaciones de un cliente
     * @param clientId ID del cliente
     * @return Historial de modificaciones
     */
    public abstract ClientHistoryResponse getClientHistory(Long clientId);
    
    /**
     * Valida el formato del correo electrónico
     * @param email correo a validar
     * @return true si el formato es válido
     */
    public abstract boolean isValidEmail(String email);
    
    /**
     * Valida el formato del número telefónico
     * @param phone número a validar
     * @return true si el formato es válido
     */
    public abstract boolean isValidPhone(String phone);
}
