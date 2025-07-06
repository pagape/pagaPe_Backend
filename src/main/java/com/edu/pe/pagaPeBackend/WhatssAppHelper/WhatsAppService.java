package com.edu.pe.pagaPeBackend.WhatssAppHelper;

import com.edu.pe.pagaPeBackend.reminder.dto.WhatsAppMessageRequest;
import java.util.List;

public interface WhatsAppService {
    void sendMessages(List<WhatsAppMessageRequest> messages);
}