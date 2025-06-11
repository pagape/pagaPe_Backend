package com.edu.pe.pagaPeBackend.manageClientService.dto.client;

import com.edu.pe.pagaPeBackend.manageClientService.model.Client;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ClientRequest {
    @NotNull
    private String userFirstName;

    @NotNull
    private String userLastName;

    @NotNull
    private String userEmail;

    private String userPhone;


    private boolean active;

    public boolean getActive(){

        return active;
    }
}
