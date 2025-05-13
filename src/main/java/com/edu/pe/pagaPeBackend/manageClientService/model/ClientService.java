package com.edu.pe.pagaPeBackend.manageClientService.model;

import com.edu.pe.pagaPeBackend.receipt.model.Receipt;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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


    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private State estado;

    private boolean contratoVigente;

    @OneToMany(mappedBy = "clienteServicio", cascade = CascadeType.ALL)
    private List<Receipt> recibos;

    enum State{

        Activo, Suspendido, Finalizado
    }

}
