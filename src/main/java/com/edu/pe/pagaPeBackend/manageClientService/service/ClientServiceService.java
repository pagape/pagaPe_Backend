package com.edu.pe.pagaPeBackend.manageClientService.service;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceResponse;

import java.util.List;

public interface ClientServiceService {

    public abstract ClientServiceResponse createClientService(ClientServiceRequest request);
    public abstract ClientServiceResponse getCSById(Long id);

    public abstract ClientServiceResponse updateCS(ClientServiceRequest request, Long id);
    public abstract void deleteCS(Long id);
    public abstract boolean existsCSbyid(Long id);

    public abstract List<ClientServiceResponse> getAll();
}
