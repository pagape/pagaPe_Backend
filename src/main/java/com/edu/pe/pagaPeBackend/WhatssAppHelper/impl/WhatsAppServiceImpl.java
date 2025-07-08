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

    // PURL de servicio externo envío de mensajes
    @Value("https://webhook-wsp-qqb5.onrender.com/enviar-mensajes")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void sendMessages(List<WhatsAppMessageRequest> messages) {
        if (messages == null || messages.isEmpty()) {
            log.info("No hay mensajes para enviar a la API de WhatsApp.");
            return;
        }

        log.info("Enviando un lote de {} mensajes al servicio externo en: {}", messages.size(), apiUrl);

        // Cabeceras de la petición.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Si la el servicio requiere un token, añador acá :v.
        // Ejemplo?: headers.set("Authorization", "Bearer ALGUN_TOKEN_INTERNO");

        HttpEntity<List<WhatsAppMessageRequest>> requestEntity = new HttpEntity<>(messages, headers);

        try {
            restTemplate.postForEntity(apiUrl, requestEntity, String.class);

            log.info("Lote de mensajes enviado con éxito al servicio externo.");

        } catch (Exception e) {
            log.error("Error al enviar el lote de mensajes al servicio externo.", e);
        }
    }
}