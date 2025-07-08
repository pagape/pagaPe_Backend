package com.edu.pe.pagaPeBackend.manageClientService.repository;

import com.edu.pe.pagaPeBackend.manageClientService.model.ClientService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientServiceRepository extends JpaRepository< ClientService,Long> {

    @Query("SELECT cs FROM ClientService cs " +
            "WHERE cs.client.id = :clientId " +
            "AND cs.service.id = :serviceId " +
            "AND cs.active = true " +
            "AND (cs.issueDate <= :dueDate AND cs.dueDate >= :issueDate)")
    Optional<ClientService> findOverlappingService(
            @Param("clientId") Long clientId,
            @Param("serviceId") Long serviceId,
            @Param("issueDate") LocalDate issueDate,
            @Param("dueDate") LocalDate dueDate);

    @Query("SELECT cs FROM ClientService cs " +
            "JOIN FETCH cs.client c " +
            "JOIN FETCH cs.service s " +
            "WHERE cs.dueDate = :targetDueDate " +
            "AND (:serviceId IS NULL OR cs.service.id = :serviceId) " +
            "AND cs.active = true")
    List<ClientService> findByDueDateAndOptionalService(
            @Param("targetDueDate") LocalDate targetDueDate,
            @Param("serviceId") Long serviceId);
}
