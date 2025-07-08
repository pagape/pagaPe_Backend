package com.edu.pe.pagaPeBackend.conversationProcess.model;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name="conversation")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ConversationStatus status;



    // Relación con el cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Relación con el recordatorio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reminder_id", nullable = false)
    private Reminder reminder;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "finish_at")
    private LocalDateTime finishAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentiment_label")
    private SentimentLabel sentimentLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_finished")
    private StatusFinish statusFinished;

    @Column(name = "sentiment_score")
    private Double sentimentScore;


    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();


    public enum SentimentLabel {
        ENOJADO,
        SATISFECHO,
        COMPLETAMENTE_SATISFECHO,

        INSATISFECHO

    }

    public enum StatusFinish {
        POSITIVO,
        NEGATIVO,
        NEUTRAL
    }

    public enum ConversationStatus {
        ABIERTA,
        CERRADA
    }
}