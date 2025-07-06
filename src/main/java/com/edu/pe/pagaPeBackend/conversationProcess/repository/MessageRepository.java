package com.edu.pe.pagaPeBackend.conversationProcess.repository;



import com.edu.pe.pagaPeBackend.conversationProcess.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversation_Id(Long conversation_id);

}
