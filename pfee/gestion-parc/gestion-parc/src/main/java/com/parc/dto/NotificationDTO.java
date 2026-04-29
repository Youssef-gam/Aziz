package com.parc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parc.domain.enums.TypeNotification;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class NotificationDTO {

    private Long id;
    private String titre;
    private String message;
    private String lien;
    private TypeNotification type;
    private String statut;
    private LocalDateTime dateEnvoi;
    private Long destinataireId;
    private String destinataireNom;
    private Long missionId;
    private Long maintenanceId;
    
    // Détails de la mission
    private String missionDestination;
    private LocalDate missionDateDebut;
    private LocalDate missionDateFin;
    private String missionStatut;
    private String missionDescription;

    public NotificationDTO() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getLien() { return lien; }
    public void setLien(String lien) { this.lien = lien; }

    public TypeNotification getType() { return type; }
    public void setType(TypeNotification type) { this.type = type; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    /** Exposé pour le frontend (cloche notifications) — aligné sur {@link #statut}. */
    @JsonProperty("lue")
    public boolean isLue() {
        return "LUE".equals(statut);
    }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public Long getDestinataireId() { return destinataireId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }

    public String getDestinataireNom() { return destinataireNom; }
    public void setDestinataireNom(String destinataireNom) { this.destinataireNom = destinataireNom; }

    public Long getMissionId() { return missionId; }
    public void setMissionId(Long missionId) { this.missionId = missionId; }

    public Long getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(Long maintenanceId) { this.maintenanceId = maintenanceId; }
    
    // Mission Details
    public String getMissionDestination() { return missionDestination; }
    public void setMissionDestination(String missionDestination) { this.missionDestination = missionDestination; }

    public LocalDate getMissionDateDebut() { return missionDateDebut; }
    public void setMissionDateDebut(LocalDate missionDateDebut) { this.missionDateDebut = missionDateDebut; }

    public LocalDate getMissionDateFin() { return missionDateFin; }
    public void setMissionDateFin(LocalDate missionDateFin) { this.missionDateFin = missionDateFin; }

    public String getMissionStatut() { return missionStatut; }
    public void setMissionStatut(String missionStatut) { this.missionStatut = missionStatut; }

    public String getMissionDescription() { return missionDescription; }
    public void setMissionDescription(String missionDescription) { this.missionDescription = missionDescription; }
}