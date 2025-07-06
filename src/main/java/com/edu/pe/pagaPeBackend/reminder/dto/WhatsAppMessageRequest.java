package com.edu.pe.pagaPeBackend.reminder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WhatsAppMessageRequest {

    // Usa @JsonProperty para que el JSON final tenga exactamente los nombres
    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("numero")
    private String numero;

    @JsonProperty("servicio")
    private String servicio;

    @JsonProperty("monto")
    private String monto;

    @JsonProperty("fecha_inicio")
    private String fechaInicio;

    @JsonProperty("fecha_fin")
    private String fechaFin;

    @JsonProperty("tipo")
    private String tipo; // "deuda" o "recordatorio"
}