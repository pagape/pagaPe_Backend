package com.edu.pe.pagaPeBackend.manageClientService.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="services")
@Getter
@Setter
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreServicio;
    private String descripcion;
   // private Double precioBase;


    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<ClientService> clientServiceList;
}
