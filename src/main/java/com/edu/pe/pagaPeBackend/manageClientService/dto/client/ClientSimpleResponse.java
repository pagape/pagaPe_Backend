package com.edu.pe.pagaPeBackend.manageClientService.dto.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientSimpleResponse {
    private Long id;
    private String userFirstName;
    private String userLastName;
    private String userPhone;
}
