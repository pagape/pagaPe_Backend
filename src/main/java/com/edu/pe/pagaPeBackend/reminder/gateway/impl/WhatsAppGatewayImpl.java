package com.edu.pe.pagaPeBackend.reminder.gateway.impl;

import com.edu.pe.pagaPeBackend.reminder.gateway.WhatsAppGateway;
import org.springframework.stereotype.Component;

@Component
public class WhatsAppGatewayImpl implements WhatsAppGateway {
    @Override
    public void sendMessage(String phoneNumber, String message) {
        // Por ahora, solo imprimirá en la consola para simular el envío.
        System.out.println("=============================================");
        System.out.println("== SIMULANDO ENVÍO DE WHATSAPP ==");
        System.out.println("== Destinatario: " + phoneNumber);
        System.out.println("== Mensaje: " + message);
        System.out.println("=============================================");
        // En el futuro, aquí iría el código real para conectar con Twilio, Meta, etc.
    }
}
