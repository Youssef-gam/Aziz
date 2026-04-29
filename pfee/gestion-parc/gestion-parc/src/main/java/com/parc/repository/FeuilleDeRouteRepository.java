package com.parc.repository;

import com.parc.domain.entity.FeuilleDeRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FeuilleDeRouteRepository extends JpaRepository<FeuilleDeRoute, Long> {
    Optional<FeuilleDeRoute> findByMissionId(Long missionId);
}