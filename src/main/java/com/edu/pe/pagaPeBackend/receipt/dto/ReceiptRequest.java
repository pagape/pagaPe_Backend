package com.edu.pe.pagaPeBackend.receipt.dto;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
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
public class ReceiptRequest {
    @NotNull
    private LocalDate issueDate;
    
    @NotNull
    private LocalDate dueDate;
    
    @NotNull
    private BigDecimal amount;
    
    private Client.PaymentStatus estado;
    
    private Long clientServiceId;
    
    private Long clientId;
} 