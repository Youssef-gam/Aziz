package com.parc.repository;

import com.parc.domain.entity.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GarageRepository extends JpaRepository<Garage, Long> {
    Optional<Garage> findBySpecialite(String specialite);
}