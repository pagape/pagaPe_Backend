package com.edu.pe.pagaPeBackend.reminder.gateway;

public interface WhatsAppGateway {
    void sendMessage(String phoneNumber, String param1_clientName, String param2_amount, String param3_issueDate, String param4_dueDate);
}