package com.parc.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "operateurs_maintenance")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class OperateurMaintenance extends Utilisateur {

    private String specialite;
    private LocalDate dateEmbauche;
    private String disponibilite;

    @ManyToOne   // ← annotation corrigée
    @JoinColumn(name = "garage_id")
    private Garage garage;

    public OperateurMaintenance() {}

    // Getters / Setters
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public String getDisponibilite() { return disponibilite; }
    public void setDisponibilite(String disponibilite) { this.disponibilite = disponibilite; }

    public Garage getGarage() { return garage; }
    public void setGarage(Garage garage) { this.garage = garage; }
}