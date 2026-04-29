package com.parc.service;

import com.parc.domain.entity.*;
import com.parc.domain.enums.StatutMaintenance;
import com.parc.domain.enums.StatutMission;
import com.parc.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssistantAIService {

    private final ChatClient chatClient;
    private final MissionRepository missionRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final AccidentRepository accidentRepository;

    public AssistantAIService(ChatClient.Builder chatClientBuilder,
                               MissionRepository missionRepository,
                               VehiculeRepository vehiculeRepository,
                               ChauffeurRepository chauffeurRepository,
                               MaintenanceRepository maintenanceRepository,
                               AccidentRepository accidentRepository) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        Vous êtes un assistant IA expert en gestion de parc automobile.
                        Vous aidez les chefs de parc à analyser leurs données de flotte.
                        Répondez toujours en français, de manière concise et structurée.
                        Utilisez des emojis pour la lisibilité.
                        Basez-vous uniquement sur les données fournies dans le contexte.
                        """)
                .build();
        this.missionRepository = missionRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.chauffeurRepository = chauffeurRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.accidentRepository = accidentRepository;
    }

    public String processUserQuery(String question, Map<String, Object> context) {
        try {
            String dbContext = buildRealContext();
            String prompt = "DONNÉES DU SYSTÈME:\n" + dbContext + "\n\nQUESTION: " + question;

            log.info("Envoi question à Groq: {}", question);
            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
            log.info("Réponse Groq reçue");
            return response;
        } catch (Exception e) {
            log.error("Erreur lors de l'appel Groq", e);
            return "❌ Erreur: " + e.getMessage();
        }
    }

    public boolean isConnected() {
        try {
            chatClient.prompt().user("ping").call().content();
            return true;
        } catch (Exception e) {
            log.warn("Groq non accessible: {}", e.getMessage());
            return false;
        }
    }

    private String buildRealContext() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(7);
        StringBuilder ctx = new StringBuilder();

        ctx.append("📅 Date du jour: ").append(today).append("\n\n");

        // --- Missions ---
        List<Mission> allMissions = missionRepository.findAll();

        List<Mission> activeMissions = allMissions.stream()
                .filter(m -> m.getStatut() == StatutMission.PLANIFIEE
                          || m.getStatut() == StatutMission.EN_COURS)
                .collect(Collectors.toList());

        List<Mission> todayMissions = allMissions.stream()
                .filter(m -> m.getDateDebut() != null && m.getDateFin() != null
                          && !m.getDateDebut().isAfter(today)
                          && !m.getDateFin().isBefore(today))
                .collect(Collectors.toList());

        ctx.append("📍 MISSIONS ACTIVES (").append(activeMissions.size()).append("):\n");
        activeMissions.forEach(m -> ctx
                .append("  - Mission #").append(m.getId())
                .append(": ").append(m.getDescription())
                .append(" → ").append(m.getDestination())
                .append(" | Statut: ").append(m.getStatut())
                .append(" | Chauffeur: ").append(m.getChauffeur() != null
                        ? m.getChauffeur().getNom() + " " + m.getChauffeur().getPrenom()
                        : "Non assigné")
                .append(" | Véhicule: ").append(m.getVehicule() != null
                        ? m.getVehicule().getMatricule() : "Non assigné")
                .append(" | ").append(m.getDateDebut()).append(" → ").append(m.getDateFin())
                .append("\n"));

        ctx.append("\n📅 MISSIONS D'AUJOURD'HUI (").append(todayMissions.size()).append("):\n");
        if (todayMissions.isEmpty()) {
            ctx.append("  Aucune mission aujourd'hui\n");
        } else {
            todayMissions.forEach(m -> ctx
                    .append("  - ").append(m.getDescription())
                    .append(" vers ").append(m.getDestination())
                    .append(" | ").append(m.getStatut()).append("\n"));
        }

        // --- Maintenances ---
        List<Maintenance> activeMaint = maintenanceRepository.findByStatut(StatutMaintenance.EN_COURS);
        activeMaint.addAll(maintenanceRepository.findByStatut(StatutMaintenance.PLANIFIEE));
        activeMaint.addAll(maintenanceRepository.findByStatut(StatutMaintenance.CONFIRMEE));
        activeMaint.addAll(maintenanceRepository.findByStatut(StatutMaintenance.PROBLEME));

        ctx.append("\n🔧 MAINTENANCES EN COURS/PLANIFIÉES (").append(activeMaint.size()).append("):\n");
        if (activeMaint.isEmpty()) {
            ctx.append("  Aucune maintenance active\n");
        } else {
            activeMaint.forEach(m -> ctx
                    .append("  - Véhicule: ").append(m.getVehicule() != null
                            ? m.getVehicule().getMatricule() : "?")
                    .append(" | Type: ").append(m.getType())
                    .append(" | Statut: ").append(m.getStatut())
                    .append(" | Date prévue: ").append(m.getDatePrevue())
                    .append(m.getCout() != null ? " | Coût: " + m.getCout() + " TND" : "")
                    .append(m.getRapportProbleme() != null ? " | Problème: " + m.getRapportProbleme() : "")
                    .append("\n"));
        }

        // --- Chauffeurs ---
        List<Chauffeur> chauffeurs = chauffeurRepository.findAll();
        ctx.append("\n👥 CHAUFFEURS (").append(chauffeurs.size()).append("):\n");
        chauffeurs.forEach(c -> {
            long missionsCount = allMissions.stream()
                    .filter(m -> m.getChauffeur() != null && m.getChauffeur().getId().equals(c.getId()))
                    .count();
            long activeMissionsCount = activeMissions.stream()
                    .filter(m -> m.getChauffeur() != null && m.getChauffeur().getId().equals(c.getId()))
                    .count();
            ctx.append("  - ").append(c.getNom()).append(" ").append(c.getPrenom())
               .append(" | Permis: ").append(c.getNumeroPermis())
               .append(" | Expire: ").append(c.getDateExpirationPermis())
               .append(" | Missions totales: ").append(missionsCount)
               .append(" | Missions actives: ").append(activeMissionsCount)
               .append("\n");
        });

        // --- Accidents cette semaine ---
        List<Accident> recentAccidents = accidentRepository.findAll().stream()
                .filter(a -> a.getDateAccident() != null
                          && !a.getDateAccident().isBefore(weekStart))
                .collect(Collectors.toList());

        ctx.append("\n⚠️ INCIDENTS/ACCIDENTS (7 derniers jours - ")
           .append(recentAccidents.size()).append("):\n");
        if (recentAccidents.isEmpty()) {
            ctx.append("  Aucun incident cette semaine\n");
        } else {
            recentAccidents.forEach(a -> ctx
                    .append("  - ").append(a.getDateAccident())
                    .append(" | Lieu: ").append(a.getLieuAccident())
                    .append(" | Chauffeur: ").append(a.getChauffeur() != null
                            ? a.getChauffeur().getNom() + " " + a.getChauffeur().getPrenom() : "?")
                    .append(" | Véhicule: ").append(a.getVehicule() != null
                            ? a.getVehicule().getMatricule() : "?")
                    .append(" | Dégâts: ").append(a.getDegats())
                    .append("\n"));
        }

        // --- Véhicules ---
        List<Vehicule> vehicules = vehiculeRepository.findAll();
        ctx.append("\n🚗 VÉHICULES (").append(vehicules.size()).append("):\n");
        vehicules.forEach(v -> ctx
                .append("  - Matricule: ").append(v.getMatricule())
                .append(" | Marque: ").append(v.getMarque() != null ? v.getMarque() : "?")
                .append(" | Modèle: ").append(v.getModele() != null ? v.getModele() : "?")
                .append(" | Statut: ").append(v.getStatut() != null ? v.getStatut() : "?")
                .append(v.getKilometre() != null ? " | Km: " + v.getKilometre() : "")
                .append(v.getTypeCarburant() != null ? " | Carburant: " + v.getTypeCarburant() : "")
                .append(v.getCouleur() != null ? " | Couleur: " + v.getCouleur() : "")
                .append(v.getDateExpirationAssurance() != null ? " | Assurance expire: " + v.getDateExpirationAssurance() : "")
                .append(v.getDateExpirationVisiteTechnique() != null ? " | Visite technique expire: " + v.getDateExpirationVisiteTechnique() : "")
                .append("\n"));

        // --- Statistiques globales ---
        long enCours = allMissions.stream().filter(m -> m.getStatut() == StatutMission.EN_COURS).count();
        long terminees = allMissions.stream().filter(m -> m.getStatut() == StatutMission.TERMINEE).count();
        long permisExpires = chauffeurs.stream()
                .filter(c -> c.getDateExpirationPermis() != null && c.getDateExpirationPermis().isBefore(today))
                .count();

        ctx.append("\n📊 STATISTIQUES GLOBALES:\n")
           .append("  - Total véhicules: ").append(vehicules.size()).append("\n")
           .append("  - Total chauffeurs: ").append(chauffeurs.size()).append("\n")
           .append("  - Total missions: ").append(allMissions.size()).append("\n")
           .append("  - Missions en cours: ").append(enCours).append("\n")
           .append("  - Missions terminées: ").append(terminees).append("\n")
           .append("  - Maintenances actives: ").append(activeMaint.size()).append("\n")
           .append("  - Permis expirés: ").append(permisExpires).append("\n");

        return ctx.toString();
    }
}
