package com.edu.pe.pagaPeBackend.reminder.dto.reminder;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReminderRequestDTO {

    @NotNull(message = "El ID del cliente no puede ser nulo.")
    private Long clientId;

    @NotNull(message = "La fecha y hora de envío no puede ser nula.")
    @Future(message = "La fecha y hora de envío debe ser en el futuro.")
    private LocalDateTime sendDateTime;

    // El usuario puede enviar una descripción personalizada opcional.
    private String description;

    @NotBlank(message = "El tipo de servicio no puede estar vacío.") // <-- AÑADIR VALIDACIÓN
    private String typeService;

    // Opcional: El usuario puede decidir si es un recordatorio de deuda vencida o por vencer.
    private Boolean isDebtor;
}
