package com.parc.repository;

import com.parc.domain.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    Optional<Vehicule> findByMatricule(String matricule);
    boolean existsByMatricule(String matricule);
    boolean existsByNumeroChassis(String numeroChassis);
    List<Vehicule> findByParcId(Long parcId);
}