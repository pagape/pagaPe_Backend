package com.edu.pe.pagaPeBackend.conversationProcess.repository;


import com.edu.pe.pagaPeBackend.conversationProcess.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByClientId(Long clientId);


    Optional<Conversation> findFirstByClientIdAndStatus(Long clientId, Conversation.ConversationStatus status);



}
