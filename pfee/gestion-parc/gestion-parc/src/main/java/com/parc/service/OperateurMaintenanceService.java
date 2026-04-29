package com.parc.service;

import com.parc.domain.enums.Role;
import com.parc.domain.enums.StatutMaintenance;
import com.parc.domain.enums.TypeNotification;
import com.parc.dto.MaintenanceDTO;
import com.parc.dto.OperateurMaintenanceDTO;
import com.parc.domain.entity.*;
import com.parc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OperateurMaintenanceService {

    private final OperateurMaintenanceRepository operateurRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final GarageRepository garageRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationService notificationService;
    private final PdfGenerationService pdfGenerationService;

    private OperateurMaintenanceDTO toDTO(OperateurMaintenance o) {
        OperateurMaintenanceDTO dto = new OperateurMaintenanceDTO();
        dto.setId(o.getId());
        dto.setNom(o.getNom());
        dto.setPrenom(o.getPrenom());
        dto.setEmail(o.getEmail());
        dto.setTelephone(o.getTelephone());
        dto.setSpecialite(o.getSpecialite());
        dto.setDateEmbauche(o.getDateEmbauche());
        dto.setDisponibilite(o.getDisponibilite());
        if (o.getGarage() != null) {
            dto.setGarageId(o.getGarage().getId());
            dto.setGarageNom(o.getGarage().getSpecialite() + " - " + o.getGarage().getAdresse());
        }
        return dto;
    }

    private OperateurMaintenance toEntity(OperateurMaintenanceDTO dto) {
        OperateurMaintenance o = new OperateurMaintenance();
        o.setNom(dto.getNom());
        o.setPrenom(dto.getPrenom());
        o.setEmail(dto.getEmail());
        o.setTelephone(dto.getTelephone());
        o.setSpecialite(dto.getSpecialite());
        o.setDateEmbauche(dto.getDateEmbauche());
        o.setDisponibilite(dto.getDisponibilite());
        if (dto.getGarageId() != null) {
            Garage garage = garageRepository.findById(dto.getGarageId())
                    .orElseThrow(() -> new RuntimeException("Garage non trouvé"));
            o.setGarage(garage);
        }
        return o;
    }

    private MaintenanceDTO toMaintenanceDTO(Maintenance m) {
        MaintenanceDTO dto = new MaintenanceDTO();
        dto.setId(m.getId());
        dto.setType(m.getType());
        dto.setStatut(m.getStatut());
        dto.setDatePrevue(m.getDatePrevue());
        dto.setCout(m.getCout());
        dto.setOperateur(m.getOperateur());
        dto.setRapportProbleme(m.getRapportProbleme());
        dto.setDateRealisation(m.getDateRealisation());
        if (m.getVehicule() != null) {
            dto.setVehiculeId(m.getVehicule().getId());
            dto.setVehiculeMatricule(m.getVehicule().getMatricule());
        }
        if (m.getGarage() != null) {
            dto.setGarageId(m.getGarage().getId());
            dto.setGarageNom(m.getGarage().getSpecialite() + " - " + m.getGarage().getAdresse());
        }
        return dto;
    }

    // --- CRUD ---
    public OperateurMaintenanceDTO create(OperateurMaintenanceDTO dto) {
        OperateurMaintenance o = toEntity(dto);
        o.setRole(Role.OPERATEUR_MAINTENANCE);
        o.setActif(true);
        return toDTO(operateurRepository.save(o));
    }

    public List<OperateurMaintenanceDTO> getAll() {
        return operateurRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public OperateurMaintenanceDTO getById(Long id) {
        return operateurRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Opérateur non trouvé"));
    }

    public OperateurMaintenanceDTO update(Long id, OperateurMaintenanceDTO dto) {
        OperateurMaintenance existing = operateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Opérateur non trouvé"));
        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setEmail(dto.getEmail());
        existing.setTelephone(dto.getTelephone());
        existing.setSpecialite(dto.getSpecialite());
        existing.setDateEmbauche(dto.getDateEmbauche());
        existing.setDisponibilite(dto.getDisponibilite());
        if (dto.getGarageId() != null) {
            Garage garage = garageRepository.findById(dto.getGarageId())
                    .orElseThrow(() -> new RuntimeException("Garage non trouvé"));
            existing.setGarage(garage);
        } else {
            existing.setGarage(null);
        }
        return toDTO(operateurRepository.save(existing));
    }

    public void delete(Long id) {
        operateurRepository.deleteById(id);
    }

    // --- Fonctions métier ---
    public List<MaintenanceDTO> consulterDemandesMaintenance(OperateurMaintenance operateur) {
        return maintenanceRepository.findByGarageIdAndStatutIn(
                operateur.getGarage().getId(),
                List.of(StatutMaintenance.PLANIFIEE, StatutMaintenance.CONFIRMEE)
        ).stream().map(this::toMaintenanceDTO).collect(Collectors.toList());
    }

    @Transactional
    public MaintenanceDTO validerMaintenance(Long maintenanceId, OperateurMaintenance operateur) {
        Maintenance m = getMaintenanceForOperateur(maintenanceId, operateur);
        if (m.getStatut() != StatutMaintenance.PLANIFIEE) {
            throw new RuntimeException("Seule une maintenance planifiée peut être validée");
        }
        m.setStatut(StatutMaintenance.CONFIRMEE);
        return toMaintenanceDTO(maintenanceRepository.save(m));
    }

    @Transactional
    public MaintenanceDTO planifierMaintenance(Long maintenanceId, OperateurMaintenance operateur) {
        Maintenance m = getMaintenanceForOperateur(maintenanceId, operateur);
        m.setStatut(StatutMaintenance.PLANIFIEE);
        return toMaintenanceDTO(maintenanceRepository.save(m));
    }

    @Transactional
    public MaintenanceDTO realiserMaintenance(Long maintenanceId, OperateurMaintenance operateur) {
        Maintenance m = getMaintenanceForOperateur(maintenanceId, operateur);
        if (m.getStatut() != StatutMaintenance.CONFIRMEE) {
            throw new RuntimeException("Seule une maintenance confirmée peut être réalisée");
        }
        m.setStatut(StatutMaintenance.TERMINEE);
        return toMaintenanceDTO(maintenanceRepository.save(m));
    }

    @Transactional
    public MaintenanceDTO rapporterProbleme(Long maintenanceId, OperateurMaintenance operateur) {
        Maintenance m = getMaintenanceForOperateur(maintenanceId, operateur);
        m.setStatut(StatutMaintenance.PROBLEME);
        return toMaintenanceDTO(maintenanceRepository.save(m));
    }

    public List<MaintenanceDTO> consulterHistoriqueMaintenance(OperateurMaintenance operateur) {
        return maintenanceRepository.findByGarageIdAndStatutIn(
                operateur.getGarage().getId(),
                List.of(StatutMaintenance.TERMINEE, StatutMaintenance.ANNULEE)
        ).stream().map(this::toMaintenanceDTO).collect(Collectors.toList());
    }

    private Maintenance getMaintenanceForOperateur(Long maintenanceId, OperateurMaintenance operateur) {
        Maintenance m = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new RuntimeException("Maintenance non trouvée"));
        if (!m.getGarage().getId().equals(operateur.getGarage().getId())) {
            throw new RuntimeException("Cette maintenance n'appartient pas à votre garage");
        }
        return m;
    }

    public OperateurMaintenance findByEmail(String email) {
        return operateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Opérateur non trouvé"));
    }

    public List<OperateurMaintenanceDTO> getByDisponibilite(String disponibilite) {
        return operateurRepository.findByDisponibilite(disponibilite).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void envoyerRapportMaintenanceAuChef(Long maintenanceId, String emailOperateur) throws Exception {
        Utilisateur utilisateur = utilisateurRepository.findByEmailIgnoreCase(emailOperateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouve"));
        if (utilisateur.getRole() != Role.OPERATEUR_MAINTENANCE) {
            throw new RuntimeException("Seul un operateur maintenance peut envoyer un rapport");
        }

        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new RuntimeException("Maintenance non trouvee"));

        OperateurMaintenance operateur = operateurRepository.findById(utilisateur.getId())
                .orElseThrow(() -> new RuntimeException("Operateur maintenance non trouve"));
        getMaintenanceForOperateur(maintenanceId, operateur);

        if (maintenance.getVehicule() == null || maintenance.getVehicule().getParc() == null
                || maintenance.getVehicule().getParc().getChef() == null) {
            throw new RuntimeException("Aucun chef de parc associe a cette maintenance");
        }

        Long chefId = maintenance.getVehicule().getParc().getChef().getId();
        String lienTelechargement = "/api/operateurs-maintenance/maintenances/" + maintenanceId + "/rapport-pdf";
        String message = "Rapport PDF de maintenance disponible pour le vehicule "
                + (maintenance.getVehicule() != null ? maintenance.getVehicule().getMatricule() : "N/A")
                + ". Cliquez pour telecharger.";

        notificationService.envoyerNotification(
                chefId,
                "Nouveau rapport maintenance PDF",
                message,
                TypeNotification.ALERTE_GENERALE,
                lienTelechargement,
                null,
                maintenanceId
        );
    }

    public byte[] telechargerRapportMaintenancePdf(Long maintenanceId, String emailUtilisateur) throws Exception {
        Utilisateur utilisateur = utilisateurRepository.findByEmailIgnoreCase(emailUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouve"));
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new RuntimeException("Maintenance non trouvee"));

        if (!peutTelechargerRapport(utilisateur, maintenance)) {
            throw new RuntimeException("Acces refuse a ce rapport");
        }

        return pdfGenerationService.generateMaintenanceReportPdf(maintenance, utilisateur);
    }

    private boolean peutTelechargerRapport(Utilisateur utilisateur, Maintenance maintenance) {
        if (utilisateur.getRole() == Role.CHEF) {
            return maintenance.getVehicule() != null
                    && maintenance.getVehicule().getParc() != null
                    && maintenance.getVehicule().getParc().getChef() != null
                    && maintenance.getVehicule().getParc().getChef().getId().equals(utilisateur.getId());
        }

        if (utilisateur.getRole() == Role.OPERATEUR_MAINTENANCE) {
            OperateurMaintenance operateur = operateurRepository.findById(utilisateur.getId()).orElse(null);
            return operateur != null
                    && maintenance.getGarage() != null
                    && operateur.getGarage() != null
                    && maintenance.getGarage().getId().equals(operateur.getGarage().getId());
        }

        return false;
    }
}