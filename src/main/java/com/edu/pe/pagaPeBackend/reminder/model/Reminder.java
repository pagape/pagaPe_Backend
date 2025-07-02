package com.edu.pe.pagaPeBackend.reminder.model;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reminders")
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clientName; //First name + Last name

    @Column(nullable = false)
    private String description; //Reminder description

    @Column(nullable = false)
    private LocalDateTime sendDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResponseStatus responseStatus;

    @Column(nullable = false)
    private String typeService; // "Consultoría", "Mantenimiento", etc.

    @Column(nullable = false)
    private String clientWhatsappPhoneNumber;

    @Column(nullable = false)
    private Boolean isDebtor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Client client;
}
