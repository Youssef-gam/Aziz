package com.parc.service;

import com.parc.dto.CarteCarburantDTO;
import com.parc.domain.entity.CarteCarburant;
import com.parc.domain.entity.Chauffeur;
import com.parc.domain.enums.StatutCarte;
import com.parc.repository.CarteCarburantRepository;
import com.parc.repository.ChauffeurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarteCarburantService {

    private final CarteCarburantRepository carteRepository;
    private final ChauffeurRepository chauffeurRepository;

    // Conversion entité -> DTO
    private CarteCarburantDTO toDTO(CarteCarburant carte) {
        CarteCarburantDTO dto = new CarteCarburantDTO();
        dto.setId(carte.getId());
        dto.setNumCarte(carte.getNumCarte());
        dto.setPlafond(carte.getPlafond());
        dto.setSolde(carte.getSolde());
        dto.setStatut(carte.getStatut());

        if (carte.getChauffeur() != null) {
            dto.setChauffeurId(carte.getChauffeur().getId());
            dto.setChauffeurNom(carte.getChauffeur().getNom() + " " + carte.getChauffeur().getPrenom());
        }
        return dto;
    }

    // Conversion DTO -> entité
    private CarteCarburant toEntity(CarteCarburantDTO dto) {
        CarteCarburant carte = new CarteCarburant();
        carte.setNumCarte(dto.getNumCarte());
        carte.setPlafond(dto.getPlafond());
        carte.setSolde(dto.getSolde() != null ? dto.getSolde() : dto.getPlafond()); // solde initial = plafond
        carte.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutCarte.ACTIVE);

        if (dto.getChauffeurId() != null) {
            Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                    .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé avec id : " + dto.getChauffeurId()));
            carte.setChauffeur(chauffeur);
        }
        return carte;
    }

    // Créer une carte
    @Transactional
    public CarteCarburantDTO createCarte(CarteCarburantDTO dto) {
        if (carteRepository.existsByNumCarte(dto.getNumCarte())) {
            throw new RuntimeException("Une carte avec ce numéro existe déjà.");
        }
        CarteCarburant carte = toEntity(dto);
        carte = carteRepository.save(carte);
        return toDTO(carte);
    }

    // Récupérer toutes les cartes
    public List<CarteCarburantDTO> getAllCartes() {
        return carteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer une carte par id
    public CarteCarburantDTO getCarteById(Long id) {
        return carteRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Carte non trouvée avec id : " + id));
    }

    // Récupérer les cartes d’un chauffeur
    public List<CarteCarburantDTO> getCartesByChauffeur(Long chauffeurId) {
        return carteRepository.findByChauffeurId(chauffeurId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Mettre à jour une carte
    @Transactional
    public CarteCarburantDTO updateCarte(Long id, CarteCarburantDTO dto) {
        CarteCarburant existing = carteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carte non trouvée avec id : " + id));

        existing.setNumCarte(dto.getNumCarte());
        existing.setPlafond(dto.getPlafond());
        existing.setSolde(dto.getSolde());
        existing.setStatut(dto.getStatut());

        if (dto.getChauffeurId() != null) {
            Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                    .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé avec id : " + dto.getChauffeurId()));
            existing.setChauffeur(chauffeur);
        } else {
            existing.setChauffeur(null);
        }

        existing = carteRepository.save(existing);
        return toDTO(existing);
    }

    // Supprimer une carte
    @Transactional
    public void deleteCarte(Long id) {
        CarteCarburant existing = carteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carte non trouvée avec id : " + id));
        carteRepository.delete(existing);
    }

    // Recharger le solde (augmenter)
    @Transactional
    public CarteCarburantDTO rechargerSolde(Long id, Double montant) {
        CarteCarburant carte = carteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carte non trouvée avec id : " + id));
        if (carte.getSolde() + montant > carte.getPlafond()) {
            throw new RuntimeException("Le rechargement dépasse le plafond autorisé.");
        }
        carte.setSolde(carte.getSolde() + montant);
        carte = carteRepository.save(carte);
        return toDTO(carte);
    }

    // Consommer du carburant (débiter le solde)
    @Transactional
    public CarteCarburantDTO consommerCarburant(Long id, Double montant) {
        CarteCarburant carte = carteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carte non trouvée avec id : " + id));
        if (carte.getSolde() < montant) {
            throw new RuntimeException("Solde insuffisant.");
        }
        carte.setSolde(carte.getSolde() - montant);
        carte = carteRepository.save(carte);
        return toDTO(carte);
    }
}