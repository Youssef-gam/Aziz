package com.parc.repository;

import com.parc.domain.entity.TransactionCarburant;
import com.parc.domain.enums.TypeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionCarburantRepository extends JpaRepository<TransactionCarburant, Long> {

    List<TransactionCarburant> findByCarteId(Long carteId);
    List<TransactionCarburant> findByMissionId(Long missionId);
    List<TransactionCarburant> findByChauffeurId(Long chauffeurId);
    List<TransactionCarburant> findByType(TypeTransaction type);

    // Total consommé par un véhicule
    @Query("SELECT SUM(t.montant) FROM TransactionCarburant t WHERE t.vehicule.id = :vehiculeId AND t.type = :type")
    Double sumMontantByVehicule(@Param("vehiculeId") Long vehiculeId, @Param("type") TypeTransaction type);

    // Total consommé par un parc
    @Query("SELECT SUM(t.montant) FROM TransactionCarburant t WHERE t.vehicule.parc.id = :parcId AND t.type = :type")
    Double sumMontantByParc(@Param("parcId") Long parcId, @Param("type") TypeTransaction type);
}