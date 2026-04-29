package com.parc.repository;

import com.parc.domain.entity.ChefDeParc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChefDeParcRepository extends JpaRepository<ChefDeParc, Long> {

    // Trouver le chef de parc par le parc qu'il dirige
    Optional<ChefDeParc> findByParcId(Long parcId);

    // Vérifier si un chef existe pour un parc
    boolean existsByParcId(Long parcId);
}