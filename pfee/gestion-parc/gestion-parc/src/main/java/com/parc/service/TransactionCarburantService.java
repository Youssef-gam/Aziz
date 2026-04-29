package com.parc.service;

import com.parc.dto.TransactionCarburantDTO;
import com.parc.domain.entity.*;
import com.parc.domain.enums.TypeTransaction;
import com.parc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionCarburantService {

    private final TransactionCarburantRepository transactionRepository;
    private final CarteCarburantRepository carteRepository;
    private final MissionRepository missionRepository;
    private final ChauffeurRepository chauffeurRepository;

    private TransactionCarburantDTO toDTO(TransactionCarburant t) {
        TransactionCarburantDTO dto = new TransactionCarburantDTO();
        dto.setId(t.getId());
        dto.setDate(t.getDate());
        dto.setMontant(t.getMontant());
        dto.setLitres(t.getLitres());
        dto.setType(t.getType().name());
        dto.setCarteId(t.getCarte().getId());
        dto.setCarteNumero(t.getCarte().getNumCarte());
        if (t.getChauffeur() != null) {
            dto.setChauffeurId(t.getChauffeur().getId());
            dto.setChauffeurNom(t.getChauffeur().getNom() + " " + t.getChauffeur().getPrenom());
        }
        if (t.getMission() != null) {
            dto.setMissionId(t.getMission().getId());
        }
        if (t.getVehicule() != null) {
            dto.setVehiculeId(t.getVehicule().getId());
            dto.setVehiculeMatricule(t.getVehicule().getMatricule());
        }
        return dto;
    }

    @Transactional
    public TransactionCarburantDTO enregistrerConsommation(Long carteId, Double montant, Double litres,
                                                           Long missionId, Long chauffeurId) {
        // 1. Récupérer la carte
        CarteCarburant carte = carteRepository.findById(carteId)
                .orElseThrow(() -> new RuntimeException("Carte non trouvée"));

        // 2. Vérifier que la carte appartient au chauffeur
        if (carte.getChauffeur() == null || !carte.getChauffeur().getId().equals(chauffeurId)) {
            throw new RuntimeException("Cette carte n'appartient pas au chauffeur");
        }

        // 3. Vérifier le solde
        if (carte.getSolde() < montant) {
            throw new RuntimeException("Solde insuffisant");
        }

        // 4. Déduire le montant du solde
        carte.setSolde(carte.getSolde() - montant);
        carteRepository.save(carte);

        // 5. Récupérer la mission et le véhicule associé
        Mission mission = null;
        Vehicule vehicule = null;
        if (missionId != null) {
            mission = missionRepository.findById(missionId)
                    .orElseThrow(() -> new RuntimeException("Mission non trouvée"));
            if (mission.getChauffeur() == null || !mission.getChauffeur().getId().equals(chauffeurId)) {
                throw new RuntimeException("Ce chauffeur n'est pas assigné à cette mission");
            }
            vehicule = mission.getVehicule();
        }

        // 6. Créer la transaction
        TransactionCarburant transaction = new TransactionCarburant();
        transaction.setDate(LocalDateTime.now());
        transaction.setMontant(montant);
        transaction.setLitres(litres);
        transaction.setType(TypeTransaction.CONSOMMATION);
        transaction.setCarte(carte);
        transaction.setChauffeur(carte.getChauffeur());
        transaction.setVehicule(vehicule);
        transaction.setMission(mission);

        transaction = transactionRepository.save(transaction);
        return toDTO(transaction);
    }

    public List<TransactionCarburantDTO> getTransactionsByCarte(Long carteId) {
        return transactionRepository.findByCarteId(carteId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionCarburantDTO> getTransactionsByMission(Long missionId) {
        return transactionRepository.findByMissionId(missionId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionCarburantDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}