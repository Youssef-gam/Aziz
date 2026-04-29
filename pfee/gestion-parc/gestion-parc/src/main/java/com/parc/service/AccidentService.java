package com.parc.service;

import com.parc.dto.AccidentDTO;
import com.parc.domain.entity.Accident;
import com.parc.domain.entity.Chauffeur;
import com.parc.domain.entity.Vehicule;
import com.parc.dto.MaintenanceDTO;
import com.parc.domain.enums.StatutMaintenance;
import com.parc.domain.entity.Mission;
import com.parc.repository.AccidentRepository;
import com.parc.repository.ChauffeurRepository;
import com.parc.repository.VehiculeRepository;
import com.parc.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccidentService {

    private final AccidentRepository accidentRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final MissionRepository missionRepository;
    private final MaintenanceService maintenanceService;
    private final NotificationService notificationService;

    // ========== MAPPING ==========
    private AccidentDTO toDTO(Accident accident) {
        AccidentDTO dto = new AccidentDTO();
        dto.setId(accident.getId());
        dto.setDateAccident(accident.getDateAccident());
        dto.setLieuAccident(accident.getLieuAccident());
        dto.setPersonnesImpliquees(accident.getPersonnesImpliquees());
        dto.setDescription(accident.getDescription());
        dto.setDegats(accident.getDegats());
        dto.setPhoto(accident.getPhoto());
        dto.setStatut(accident.getStatut());
        dto.setDateDeclaration(accident.getDateDeclaration());

        if (accident.getMission() != null) {
            dto.setMissionId(accident.getMission().getId());
        }
        if (accident.getChauffeur() != null) {
            dto.setChauffeurId(accident.getChauffeur().getId());
            dto.setChauffeurNom(accident.getChauffeur().getNom() + " " + accident.getChauffeur().getPrenom());
        }
        if (accident.getVehicule() != null) {
            dto.setVehiculeId(accident.getVehicule().getId());
            dto.setVehiculeMatricule(accident.getVehicule().getMatricule());
        }
        return dto;
    }

    private Accident toEntity(AccidentDTO dto) {
        Accident accident = new Accident();
        accident.setDateAccident(dto.getDateAccident());
        accident.setLieuAccident(dto.getLieuAccident());
        accident.setPersonnesImpliquees(dto.getPersonnesImpliquees());
        accident.setDescription(dto.getDescription());
        accident.setDegats(dto.getDegats());
        accident.setPhoto(dto.getPhoto());
        accident.setStatut(dto.getStatut() != null ? dto.getStatut() : "DÉCLARÉ");
        // La date de déclaration est générée automatiquement
        
        if (dto.getMissionId() != null) {
            Mission mission = missionRepository.findById(dto.getMissionId())
                    .orElseThrow(() -> new RuntimeException("Mission non trouvée avec id : " + dto.getMissionId()));
            accident.setMission(mission);
        }
        
        if (dto.getChauffeurId() != null) {
            Chauffeur chauffeur = chauffeurRepository.findById(dto.getChauffeurId())
                    .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé avec id : " + dto.getChauffeurId()));
            accident.setChauffeur(chauffeur);
        }
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new RuntimeException("Véhicule non trouvé avec id : " + dto.getVehiculeId()));
            accident.setVehicule(vehicule);
        }
        return accident;
    }

    // ========== CRUD ==========
    @Transactional
    public AccidentDTO create(AccidentDTO dto) {
        // Validation : le chauffeur et le véhicule doivent exister
        Accident accident = toEntity(dto);
        accident = accidentRepository.save(accident);

        // 1. Notification au chef de parc
        String message = String.format(
            "Accident déclaré pour le véhicule %s par %s %s. Lieu : %s",
            accident.getVehicule().getMatricule(),
            accident.getChauffeur().getNom(),
            accident.getChauffeur().getPrenom(),
            accident.getLieuAccident()
        );
        notificationService.envoyerNotificationChefParc(message);

        // 2. Déclenchement automatique d'une maintenance corrective
       MaintenanceDTO maintenanceDTO = new MaintenanceDTO();
maintenanceDTO.setType("Accident");
maintenanceDTO.setStatut(StatutMaintenance.PLANIFIEE);
maintenanceDTO.setDatePrevue(LocalDate.now().plusDays(2));
maintenanceDTO.setOperateur("À assigner");
maintenanceDTO.setVehiculeId(accident.getVehicule().getId());
maintenanceService.create(maintenanceDTO);

        return toDTO(accident);
    }

    public List<AccidentDTO> getAll() {
        return accidentRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public AccidentDTO getById(Long id) {
        return accidentRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Accident non trouvé avec id : " + id));
    }

    public List<AccidentDTO> getByChauffeur(Long chauffeurId) {
        return accidentRepository.findByChauffeurId(chauffeurId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<AccidentDTO> getByVehicule(Long vehiculeId) {
        return accidentRepository.findByVehiculeId(vehiculeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccidentDTO update(Long id, AccidentDTO dto) {
        Accident existing = accidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accident non trouvé avec id : " + id));

        existing.setDateAccident(dto.getDateAccident());
        existing.setLieuAccident(dto.getLieuAccident());
        existing.setPersonnesImpliquees(dto.getPersonnesImpliquees());
        existing.setDescription(dto.getDescription());
        existing.setDegats(dto.getDegats());
        existing.setPhoto(dto.getPhoto());
        existing.setStatut(dto.getStatut());

        if (dto.getMissionId() != null) {
            Mission mission = missionRepository.findById(dto.getMissionId())
                    .orElseThrow(() -> new RuntimeException("Mission non trouvée"));
            existing.setMission(mission);
        } else {
            existing.setMission(null);
        }

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

        return toDTO(accidentRepository.save(existing));
    }

    @Transactional
    public void delete(Long id) {
        Accident existing = accidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Accident non trouvé avec id : " + id));
        accidentRepository.delete(existing);
    }
}