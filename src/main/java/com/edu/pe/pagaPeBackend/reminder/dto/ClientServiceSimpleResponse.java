package com.edu.pe.pagaPeBackend.reminder.dto;
import com.edu.pe.pagaPeBackend.manageClientService.dto.client.ClientSimpleResponse;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ClientServiceSimpleResponse {
    private Long id;
    private BigDecimal amount;
    private LocalDate dueDate;
    private ClientSimpleResponse client;
}
