package com.parc.repository;

import com.parc.domain.entity.Amende;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AmendeRepository extends JpaRepository<Amende, Long> {
    List<Amende> findByChauffeurId(Long chauffeurId);
    List<Amende> findByVehiculeId(Long vehiculeId);
}