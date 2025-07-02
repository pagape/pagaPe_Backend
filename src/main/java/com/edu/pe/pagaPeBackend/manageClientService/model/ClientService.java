package com.edu.pe.pagaPeBackend.manageClientService.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="client_service")
@Getter
@Setter
public class ClientService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name="amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name="issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name="due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name="payment_frequency")
    private PaymentFrequency paymentFrequency = PaymentFrequency.FIN_DE_MES;



    @Column(nullable = false)
    private boolean active;
    
    @Column(name = "contrato_vigente", nullable = false)
    private Boolean contratoVigente;

    public boolean getActive() {
        return active;
    }


    public enum PaymentFrequency {
        QUINCENAL, FIN_DE_MES
    }
}
