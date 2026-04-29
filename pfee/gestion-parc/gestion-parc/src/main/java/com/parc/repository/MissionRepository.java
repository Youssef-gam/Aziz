package com.parc.repository;

import com.parc.domain.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    List<Mission> findByChauffeurId(Long chauffeurId);
    List<Mission> findByVehiculeId(Long vehiculeId);
}