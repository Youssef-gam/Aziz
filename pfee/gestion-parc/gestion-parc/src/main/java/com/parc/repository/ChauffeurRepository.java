package com.parc.repository;

import com.parc.domain.entity.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {
    Optional<Chauffeur> findByNumeroPermis(String numeroPermis);
    boolean existsByNumeroPermis(String numeroPermis);
    boolean existsByParcId(Long parcId);
    Optional<Chauffeur> findByEmail(String email);   // ← unique déclaration
}