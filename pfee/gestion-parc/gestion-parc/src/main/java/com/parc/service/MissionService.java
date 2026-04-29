package com.parc.service;

import com.parc.dto.FeuilleDeRouteDTO;
import com.parc.dto.MissionDTO;
import com.parc.domain.entity.*;
import com.parc.domain.enums.StatutMission;
import com.parc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final FeuilleDeRouteRepository feuilleDeRouteRepository;
    private final NotificationService notificationService;

    // Conversions
    private MissionDTO toDTO(Mission mission) {
        MissionDTO dto = new MissionDTO();
        dto.setId(mission.getId());
        dto.setDescription(mission.getDescription());
        dto.setDestination(mission.getDestination());
        dto.setObjetALivrer(mission.getObjetALivrer());
        dto.setNombreDeParticipants(mission.getNombreDeParticipants());
        dto.setDateDebut(mission.getDateDebut());
        dto.setDateFin(mission.getDateFin());
        dto.setStatut(mission.getStatut());

        if (mission.getVehicule() != null) {
            dto.setVehiculeId(mission.getVehicule().getId());
            dto.setVehiculeMatricule(mission.getVehicule().getMatricule());
        }
        if (mission.getChauffeur() != null) {
            dto.setChauffeurId(mission.getChauffeur().getId());
            dto.setChauffeurNom(mission.getChauffeur().getNom() + " " + mission.getChauffeur().getPrenom());
        }
        if (mission.getFeuilleDeRoute() != null) {
            dto.setFeuilleDeRoute(toFeuilleDTO(mission.getFeuilleDeRoute()));
        }
        return dto;
    }

    private FeuilleDeRouteDTO toFeuilleDTO(FeuilleDeRoute feuille) {
        FeuilleDeRouteDTO dto = new FeuilleDeRouteDTO();
        dto.setId(feuille.getId());
        dto.setNumeroFeuille(feuille.getNumeroFeuille());
        dto.setNombreParticipants(feuille.getNombreParticipants());
        dto.setDestination(feuille.getDestination());
        dto.setObjetMission(feuille.getObjetMission());
        if (feuille.getMission() != null) {
            dto.setMissionId(feuille.getMission().getId());
        }
        return dto;
    }

    private Mission toEntity(MissionDTO dto) {
        Mission mission = new Mission();
        mission.setDescription(dto.getDescription());
        mission.setDestination(dto.getDestination());
        mission.setObjetALivrer(dto.getObjetALivrer());
        mission.setNombreDeParticipants(dto.getNombreDeParticipants());
        mission.setDateDebut(dto.getDateDebut());
        mission.setDateFin(dto.getDateFin());
        mission.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutMission.PLANIFIEE);

        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec id : " + dto.getVehiculeId()));
            mission.setVehicule(vehicule);
        }
        if (dto.getChauffeurId() != null) {
            Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                    .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé avec id : " + dto.getChauffeurId()));
            mission.setChauffeur(chauffeur);
        }
        return mission;
    }

    // CRUD
    @Transactional
    public MissionDTO createMission(MissionDTO dto) {
        Mission mission = toEntity(dto);
        Mission saved = missionRepository.save(mission);
        
        // 🚀 Créer automatiquement la feuille de route
        createFeuilleDeRouteAutomatically(saved);
        
        // 📬 Envoyer une notification si un chauffeur est assigné
        if (saved.getChauffeur() != null) {
            notificationService.notifierMissionAssignee(saved.getChauffeur().getId(), saved);
            System.out.println("✅ Notification envoyée au chauffeur: " + saved.getChauffeur().getNom());
        }
        
        return toDTO(saved);
    }

    /**
     * Crée automatiquement une feuille de route pour une mission
     */
    private void createFeuilleDeRouteAutomatically(Mission mission) {
        try {
            FeuilleDeRoute feuilleDeRoute = new FeuilleDeRoute();
            
            // Générer un numéro unique pour la feuille
            String numeroFeuille = "FDR-" + mission.getId() + "-" + System.currentTimeMillis();
            feuilleDeRoute.setNumeroFeuille(numeroFeuille);
            
            // Remplir les informations
            feuilleDeRoute.setDestination(mission.getDestination());
            feuilleDeRoute.setObjetMission(mission.getDescription());
            feuilleDeRoute.setNombreParticipants(1); // Par défaut, 1 participant (le chauffeur)
            feuilleDeRoute.setMission(mission);
            
            // Sauvegarder la feuille de route
            feuilleDeRouteRepository.save(feuilleDeRoute);
            mission.setFeuilleDeRoute(feuilleDeRoute);
            missionRepository.save(mission);
            
            System.out.println("✅ Feuille de route créée automatiquement: " + numeroFeuille);
        } catch (Exception e) {
            System.err.println("⚠️ Erreur lors de la création automatique de la feuille de route: " + e.getMessage());
        }
    }

    public List<MissionDTO> getAllMissions() {
        return missionRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MissionDTO getMissionById(Long id) {
        return missionRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec id : " + id));
    }

    public List<MissionDTO> getMissionsByChauffeur(Long chauffeurId) {
        return missionRepository.findByChauffeurId(chauffeurId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MissionDTO> getMissionsByVehicule(Long vehiculeId) {
        return missionRepository.findByVehiculeId(vehiculeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MissionDTO updateMission(Long id, MissionDTO dto) {
        Mission existing = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec id : " + id));

        existing.setDescription(dto.getDescription());
        existing.setDestination(dto.getDestination());
        existing.setDateDebut(dto.getDateDebut());
        existing.setDateFin(dto.getDateFin());
        existing.setStatut(dto.getStatut());

        boolean chauffeurChanged = false;
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec id : " + dto.getVehiculeId()));
            existing.setVehicule(vehicule);
        } else {
            existing.setVehicule(null);
        }

        if (dto.getChauffeurId() != null) {
            Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                    .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé avec id : " + dto.getChauffeurId()));
            
            // Vérifier si le chauffeur a changé
            if (existing.getChauffeur() == null || !existing.getChauffeur().getId().equals(chauffeur.getId())) {
                chauffeurChanged = true;
            }
            
            existing.setChauffeur(chauffeur);
        } else {
            existing.setChauffeur(null);
        }

        Mission updated = missionRepository.save(existing);
        
        // 📬 Envoyer une notification si un chauffeur est assigné
        if (chauffeurChanged && updated.getChauffeur() != null) {
            notificationService.notifierMissionAssignee(updated.getChauffeur().getId(), updated);
            System.out.println("✅ Notification envoyée au chauffeur: " + updated.getChauffeur().getNom());
        }
        // 🔄 Envoyer une notification de modification si le chauffeur reste le même
        else if (!chauffeurChanged && existing.getChauffeur() != null) {
            notificationService.notifierMissionModifiee(existing.getChauffeur().getId(), updated);
            System.out.println("✅ Notification de modification envoyée au chauffeur: " + existing.getChauffeur().getNom());
        }
        
        return toDTO(updated);
    }

    @Transactional
    public void deleteMission(Long id) {
        Mission existing = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec id : " + id));
        
        // Supprimer la feuille de route d'abord si elle existe
        // (cascade sera géré après suppression de la Mission)
        if (existing.getFeuilleDeRoute() != null) {
            // Détacher la feuille de route de la mission
            FeuilleDeRoute feuille = existing.getFeuilleDeRoute();
            feuille.setMission(null);
            feuilleDeRouteRepository.save(feuille);
            existing.setFeuilleDeRoute(null);
        }
        
        // Supprimer la mission
        missionRepository.delete(existing);
    }

    // Gestion de la feuille de route
    @Transactional
    public FeuilleDeRouteDTO addFeuilleDeRoute(Long missionId, FeuilleDeRouteDTO dto) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec id : " + missionId));

        if (mission.getFeuilleDeRoute() != null) {
            throw new RuntimeException("Cette mission a déjà une feuille de route.");
        }

        FeuilleDeRoute feuille = new FeuilleDeRoute();
        feuille.setNumeroFeuille(dto.getNumeroFeuille());
        feuille.setNombreParticipants(dto.getNombreParticipants());
        feuille.setDestination(dto.getDestination());
        feuille.setObjetMission(dto.getObjetMission());
        feuille.setMission(mission);

        FeuilleDeRoute saved = feuilleDeRouteRepository.save(feuille);
        mission.setFeuilleDeRoute(saved);
        missionRepository.save(mission); // mise à jour de la relation

        return toFeuilleDTO(saved);
    }

    public FeuilleDeRouteDTO getFeuilleDeRouteByMission(Long missionId) {
        return feuilleDeRouteRepository.findByMissionId(missionId)
                .map(this::toFeuilleDTO)
                .orElseThrow(() -> new RuntimeException("Aucune feuille de route trouvée pour cette mission."));
    }

    @Transactional
    public FeuilleDeRouteDTO updateFeuilleDeRoute(Long missionId, FeuilleDeRouteDTO dto) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec id : " + missionId));

        FeuilleDeRoute feuille = mission.getFeuilleDeRoute();
        if (feuille == null) {
            throw new RuntimeException("Cette mission n'a pas encore de feuille de route.");
        }

        feuille.setNumeroFeuille(dto.getNumeroFeuille());
        feuille.setNombreParticipants(dto.getNombreParticipants());
        feuille.setDestination(dto.getDestination());
        feuille.setObjetMission(dto.getObjetMission());

        FeuilleDeRoute updated = feuilleDeRouteRepository.save(feuille);
        return toFeuilleDTO(updated);
    }

    @Transactional
    public void deleteFeuilleDeRoute(Long missionId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec id : " + missionId));

        FeuilleDeRoute feuille = mission.getFeuilleDeRoute();
        if (feuille != null) {
            mission.setFeuilleDeRoute(null);
            missionRepository.save(mission);
            feuilleDeRouteRepository.delete(feuille);
        }
    }

    // Affectation de véhicule à une mission
    @Transactional
    public MissionDTO affecterVehicule(Long missionId, Long vehiculeId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec id : " + missionId));
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId)
                .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec id : " + vehiculeId));
        mission.setVehicule(vehicule);
        return toDTO(missionRepository.save(mission));
    }
}