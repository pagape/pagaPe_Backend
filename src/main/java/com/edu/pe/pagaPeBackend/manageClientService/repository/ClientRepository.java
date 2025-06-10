package com.edu.pe.pagaPeBackend.manageClientService.repository;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    Optional<Client> findByUserFirstNameAndUserEmail(String name, String userEmail);
    
    Optional<Client> findByUserPhone(String userPhone);
    
    Optional<Client> findByUserPhoneAndIdNot(String userPhone, Long clientId);
}
