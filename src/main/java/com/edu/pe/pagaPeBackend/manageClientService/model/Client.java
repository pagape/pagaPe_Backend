package com.edu.pe.pagaPeBackend.manageClientService.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="clients")
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_first_name", nullable = false, length = 50)
    private String userFirstName;

    @Column(name="user_last_name", nullable = false, length = 50)
    private String userLastName;

    @Column(name="user_email", nullable = false, length = 50)
    private String userEmail;

    @Column(name="user_phone", nullable = true, length = 50)
    private String userPhone;
    
    @Column(name="amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name="issue_date", nullable = false)
    private LocalDate issueDate;
    
    @Column(name="due_date", nullable = false)
    private LocalDate dueDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name="payment_frequency")
    private PaymentFrequency paymentFrequency = PaymentFrequency.FIN_DE_MES;
    
    @ManyToOne
    @JoinColumn(name = "client_service_id")
    private ClientService clientService;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ClientService> serviciosContratados;
    
    public enum PaymentFrequency {
        QUINCENAL, FIN_DE_MES
    }
}
