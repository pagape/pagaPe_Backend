package com.edu.pe.pagaPeBackend.reminder.service;

import com.edu.pe.pagaPeBackend.reminder.model.Reminder;

import java.util.List;

public interface ReminderService {
    public List<Reminder> getAll();
    public Reminder createReminder(Reminder reminder, Long clientId);
    public List<Reminder> getByClient(Long clientId);
    public List<Reminder> getExpired();
    public List<Reminder> getExpireToday();
    public List<Reminder> getExpireInNextDays(int days);



}
