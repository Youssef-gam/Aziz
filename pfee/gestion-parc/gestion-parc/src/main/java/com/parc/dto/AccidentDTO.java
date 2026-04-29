package com.parc.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AccidentDTO {
    private Long id;
    private LocalDate dateAccident;
    private String lieuAccident;
    private String personnesImpliquees;
    private String description;
    private String degats;
    private String photo;
    private String statut;
    private LocalDateTime dateDeclaration;
    private Long missionId;
    private Long chauffeurId;
    private String chauffeurNom;
    private Long vehiculeId;
    private String vehiculeMatricule;

    public AccidentDTO() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDateAccident() { return dateAccident; }
    public void setDateAccident(LocalDate dateAccident) { this.dateAccident = dateAccident; }

    public String getLieuAccident() { return lieuAccident; }
    public void setLieuAccident(String lieuAccident) { this.lieuAccident = lieuAccident; }

    public String getPersonnesImpliquees() { return personnesImpliquees; }
    public void setPersonnesImpliquees(String personnesImpliquees) { this.personnesImpliquees = personnesImpliquees; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDegats() { return degats; }
    public void setDegats(String degats) { this.degats = degats; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getDateDeclaration() { return dateDeclaration; }
    public void setDateDeclaration(LocalDateTime dateDeclaration) { this.dateDeclaration = dateDeclaration; }

    public Long getMissionId() { return missionId; }
    public void setMissionId(Long missionId) { this.missionId = missionId; }

    public Long getChauffeurId() { return chauffeurId; }
    public void setChauffeurId(Long chauffeurId) { this.chauffeurId = chauffeurId; }

    public String getChauffeurNom() { return chauffeurNom; }
    public void setChauffeurNom(String chauffeurNom) { this.chauffeurNom = chauffeurNom; }

    public Long getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(Long vehiculeId) { this.vehiculeId = vehiculeId; }

    public String getVehiculeMatricule() { return vehiculeMatricule; }
    public void setVehiculeMatricule(String vehiculeMatricule) { this.vehiculeMatricule = vehiculeMatricule; }
}