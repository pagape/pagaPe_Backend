package com.edu.pe.pagaPeBackend.manageClientService.repository;

import com.edu.pe.pagaPeBackend.manageClientService.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository< Service,Long> {
}
