package com.edu.pe.pagaPeBackend.manageClientService.service;

import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceRequest;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientRequest;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;

import java.util.List;

public interface ServiceService {

    public abstract Service createService(ServiceRequest request);

    public abstract List<Service> getAll();
}
