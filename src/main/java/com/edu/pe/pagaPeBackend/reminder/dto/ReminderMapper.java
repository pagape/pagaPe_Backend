package com.edu.pe.pagaPeBackend.reminder.dto;

import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceMapper;
import com.edu.pe.pagaPeBackend.manageClientService.dto.ClientServiceResponse;
import com.edu.pe.pagaPeBackend.manageClientService.dto.Service.ServiceMapper;
import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import com.edu.pe.pagaPeBackend.reminder.model.Reminder;
import com.edu.pe.pagaPeBackend.reminder.model.WhatsappNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReminderMapper {
    
    @Autowired
    private ServiceMapper serviceMapper;
    
    @Autowired
    private WhatsappNumberMapper whatsappNumberMapper;
    
    @Autowired
    private ClientServiceMapper clientServiceMapper;
    
    public Reminder toReminder(ReminderRequest request, Service service, WhatsappNumber whatsappNumber, List<ClientService> clientServices) {
        return Reminder.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .scheduledDate(request.getScheduledDate())
                .responseStatus(request.getResponseStatus() != null ? request.getResponseStatus() : Reminder.ResponseStatus.PENDIENTE)
                .service(service)
                .whatsappNumber(whatsappNumber)
                .clientServices(clientServices)
                .isDebtor(request.getIsDebtor() != null ? request.getIsDebtor() : false)
                .daysInAdvance(request.getDaysInAdvance())
                .build();
    }
    
    public ReminderResponse toReminderResponse(Reminder reminder) {
        List<ClientServiceResponse> clientServiceResponses = reminder.getClientServices() != null ?
                reminder.getClientServices().stream()
                        .map(clientService -> clientServiceMapper.toResponse(clientService))
                        .collect(Collectors.toList()) :
                new ArrayList<>();
                
        return ReminderResponse.builder()
                .id(reminder.getId())
                .title(reminder.getTitle())
                .description(reminder.getDescription())
                .scheduledDate(reminder.getScheduledDate())
                .responseStatus(reminder.getResponseStatus())
                .service(serviceMapper.toResponse(reminder.getService()))
                .whatsappNumber(WhatsappNumberMapper.toWhatsappNumberResponse(reminder.getWhatsappNumber()))
                .clientServices(clientServiceResponses)
                .isDebtor(reminder.isDebtor())
                .daysInAdvance(reminder.getDaysInAdvance())
                .build();
    }
} 