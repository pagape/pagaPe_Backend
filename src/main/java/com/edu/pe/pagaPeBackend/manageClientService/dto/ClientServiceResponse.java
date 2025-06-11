package com.edu.pe.pagaPeBackend.manageClientService.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientServiceResponse {
    private Long id;
    private ClientResponse client;
    private ServiceResponse service;

    private BigDecimal amount;


    private LocalDate issueDate;


    private LocalDate dueDate;

    private ClientService.PaymentFrequency paymentFrequency;

    private boolean estado;

    private boolean contratoVigente;
}
