package com.edu.pe.pagaPeBackend.manageClientService.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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


    @Column(nullable = false)
    private boolean active;

    @Column(name = "createdAt")
    private LocalDate created;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<ClientService> serviciosContratados;


    public boolean getActive() {
        return active;
    }
}
