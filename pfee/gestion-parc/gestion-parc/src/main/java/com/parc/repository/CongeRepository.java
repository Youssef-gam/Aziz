package com.parc.repository;

import com.parc.domain.entity.Conge;
import com.parc.domain.enums.StatutConge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CongeRepository extends JpaRepository<Conge, Long> {
    List<Conge> findByChauffeurId(Long chauffeurId);  // une seule fois
    List<Conge> findByStatut(StatutConge statut);
}