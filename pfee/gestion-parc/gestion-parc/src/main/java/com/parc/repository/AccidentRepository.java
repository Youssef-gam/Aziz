package com.parc.repository;

import com.parc.domain.entity.Accident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccidentRepository extends JpaRepository<Accident, Long> {
    List<Accident> findByChauffeurId(Long chauffeurId);
    List<Accident> findByVehiculeId(Long vehiculeId);
}