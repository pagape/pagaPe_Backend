package com.edu.pe.pagaPeBackend.reminder.repository;

import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.model.ResponseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByClientId(Long clientId); // Para listar recordatorios de un cliente específico

    List<Reminder> findByResponseStatus(ResponseStatus responseStatus);

    List<Reminder> findBySendDateTimeBefore(java.time.LocalDateTime date); // Para obtener vencidos
    List<Reminder> findBySendDateTimeBetween(LocalDateTime start, LocalDateTime end);
    //List<Reminder> findBySendDateTimeBeforeAndResponseStatus(LocalDateTime date, ResponseStatus status);
    @Query("SELECT r FROM Reminder r JOIN FETCH r.client WHERE r.sendDateTime < :now AND r.responseStatus = :status")
    List<Reminder> findReadyToSendWithClient(LocalDateTime now, ResponseStatus status);


}
