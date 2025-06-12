package com.edu.pe.pagaPeBackend.manageClientService.service.Impl;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceMapper;
import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceMapper;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientRepository;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientServiceRepository;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ServiceRepository;
import com.edu.pe.pagaPeBackend.manageClientService.service.ClientServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        Optional<Client> client= clientRepository.findById(request.getClientId());

        Optional<com.edu.pe.pagaPeBackend.manageClientService.model.Service>service= serviceRepository.findById(request.getServiceId());
         ClientService newS = ClientServiceMapper.toEntity(request, client.get(), service.get());

         repository.save(newS);

         return ClientServiceMapper.toResponse(newS);

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
