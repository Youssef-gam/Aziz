package com.parc.repository;

import com.parc.domain.entity.Parc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcRepository extends JpaRepository<Parc, Long> {
    // Tu peux ajouter des méthodes personnalisées si besoin
}