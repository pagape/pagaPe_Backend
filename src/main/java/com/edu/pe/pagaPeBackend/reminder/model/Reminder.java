package com.edu.pe.pagaPeBackend.reminder.model;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reminderName;

    @Column(length = 1024)
    private String description;

    // --- Filtros para la Selección de Clientes ---

    @Column(nullable = false)
    private Boolean debtorFilter; // true: solo deudores, false: solo no deudores

    // Para filtrar por tipo de servicio. Puede ser nulo para aplicar a TODOS los servicios.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id_filter")
    private Service serviceFilter;

    // --- Configuración de Envío ---

    @Column(nullable = false)
    private Integer relativeDays;

    @Column(nullable = false)
    private LocalDate scheduledDate;

    @Column(nullable = false)
    private String companyWhatsappNumber; // Número de la empresa que enviará el mensaje

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReminderStatus status = ReminderStatus.PENDING;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "reminder_selected_clients",
            joinColumns = @JoinColumn(name = "reminder_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private List<Client> selectedClients;


    public enum ReminderStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED
    }
}