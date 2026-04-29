package com.parc.service;

import com.parc.dto.FeuilleDeRouteDTO;
import com.parc.domain.entity.FeuilleDeRoute;
import com.parc.domain.entity.Mission;
import com.parc.repository.FeuilleDeRouteRepository;
import com.parc.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeuilleDeRouteService {

    private final FeuilleDeRouteRepository feuilleDeRouteRepository;
    private final MissionRepository missionRepository;

    // Conversion entité -> DTO
    private FeuilleDeRouteDTO toDTO(FeuilleDeRoute feuille) {
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

    // Conversion DTO -> entité (sans la mission)
    private FeuilleDeRoute toEntity(FeuilleDeRouteDTO dto) {
        FeuilleDeRoute feuille = new FeuilleDeRoute();
        feuille.setNumeroFeuille(dto.getNumeroFeuille());
        feuille.setNombreParticipants(dto.getNombreParticipants());
        feuille.setDestination(dto.getDestination());
        feuille.setObjetMission(dto.getObjetMission());
        return feuille;
    }

    // Créer une feuille de route pour une mission
    @Transactional
    public FeuilleDeRouteDTO createFeuilleDeRoute(Long missionId, FeuilleDeRouteDTO dto) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission non trouvée avec id : " + missionId));

        // Vérifier si la mission a déjà une feuille de route
        if (mission.getFeuilleDeRoute() != null) {
            throw new RuntimeException("Cette mission a déjà une feuille de route.");
        }

        FeuilleDeRoute feuille = toEntity(dto);
        feuille.setMission(mission);
        FeuilleDeRoute saved = feuilleDeRouteRepository.save(feuille);

        // Mettre à jour la relation inverse
        mission.setFeuilleDeRoute(saved);
        missionRepository.save(mission);

        return toDTO(saved);
    }

    // Récupérer la feuille de route d'une mission
    public FeuilleDeRouteDTO getFeuilleByMission(Long missionId) {
        return feuilleDeRouteRepository.findByMissionId(missionId)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Aucune feuille de route pour cette mission."));
    }

    // Récupérer toutes les feuilles de route
    public List<FeuilleDeRouteDTO> getAllFeuilles() {
        return feuilleDeRouteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Mettre à jour une feuille de route (par son ID)
    @Transactional
    public FeuilleDeRouteDTO updateFeuilleDeRoute(Long id, FeuilleDeRouteDTO dto) {
        FeuilleDeRoute existing = feuilleDeRouteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feuille de route non trouvée avec id : " + id));

        existing.setNumeroFeuille(dto.getNumeroFeuille());
        existing.setNombreParticipants(dto.getNombreParticipants());
        existing.setDestination(dto.getDestination());
        existing.setObjetMission(dto.getObjetMission());

        FeuilleDeRoute updated = feuilleDeRouteRepository.save(existing);
        return toDTO(updated);
    }

    // Supprimer une feuille de route (et la détacher de la mission)
    @Transactional
    public void deleteFeuilleDeRoute(Long id) {
        FeuilleDeRoute feuille = feuilleDeRouteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feuille de route non trouvée avec id : " + id));

        Mission mission = feuille.getMission();
        if (mission != null) {
            mission.setFeuilleDeRoute(null);
            missionRepository.save(mission);
        }
        feuilleDeRouteRepository.delete(feuille);
    }
}