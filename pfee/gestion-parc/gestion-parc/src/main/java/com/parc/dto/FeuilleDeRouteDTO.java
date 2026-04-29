package com.parc.dto;

public class FeuilleDeRouteDTO {
    private Long id;
    private String numeroFeuille;
    private Integer nombreParticipants;
    private String destination;
    private String objetMission;
    private Long missionId;   // optionnel

    // Constructeurs
    public FeuilleDeRouteDTO() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFeuille() { return numeroFeuille; }
    public void setNumeroFeuille(String numeroFeuille) { this.numeroFeuille = numeroFeuille; }

    public Integer getNombreParticipants() { return nombreParticipants; }
    public void setNombreParticipants(Integer nombreParticipants) { this.nombreParticipants = nombreParticipants; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getObjetMission() { return objetMission; }
    public void setObjetMission(String objetMission) { this.objetMission = objetMission; }

    public Long getMissionId() { return missionId; }
    public void setMissionId(Long missionId) { this.missionId = missionId; }
}