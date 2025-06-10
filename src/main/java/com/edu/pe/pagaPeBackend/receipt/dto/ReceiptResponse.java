package com.edu.pe.pagaPeBackend.receipt.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientResponse;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
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
public class ReceiptResponse {
    private Long id;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private BigDecimal amount;
    private Client.PaymentStatus estado;
    private ClientServiceResponse clientService;
    private ClientResponse client;
} 