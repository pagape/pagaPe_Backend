package com.edu.pe.pagaPeBackend.manageClientService.dto;

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
public class ClientServiceRequest {

    private Long clientId;

    private Long serviceId;

    private BigDecimal amount;


    private LocalDate issueDate;


    private LocalDate dueDate;

    private boolean active;

    public boolean getActive(){

        return active;
    }

    private ClientService.PaymentFrequency paymentFrequency;
}
