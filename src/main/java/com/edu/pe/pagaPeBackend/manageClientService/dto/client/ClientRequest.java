package com.edu.pe.pagaPeBackend.manageClientService.dto.client;

import jakarta.persistence.Column;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ClientRequest {

    private String userFirstName;


    private String userLastName;


    private String userEmail;


    private String userPhone;
}
