package com.edu.pe.pagaPeBackend.manageClientService.repository;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientModification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientModificationRepository extends JpaRepository<ClientModification, Long> {
    
    List<ClientModification> findByClientOrderByModificationDateDesc(Client client);
    
    List<ClientModification> findByClientIdOrderByModificationDateDesc(Long clientId);
} 