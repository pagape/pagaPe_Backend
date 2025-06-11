package com.edu.pe.pagaPeBackend.reminder.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="whatsapp_numbers")
@Getter
@Setter
public class WhatsappNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="number", nullable = false, length = 20, unique = true)
    private String number;
    
    @Column(name="alias", length = 100)
    private String alias;
    
    @Column(name="is_active", nullable = false)
    private boolean isActive = true;
} 