package com.edu.pe.pagaPeBackend.reminder.repository;

import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.model.WhatsappNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByService(Service service);
    List<Reminder> findByWhatsappNumber(WhatsappNumber whatsappNumber);
    List<Reminder> findByScheduledDateBetween(LocalDateTime start, LocalDateTime end);
    List<Reminder> findByServiceAndIsDebtor(Service service, boolean isDebtor);
    
    @Query("SELECT r FROM Reminder r JOIN r.clientServices cs WHERE cs = :clientService")
    List<Reminder> findByClientService(@Param("clientService") ClientService clientService);
    
    @Query("SELECT r FROM Reminder r JOIN r.clientServices cs WHERE cs = :clientService AND r.isDebtor = :isDebtor")
    List<Reminder> findByClientServiceAndIsDebtor(@Param("clientService") ClientService clientService, @Param("isDebtor") boolean isDebtor);
} 