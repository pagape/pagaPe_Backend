package com.edu.pe.pagaPeBackend.WhatssAppHelper.impl;

import com.edu.pe.pagaPeBackend.WhatssAppHelper.WhatsAppService;
import com.edu.pe.pagaPeBackend.reminder.dto.WhatsAppMessageRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;

@Service
public class WhatsAppServiceImpl implements WhatsAppService {

    private static final Logger log = LoggerFactory.getLogger(WhatsAppServiceImpl.class);

    // URL desde application.properties.
    @Value("${whatsapp.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendMessages(List<WhatsAppMessageRequest> messages) {
        log.info("Preparando para enviar {} mensajes a la API de WhatsApp en la URL: {}", messages.size(), apiUrl);

        // Cabeceras de la petición
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // AUTENTICACIÓN SI LA API LA REQUIERE
        // headers.set("Authorization", "Bearer TU_API_KEY_DE_WHATSAPP");

        HttpEntity<List<WhatsAppMessageRequest>> request = new HttpEntity<>(messages, headers);

        try {
            // Esqueleto de la llamada.
            // Cuando tenga la API real, enviará los datos.
            // restTemplate.postForEntity(apiUrl, request, String.class);

            // Por ahora, solo simularemos el envío con un log
            log.info("SIMULACIÓN: Petición POST a {} enviada con éxito.", apiUrl);
            log.info("SIMULACIÓN: Datos enviados: {}", request.getBody().toString());

        } catch (Exception e) {
            log.error("Error al intentar enviar mensajes a la API de WhatsApp", e);
            // Añadir lógica para reintentar el envío o notificar a un administrador
        }
    }
}