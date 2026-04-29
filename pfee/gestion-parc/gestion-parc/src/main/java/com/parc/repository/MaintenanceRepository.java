package com.parc.repository;

import com.parc.domain.entity.Maintenance;
import com.parc.domain.enums.StatutMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByVehiculeId(Long vehiculeId);
    List<Maintenance> findByGarageId(Long garageId);
    List<Maintenance> findByStatut(StatutMaintenance statut);
    List<Maintenance> findByGarageIdAndStatutIn(Long garageId, List<StatutMaintenance> statuts);
}