package com.edu.pe.pagaPeBackend.manageClientService.repository;

import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientServiceRepository extends JpaRepository< ClientService,Long> {
}
