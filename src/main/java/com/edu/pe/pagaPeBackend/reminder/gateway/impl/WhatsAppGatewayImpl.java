package com.edu.pe.pagaPeBackend.reminder.gateway.impl;

import com.edu.pe.pagaPeBackend.reminder.gateway.WhatsAppGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Component
public class WhatsAppGatewayImpl implements WhatsAppGateway {

    private final RestTemplate restTemplate;

    @Value("${whatsapp.api.url}")
    private String apiUrl;

    @Value("${whatsapp.bearer.token}")
    private String bearerToken;

    @Autowired
    public WhatsAppGatewayImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendMessage(String phoneNumber, String param1_clientName, String param2_amount, String param3_issueDate, String param4_dueDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // OJO: El nombre 'recordatorio_deuda_detallado' debe coincidir EXACTAMENTE con el de Meta.
        String jsonBody = String.format("""
            {
                "messaging_product": "whatsapp",
                "to": "%s",
                "type": "template",
                "template": {
                    "name": "recordatorio_deuda_detallado",
                    "language": { "code": "es" },
                    "components": [
                        {
                            "type": "body",
                            "parameters": [
                                { "type": "text", "text": "%s" },
                                { "type": "text", "text": "%s" },
                                { "type": "text", "text": "%s" },
                                { "type": "text", "text": "%s" }
                            ]
                        }
                    ]
                }
            }
            """, phoneNumber, param1_clientName, param2_amount, param3_issueDate, param4_dueDate);

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            System.out.println("Enviando petición a WhatsApp API: " + jsonBody);
            restTemplate.postForEntity(apiUrl, entity, String.class);
            System.out.println("Petición de plantilla enviada con éxito para el número: " + phoneNumber);
        } catch (Exception e) {
            System.err.println("Error al enviar el mensaje de plantilla de WhatsApp: " + e.getMessage());
            throw new RuntimeException("Fallo al enviar a la API de WhatsApp", e);
        }
    }
}
