package com.edu.pe.pagaPeBackend.conversationProcess.service.Impl;



import com.edu.pe.pagaPeBackend.conversationProcess.dto.*;
import com.edu.pe.pagaPeBackend.conversationProcess.model.Conversation;
import com.edu.pe.pagaPeBackend.conversationProcess.model.Message;
import com.edu.pe.pagaPeBackend.conversationProcess.repository.ConversationRepository;
import com.edu.pe.pagaPeBackend.conversationProcess.repository.MessageRepository;
import com.edu.pe.pagaPeBackend.conversationProcess.service.ConversationService;
import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.repository.ClientRepository;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ClientRepository clientRepository;
    private final ReminderRepository reminderRepository;
    private final MessageRepository messageRepository;

    @Override
    public ConversationResponse createConversation(ConversationRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Reminder reminder = reminderRepository.findById(request.getReminderId())
                .orElseThrow(() -> new RuntimeException("Reminder no encontrado"));

        Conversation conversation = ConversationMapper.toEntity(request, client, reminder);
        conversationRepository.save(conversation);

        return ConversationMapper.toResponse(conversation);
    }

    @Override
    public ConversationResponse getActiveConversationByPhoneNumber(String phoneNumber) {

            Client client = clientRepository.findByUserPhone(phoneNumber)
                    .orElse(null);

            if (client == null) {
                return null;
            }

            Conversation conversation = conversationRepository
                    .findFirstByClientIdAndStatus(
                            client.getId(),
                            Conversation.ConversationStatus.ABIERTA
                    )
                    .orElse(null);

            if (conversation == null) {
                return null;
            }

            return ConversationMapper.toResponse(conversation);
    }

    @Override
    public List<ConversationResponse> getAllConversations() {
        List<Conversation> conversations = conversationRepository.findAll();
        return conversations.stream()
                .map(ConversationMapper::toResponse)
                .collect(Collectors.toList());
    }


    @Override
    public MessageResponse addMessageToConversation(Long conversationId, MessageRequest messageRequest) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation no encontrada"));

        if (conversation.getStatus() == Conversation.ConversationStatus.CERRADA) {
            throw new RuntimeException("No se puede agregar mensaje a una conversación cerrada");
        }

        Message message = MessageMapper.toEntity(messageRequest, conversation);
        messageRepository.save(message);

        // también actualizas la lista en memoria
        conversation.getMessages().add(message);

        return MessageMapper.toResponse(message);
    }



    @Override
    public void closeConversation(Long conversationId,  CloseConversationRequest request) {

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation no encontrada"));

        conversation.setStatus(Conversation.ConversationStatus.CERRADA);
        conversation.setFinishAt(java.time.LocalDateTime.now());

        if (request.getSentimentLabel() != null) {
            conversation.setSentimentLabel(
                  request.getSentimentLabel()
            );
        }

        conversation.setSentimentScore(request.getSentimentScore());
        conversation.setStatusFinished(request.getStatusFinish());

        conversationRepository.save(conversation);

    }

    @Override
    public ConversationMetricsResponse getSentimentMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        List<Conversation> conversations;

        if (startDate != null && endDate != null) {
            conversations = conversationRepository.findAllByFinishAtBetween(startDate, endDate);
        } else {
            conversations = conversationRepository.findAll();
        }


        Map<String, Long> counts = conversations.stream()
                .filter(c -> c.getSentimentLabel() != null)
                .collect(Collectors.groupingBy(
                        c -> c.getSentimentLabel().name(),
                        Collectors.counting()
                ));
        for (Conversation.SentimentLabel label : Conversation.SentimentLabel.values()) {
            counts.putIfAbsent(label.name(), 0L);
        }

        Long total = counts.values().stream().mapToLong(Long::longValue).sum();

        Map<String, Double> percentages = counts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> total > 0
                                ? (e.getValue() * 100.0) / total
                                : 0.0
                ));

        return ConversationMetricsResponse.builder()
                .totalConversations(total)
                .distribution(counts)
                .percentages(percentages)
                .build();
    }

    @Override
    public ConversationMetricsResponse getStatusFinishMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        List<Conversation> conversations;

        if (startDate != null && endDate != null) {
            conversations = conversationRepository.findAllByFinishAtBetween(startDate, endDate);
        } else {
            conversations = conversationRepository.findAll();
        }


        Map<String, Long> counts = conversations.stream()
                .filter(c -> c.getStatusFinished() != null)
                .collect(Collectors.groupingBy(
                        c -> c.getStatusFinished().name(),
                        Collectors.counting()
                ));



        Long total = counts.values().stream().mapToLong(Long::longValue).sum();

        Map<String, Double> percentages = counts.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> total > 0
                                ? (e.getValue() * 100.0) / total
                                : 0.0
                ));

        return ConversationMetricsResponse.builder()
                .totalConversations(total)
                .distribution(counts)
                .percentages(percentages)
                .build();
    }
}
