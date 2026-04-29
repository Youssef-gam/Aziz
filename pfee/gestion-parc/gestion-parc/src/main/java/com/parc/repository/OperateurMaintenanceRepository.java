package com.parc.repository;

import com.parc.domain.entity.OperateurMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OperateurMaintenanceRepository extends JpaRepository<OperateurMaintenance, Long> {
    Optional<OperateurMaintenance> findByEmail(String email);
    Optional<OperateurMaintenance> findByEmailIgnoreCase(String email);
    List<OperateurMaintenance> findByDisponibilite(String disponibilite);

    List<OperateurMaintenance> findByGarage_Id(Long garageId);
}