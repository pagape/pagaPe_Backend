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

    public abstract Client updateClient(Long id, ClientRequest request);
    public abstract void deleteClient(Long id);

    public abstract void desactivateClient(Long user_id) ;

    public List<Client> getAllClientsByStatus(boolean status);
    
    /**
     * Obtiene todos los clientes
     * @return Lista de todos los clientes
     */
    public abstract List<Client> getAllClients();
    
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
    
    /**
     * Busca clientes por nombre, apellido o teléfono
     * @param query término de búsqueda
     * @return Lista de clientes que coinciden con la búsqueda
     */
    public abstract List<Client> searchClients(String query);
    
    /**
     * Filtra clientes por la primera letra del nombre
     * @param letter letra para filtrar
     * @return Lista de clientes cuyo nombre empieza con la letra especificada
     */
    public abstract List<Client> filterClientsByLetter(String letter);
}
