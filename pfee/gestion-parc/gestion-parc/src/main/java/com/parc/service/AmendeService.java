package com.parc.service;

import com.parc.dto.AmendeDTO;
import com.parc.domain.entity.Amende;
import com.parc.domain.entity.Chauffeur;
import com.parc.domain.entity.Vehicule;
import com.parc.repository.AmendeRepository;
import com.parc.repository.ChauffeurRepository;
import com.parc.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AmendeService {

    private final AmendeRepository amendeRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final NotificationService notificationService;

    // === Conversion DTO ⇄ Entité ===
    private AmendeDTO toDTO(Amende amende) {
        AmendeDTO dto = new AmendeDTO();
        dto.setId(amende.getId());
        dto.setMontant(amende.getMontant());
        dto.setDateInfraction(amende.getDateInfraction());
        dto.setLieuInfraction(amende.getLieuInfraction());
        dto.setMotif(amende.getMotif());
        dto.setDescription(amende.getDescription());
        dto.setPhoto(amende.getPhoto());
        dto.setStatut(amende.getStatut());
        dto.setDateDeclaration(amende.getDateDeclaration());
        dto.setPayee(amende.getPayee());

        if (amende.getChauffeur() != null) {
            dto.setChauffeurId(amende.getChauffeur().getId());
            dto.setChauffeurNom(amende.getChauffeur().getNom() + " " + amende.getChauffeur().getPrenom());
        }
        if (amende.getVehicule() != null) {
            dto.setVehiculeId(amende.getVehicule().getId());
            dto.setVehiculeMatricule(amende.getVehicule().getMatricule());
        }
        return dto;
    }

    private Amende toEntity(AmendeDTO dto) {
        Amende amende = new Amende();
        amende.setMontant(dto.getMontant());
        amende.setDateInfraction(dto.getDateInfraction());
        amende.setLieuInfraction(dto.getLieuInfraction());
        amende.setMotif(dto.getMotif());
        amende.setDescription(dto.getDescription());
        amende.setPhoto(dto.getPhoto());
        amende.setStatut(dto.getStatut() != null ? dto.getStatut() : "SIGNALÉE");
        amende.setPayee(dto.getPayee() != null ? dto.getPayee() : false);

        if (dto.getChauffeurId() != null) {
            Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                    .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));
            amende.setChauffeur(chauffeur);
        }
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
            amende.setVehicule(vehicule);
        }
        return amende;
    }

    // === CRUD ===
    @Transactional
    public AmendeDTO create(AmendeDTO dto) {
        Amende amende = toEntity(dto);
        amende = amendeRepository.save(amende);

        String message = String.format(
            "Amende déclarée pour le véhicule %s par %s %s. Montant : %.2f TND",
            amende.getVehicule().getMatricule(),
            amende.getChauffeur().getNom(),
            amende.getChauffeur().getPrenom(),
            amende.getMontant()
        );
        notificationService.envoyerNotificationChefParc(message);

        return toDTO(amende);
    }

    public List<AmendeDTO> getAll() {
        return amendeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AmendeDTO getById(Long id) {
        return amendeRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Amende non trouvée"));
    }

    public List<AmendeDTO> getByChauffeur(Long chauffeurId) {
        return amendeRepository.findByChauffeurId(chauffeurId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<AmendeDTO> getByVehicule(Long vehiculeId) {
        return amendeRepository.findByVehiculeId(vehiculeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AmendeDTO update(Long id, AmendeDTO dto) {
        Amende existing = amendeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Amende non trouvée"));
        existing.setMontant(dto.getMontant());
        existing.setDateInfraction(dto.getDateInfraction());
        existing.setLieuInfraction(dto.getLieuInfraction());
        existing.setMotif(dto.getMotif());
        existing.setDescription(dto.getDescription());
        existing.setPhoto(dto.getPhoto());
        existing.setStatut(dto.getStatut());
        existing.setPayee(dto.getPayee());

        if (dto.getChauffeurId() != null) {
            Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                    .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));
            existing.setChauffeur(chauffeur);
        } else {
            existing.setChauffeur(null);
        }

        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
            existing.setVehicule(vehicule);
        } else {
            existing.setVehicule(null);
        }

        return toDTO(amendeRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        Amende existing = amendeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Amende non trouvée"));
        amendeRepository.delete(existing);
    }

    // === Méthode spécifique : marquer l’amende comme payée ===
    @Transactional
    public AmendeDTO marquerPayee(Long id) {
        Amende amende = amendeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Amende non trouvée"));
        amende.setPayee(true);
        return toDTO(amendeRepository.save(amende));
    }
}