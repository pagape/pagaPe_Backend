package com.edu.pe.pagaPeBackend.manageClientService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client_modifications")
@Getter
@Setter
public class ClientModification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "modification_date", nullable = false)
    private LocalDateTime modificationDate;

    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ModificationAction action;

    @Column(name = "details", length = 500)
    private String details;

    public enum ModificationAction {
        CREATION, UPDATE, DELETION
    }
} 