package com.edu.pe.pagaPeBackend.reminder.repository;

import com.edu.pe.pagaPeBackend.reminder.model.WhatsappNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WhatsappNumberRepository extends JpaRepository<WhatsappNumber, Long> {
    Optional<WhatsappNumber> findByNumber(String number);
    Optional<WhatsappNumber> findByNumberAndIdNot(String number, Long id);
} 