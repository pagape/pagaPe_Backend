package com.edu.pe.pagaPeBackend.reminder.service;

import com.edu.pe.pagaPeBackend.reminder.dto.reminder.ReminderRequestDTO;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.model.ResponseStatus;

import java.util.List;

public interface ReminderService {
    public List<Reminder> getAll();
    public Reminder createReminder(ReminderRequestDTO request);
    public List<Reminder> getByClient(Long clientId);
    public List<Reminder> getExpired();
    public List<Reminder> getExpireToday();
    public List<Reminder> getExpireInNextDays(int days);
    void updateReminderStatus(Long reminderId, ResponseStatus newStatus);
    List<Reminder> findRemindersReadyToSend();
}
