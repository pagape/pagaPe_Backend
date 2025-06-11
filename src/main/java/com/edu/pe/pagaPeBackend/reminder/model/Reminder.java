package com.edu.pe.pagaPeBackend.reminder.model;

import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="reminders")
@Getter
@Setter
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="title", nullable = false, length = 100)
    private String title;
    
    @Column(name="description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name="scheduled_date", nullable = false)
    private LocalDateTime scheduledDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name="response_status")
    private ResponseStatus responseStatus = ResponseStatus.PENDIENTE;
    
    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;
    
    @ManyToOne
    @JoinColumn(name = "whatsapp_number_id", nullable = false)
    private WhatsappNumber whatsappNumber;
    
    @ManyToMany
    @JoinTable(
        name = "reminder_client_service",
        joinColumns = @JoinColumn(name = "reminder_id"),
        inverseJoinColumns = @JoinColumn(name = "client_service_id")
    )
    private List<ClientService> clientServices;
    
    @Column(name="is_debtor", nullable = false)
    private boolean isDebtor = false;
    
    @Column(name="days_in_advance")
    private Integer daysInAdvance;
    
    public enum ResponseStatus {
        PENDIENTE, ENVIADO, FALLIDO, ENTREGADO, LEIDO, RESPONDIDO
    }
} 