package com.parc.service;

import com.parc.dto.MaintenanceDTO;
import com.parc.domain.entity.*;
import com.parc.domain.enums.Role;
import com.parc.domain.enums.StatutMaintenance;
import com.parc.domain.enums.TypeNotification;
import com.parc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final VehiculeRepository vehiculeRepository;
    private final GarageRepository garageRepository;
    private final OperateurMaintenanceRepository operateurMaintenanceRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationService notificationService;
    private final SmsService smsService;
    private final PushNotificationService pushService;

    private MaintenanceDTO toDTO(Maintenance m) {
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

    private Maintenance toEntity(MaintenanceDTO dto) {
        Maintenance m = new Maintenance();
        m.setType(dto.getType());
        m.setStatut(dto.getStatut() != null ? dto.getStatut() : StatutMaintenance.PLANIFIEE);
        m.setDatePrevue(dto.getDatePrevue());
        m.setCout(dto.getCout());
        m.setOperateur(dto.getOperateur());
        m.setRapportProbleme(dto.getRapportProbleme());
        m.setDateRealisation(dto.getDateRealisation());
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
            m.setVehicule(vehicule);
        }
        if (dto.getGarageId() != null) {
            Garage garage = garageRepository.findById(dto.getGarageId())
                    .orElseThrow(() -> new RuntimeException("Garage non trouvé"));
            m.setGarage(garage);
        }
        return m;
    }

    @Transactional
    public MaintenanceDTO create(MaintenanceDTO dto) {
        Maintenance maintenance = toEntity(dto);
        maintenance = maintenanceRepository.save(maintenance);

        Garage garage = maintenance.getGarage();
        if (garage != null && garage.getTelephone() != null && !garage.getTelephone().isEmpty()) {
            String message = String.format(
                "Maintenance prévue pour le véhicule %s le %s. Type: %s",
                maintenance.getVehicule().getMatricule(),
                maintenance.getDatePrevue(),
                maintenance.getType()
            );
            smsService.sendSms(garage.getTelephone(), message);
        }
        if (garage != null && garage.getFcmToken() != null && !garage.getFcmToken().isEmpty()) {
            String title = "Nouvelle maintenance";
            String body = String.format(
                "Maintenance prévue pour le véhicule %s le %s.\nType: %s",
                maintenance.getVehicule().getMatricule(),
                maintenance.getDatePrevue(),
                maintenance.getType()
            );
            pushService.sendPush(garage.getFcmToken(), title, body);
        }
        notifyAssignedOperateur(maintenance);
        return toDTO(maintenance);
    }

    public List<MaintenanceDTO> getAll() {
        return maintenanceRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MaintenanceDTO getById(Long id) {
        return maintenanceRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Maintenance non trouvée"));
    }

    public List<MaintenanceDTO> getByVehicule(Long vehiculeId) {
        return maintenanceRepository.findByVehiculeId(vehiculeId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<MaintenanceDTO> getByGarage(Long garageId) {
        return maintenanceRepository.findByGarageId(garageId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public MaintenanceDTO update(Long id, MaintenanceDTO dto) {
        Maintenance existing = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance non trouvée"));
        String previousOperateur = existing.getOperateur();
        existing.setType(dto.getType());
        existing.setStatut(dto.getStatut());
        existing.setDatePrevue(dto.getDatePrevue());
        existing.setCout(dto.getCout());
        existing.setOperateur(dto.getOperateur());
        existing.setRapportProbleme(dto.getRapportProbleme());
        existing.setDateRealisation(dto.getDateRealisation());
        if (dto.getVehiculeId() != null) {
            Vehicule vehicule = vehiculeRepository.findById(dto.getVehiculeId())
                    .orElseThrow(() -> new RuntimeException("Véhicule non trouvé"));
            existing.setVehicule(vehicule);
        } else {
            existing.setVehicule(null);
        }
        if (dto.getGarageId() != null) {
            Garage garage = garageRepository.findById(dto.getGarageId())
                    .orElseThrow(() -> new RuntimeException("Garage non trouvé"));
            existing.setGarage(garage);
        } else {
            existing.setGarage(null);
        }
        Maintenance saved = maintenanceRepository.save(existing);
        boolean operateurChanged = previousOperateur == null
                ? saved.getOperateur() != null && !saved.getOperateur().isBlank()
                : !previousOperateur.equals(saved.getOperateur());
        if (operateurChanged) {
            notifyAssignedOperateur(saved);
        }
        return toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        Maintenance existing = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance non trouvée"));
        maintenanceRepository.delete(existing);
    }

    private void notifyAssignedOperateur(Maintenance maintenance) {
        String operateurValue = maintenance.getOperateur();
        if (operateurValue == null || operateurValue.isBlank()) {
            notifyOperateurs(maintenance, resolveOperateursByGarageOrAll(maintenance));
            return;
        }

        Utilisateur operateur = resolveOperateur(operateurValue.trim());
        List<Utilisateur> destinataires = new ArrayList<>();
        if (operateur != null) {
            destinataires.add(operateur);
        } else {
            destinataires.addAll(resolveOperateursByGarageOrAll(maintenance));
        }

        notifyOperateurs(maintenance, destinataires);
    }

    private void notifyOperateurs(Maintenance maintenance, List<Utilisateur> operateurs) {
        if (operateurs == null || operateurs.isEmpty()) {
            return;
        }
        Set<Long> vu = new LinkedHashSet<>();
        String matricule = maintenance.getVehicule() != null ? maintenance.getVehicule().getMatricule() : "N/A";
        String message = String.format(
                "Une maintenance (%s) vous a ete affectee pour le vehicule %s, date prevue: %s.",
                maintenance.getType(),
                matricule,
                maintenance.getDatePrevue()
        );
        for (Utilisateur op : operateurs) {
            if (op == null || op.getId() == null || !vu.add(op.getId())) {
                continue;
            }
            notificationService.envoyerNotification(
                    op.getId(),
                    "Nouvelle maintenance affectee",
                    message,
                    TypeNotification.MAINTENANCE_PROGRAMMEE,
                    "/operateur-maintenance/maintenances",
                    null,
                    maintenance.getId()
            );
        }
    }

    private Utilisateur resolveOperateur(String operateurValue) {
        String normalizedQuery = normalizeText(operateurValue);
        return getAllOperateurs().stream()
                .filter(op -> {
                    String nom = normalizeText(op.getNom());
                    String prenom = normalizeText(op.getPrenom());
                    String fullName = normalizeText(op.getNom() + " " + op.getPrenom());
                    String reverseFullName = normalizeText(op.getPrenom() + " " + op.getNom());
                    String email = normalizeText(op.getEmail());

                    return fullName.equals(normalizedQuery)
                            || reverseFullName.equals(normalizedQuery)
                            || nom.equals(normalizedQuery)
                            || prenom.equals(normalizedQuery)
                            || email.equals(normalizedQuery)
                            || fullName.contains(normalizedQuery)
                            || reverseFullName.contains(normalizedQuery);
                })
                .findFirst()
                .orElse(null);
    }

    private List<Utilisateur> resolveOperateursByGarageOrAll(Maintenance maintenance) {
        if (maintenance.getGarage() == null) {
            return getAllOperateurs();
        }

        List<Utilisateur> byGarage = operateurMaintenanceRepository.findByGarage_Id(maintenance.getGarage().getId())
                .stream()
                .map(op -> (Utilisateur) op)
                .collect(Collectors.toList());

        return byGarage.isEmpty() ? getAllOperateurs() : byGarage;
    }

    private List<Utilisateur> getAllOperateurs() {
        return utilisateurRepository.findByRole(Role.OPERATEUR_MAINTENANCE).stream()
                .filter(Utilisateur::isActif)
                .collect(Collectors.toList());
    }

    private String normalizeText(String value) {
        if (value == null) {
            return "";
        }
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return normalized.trim().toLowerCase(Locale.ROOT);
    }
}