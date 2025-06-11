package com.edu.pe.pagaPeBackend.manageClientService.service.Impl;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientMapper;
import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceMapper;
import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceRequest;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientRepository;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ServiceRepository;
import com.edu.pe.pagaPeBackend.manageClientService.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SServiceImpl implements ServiceService {

    @Autowired
    private ServiceRepository repository;

    @Override
    public com.edu.pe.pagaPeBackend.manageClientService.model.Service createService(ServiceRequest request) {
        com.edu.pe.pagaPeBackend.manageClientService.model.Service newS = ServiceMapper.toEntity(request);

        return repository.save(newS);
    }

    @Override
    public List<com.edu.pe.pagaPeBackend.manageClientService.model.Service> getAll() {
        return repository.findAll();
    }


}
