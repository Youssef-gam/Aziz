package com.parc.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "accidents")
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateAccident;
    private String lieuAccident;
    private String personnesImpliquees;
    private String description;
    private String degats;
    private String photo;
    private String statut = "DÉCLARÉ";

    private LocalDateTime dateDeclaration = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "chauffeur_id")
    private Chauffeur chauffeur;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    // Constructeurs
    public Accident() {}

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

    public Chauffeur getChauffeur() { return chauffeur; }
    public void setChauffeur(Chauffeur chauffeur) { this.chauffeur = chauffeur; }

    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }

    public Mission getMission() { return mission; }
    public void setMission(Mission mission) { this.mission = mission; }
}