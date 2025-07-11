package com.edu.pe.pagaPeBackend.reminder.repository;

import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByStatusAndScheduledDateLessThanEqual(Reminder.ReminderStatus status, LocalDate now);
    List<Reminder> findByStatusAndScheduledDate(Reminder.ReminderStatus status, LocalDate date);

}