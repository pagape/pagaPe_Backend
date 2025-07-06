package com.edu.pe.pagaPeBackend.conversationProcess.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name="message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Lob
    @Column(name = "message_content")
    private String messageContent;

    @Enumerated(EnumType.STRING)
    private SenderType sender;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    public enum SenderType {
        CLIENT, SYSTEM
    }

    public enum MessageType {
        TEXT, IMAGE, VIDEO, AUDIO
    }
}
