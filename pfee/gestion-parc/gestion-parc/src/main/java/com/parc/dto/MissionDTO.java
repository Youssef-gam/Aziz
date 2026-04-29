package com.parc.dto;

import com.parc.domain.enums.StatutMission;
import java.time.LocalDate;

public class MissionDTO {
    private Long id;
    private String description;
    private String destination;
    private String objetALivrer;
    private Integer nombreDeParticipants;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private StatutMission statut;
    private Long vehiculeId;
    private String vehiculeMatricule;   // pour affichage
    private Long chauffeurId;
    private String chauffeurNom;         // pour affichage
    private FeuilleDeRouteDTO feuilleDeRoute;

    // Constructeurs
    public MissionDTO() {}
    public MissionDTO(Long id, String description, String destination, LocalDate dateDebut, LocalDate dateFin, StatutMission statut) {
        this.id = id;
        this.description = description;
        this.destination = destination;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getObjetALivrer() { return objetALivrer; }
    public void setObjetALivrer(String objetALivrer) { this.objetALivrer = objetALivrer; }

    public Integer getNombreDeParticipants() { return nombreDeParticipants; }
    public void setNombreDeParticipants(Integer nombreDeParticipants) { this.nombreDeParticipants = nombreDeParticipants; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public StatutMission getStatut() { return statut; }
    public void setStatut(StatutMission statut) { this.statut = statut; }

    public Long getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(Long vehiculeId) { this.vehiculeId = vehiculeId; }

    public String getVehiculeMatricule() { return vehiculeMatricule; }
    public void setVehiculeMatricule(String vehiculeMatricule) { this.vehiculeMatricule = vehiculeMatricule; }

    public Long getChauffeurId() { return chauffeurId; }
    public void setChauffeurId(Long chauffeurId) { this.chauffeurId = chauffeurId; }

    public String getChauffeurNom() { return chauffeurNom; }
    public void setChauffeurNom(String chauffeurNom) { this.chauffeurNom = chauffeurNom; }

    public FeuilleDeRouteDTO getFeuilleDeRoute() { return feuilleDeRoute; }
    public void setFeuilleDeRoute(FeuilleDeRouteDTO feuilleDeRoute) { this.feuilleDeRoute = feuilleDeRoute; }
}