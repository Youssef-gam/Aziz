package com.parc.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AmendeDTO {
    private Long id;
    private Double montant;
    private LocalDate dateInfraction;
    private String lieuInfraction;
    private String motif;
    private String description;
    private String photo;
    private String statut;
    private LocalDateTime dateDeclaration;
    private Boolean payee;
    private Long chauffeurId;
    private String chauffeurNom;
    private Long vehiculeId;
    private String vehiculeMatricule;

    public AmendeDTO() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }

    public LocalDate getDateInfraction() { return dateInfraction; }
    public void setDateInfraction(LocalDate dateInfraction) { this.dateInfraction = dateInfraction; }

    public String getLieuInfraction() { return lieuInfraction; }
    public void setLieuInfraction(String lieuInfraction) { this.lieuInfraction = lieuInfraction; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getDateDeclaration() { return dateDeclaration; }
    public void setDateDeclaration(LocalDateTime dateDeclaration) { this.dateDeclaration = dateDeclaration; }

    public Boolean getPayee() { return payee; }
    public void setPayee(Boolean payee) { this.payee = payee; }

    public Long getChauffeurId() { return chauffeurId; }
    public void setChauffeurId(Long chauffeurId) { this.chauffeurId = chauffeurId; }

    public String getChauffeurNom() { return chauffeurNom; }
    public void setChauffeurNom(String chauffeurNom) { this.chauffeurNom = chauffeurNom; }

    public Long getVehiculeId() { return vehiculeId; }
    public void setVehiculeId(Long vehiculeId) { this.vehiculeId = vehiculeId; }

    public String getVehiculeMatricule() { return vehiculeMatricule; }
    public void setVehiculeMatricule(String vehiculeMatricule) { this.vehiculeMatricule = vehiculeMatricule; }
}