package com.edu.pe.pagaPeBackend.receipt.model;

import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="clients")
@Getter
@Setter
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDate issueDate;
    @Column(nullable = false)
    private LocalDate dueDate;
    @Column(nullable = false)
    private Float amount;

    @Enumerated(EnumType.STRING)
    private State estado;

    @ManyToOne
    @JoinColumn(name = "client_service_id")
    private ClientService clienteServicio;

    enum State{

        PENDIENTE, PAGADO, VENCIDO

    }
}
