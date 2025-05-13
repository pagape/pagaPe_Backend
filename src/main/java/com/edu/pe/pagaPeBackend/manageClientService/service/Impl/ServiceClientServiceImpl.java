package com.edu.pe.pagaPeBackend.manageClientService.service.Impl;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientRepository;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientServiceRepository;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ServiceRepository;
import com.edu.pe.pagaPeBackend.manageClientService.service.ClientServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ServiceClientServiceImpl implements ClientServiceService {


    @Autowired
    private ClientServiceRepository repository;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public ClientServiceResponse createClientService(ClientServiceRequest request) {
        return null;
    }

    @Override
    public ClientServiceResponse getCSById(Long id) {
        return null;
    }

    @Override
    public ClientServiceResponse updateCS(ClientService user) {
        return null;
    }

    @Override
    public void deleteCS(Long id) {

    }

    @Override
    public boolean existsCSbyid(Long id) {
        return false;
    }

    @Override
    public List<ClientServiceResponse> getAll() {
        return null;
    }
}
