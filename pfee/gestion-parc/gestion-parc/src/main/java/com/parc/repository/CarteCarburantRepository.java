package com.parc.repository;

import com.parc.domain.entity.CarteCarburant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarteCarburantRepository extends JpaRepository<CarteCarburant, Long> {
    Optional<CarteCarburant> findByNumCarte(String numCarte);
    boolean existsByNumCarte(String numCarte);
    List<CarteCarburant> findByChauffeurId(Long chauffeurId);
}
