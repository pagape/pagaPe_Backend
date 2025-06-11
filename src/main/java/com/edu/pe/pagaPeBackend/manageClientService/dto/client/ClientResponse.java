package com.edu.pe.pagaPeBackend.manageClientService.dto.client;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPhone;
    private BigDecimal amount;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private Long clientServiceId;
}
