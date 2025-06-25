package com.edu.pe.pagaPeBackend.reminder.gateway;

public interface WhatsAppGateway {
    void sendMessage(String phoneNumber, String message);
}