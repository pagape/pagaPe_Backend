package com.edu.pe.pagaPeBackend.manageClientService.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userPhone;

    private LocalDate created;
    private boolean active;

    public boolean getActive(){

        return active;
    }
}
