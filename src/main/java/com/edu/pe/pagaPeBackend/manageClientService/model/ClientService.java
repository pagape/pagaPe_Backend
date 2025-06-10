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

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    
    @Column(name = "estado")
    private Byte estado; // tinyint in database
    
    @Column(name = "contrato_vigente", nullable = false)
    private boolean contratoVigente;

    @OneToMany(mappedBy = "clienteServicio", cascade = CascadeType.ALL)
    private List<Receipt> recibos;
    
    // Constants for estado values
    public static final byte ESTADO_ACTIVO = 1;
    public static final byte ESTADO_SUSPENDIDO = 2;
    public static final byte ESTADO_CANCELADO = 3;
}
