package com.edu.pe.pagaPeBackend.reminder.model;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Integer daysUntilSend; // 0 = hoy, 1 = mañana, etc.

    @Column(nullable = false)
    private String companyWhatsappNumber; // Número de la empresa que enviará el mensaje

    // --- Campos de Estado y Auditoría ---

    // Sugerencia: 'createdAt' es un nombre estándar para la fecha de registro.
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Sugerencia: 'scheduledSendDate' es más descriptivo para la fecha de envío calculada.
    @Column(nullable = false)
    private LocalDateTime scheduledSendDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReminderStatus status;

    // Guardar los clientes que fueron seleccionados por este recordatorio
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "reminder_selected_clients",
            joinColumns = @JoinColumn(name = "reminder_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    private List<Client> selectedClients;


    public enum ReminderStatus {
        PENDING,   // Creado, esperando fecha de envío
        PROCESSING, // En proceso de selección y envío
        COMPLETED, // Proceso finalizado con éxito
        FAILED     // Ocurrió un error
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = ReminderStatus.PENDING;
        // Calculamos la fecha de envío al momento de crear
        this.scheduledSendDate = this.createdAt.plusDays(this.daysUntilSend);
    }
}