package com.edu.pe.pagaPeBackend.manageClientService.service.Impl;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientMapper;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));


        com.edu.pe.pagaPeBackend.manageClientService.model.Service service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));


        if (request.getDueDate().isBefore(request.getIssueDate()) || request.getDueDate().isEqual(request.getIssueDate())) {
            throw new RuntimeException("La fecha de vencimiento no puede ser anterior o igual a la fecha de emisión");
        }


        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero");
        }

        Optional<ClientService> overlapping = repository.findOverlappingService(
                request.getClientId(),
                request.getServiceId(),
                request.getIssueDate(),
                request.getDueDate()
        );

        if (overlapping.isPresent()) {
            throw new RuntimeException("Ya existe un contrato activo para este cliente y servicio en el periodo indicado.");
        }

        ClientService newS = ClientServiceMapper.toEntity(request, client, service);

         repository.save(newS);

         return ClientServiceMapper.toResponse(newS);

    }

    @Override
    public ClientServiceResponse getCSById(Long id) {

        ClientService clientService = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("relacion no encontrada"));


        return ClientServiceMapper.toResponse(clientService);
    }

    @Override
    public ClientServiceResponse updateCS(ClientServiceRequest request, Long id) {
        ClientService existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada para actualizar"));

        Long clientId = (request.getClientId() != null) ? request.getClientId() : existing.getClient().getId();
        Long serviceId = (request.getServiceId() != null) ? request.getServiceId() : existing.getService().getId();
        LocalDate issueDate = (request.getIssueDate() != null) ? request.getIssueDate() : existing.getIssueDate();
        LocalDate dueDate = (request.getDueDate() != null) ? request.getDueDate() : existing.getDueDate();


        if (dueDate.isBefore(issueDate)) {
            throw new RuntimeException("La fecha de vencimiento no puede ser anterior a la fecha de emisión");
        }


        Optional<ClientService> overlapping = repository.findOverlappingService(
                clientId, serviceId, issueDate, dueDate
        ).filter(cs -> !cs.getId().equals(id));

        if (overlapping.isPresent()) {
            throw new RuntimeException("Ya existe otro contrato activo para este cliente y servicio en el periodo indicado.");
        }


        if (request.getAmount() != null) {
            if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("El monto debe ser mayor a cero");
            }
            existing.setAmount(request.getAmount());
        }

        if (request.getIssueDate() != null) {
            existing.setIssueDate(request.getIssueDate());
        }

        if (request.getDueDate() != null) {
            if (existing.getIssueDate() != null && request.getDueDate().isBefore(existing.getIssueDate())) {
                throw new RuntimeException("La fecha de vencimiento no puede ser anterior a la de emisión");
            }
            existing.setDueDate(request.getDueDate());
        }

        if (request.getPaymentFrequency() != null) {
            existing.setPaymentFrequency(request.getPaymentFrequency());
        }


        repository.save(existing);
        return ClientServiceMapper.toResponse(existing);
    }

    @Override
    public void deleteCS(Long id) {

        ClientService clientService = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada para eliminar"));

        clientService.setActive(false);
        repository.save(clientService);
    }

    @Override
    public boolean existsCSbyid(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<ClientServiceResponse> getAll() {

        List<ClientService> clientServiceList= repository.findAll();

        return clientServiceList.stream()
                .map(ClientServiceMapper::toResponse)
                .collect(Collectors.toList());

    }
}
