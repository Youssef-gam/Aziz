package com.parc.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "chef_de_parc")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class ChefDeParc extends Utilisateur {

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @Column(name = "zone_affectee")
    private String zoneAffectee;

    @OneToOne
    @JoinColumn(name = "parc_id", unique = true)
    private Parc parc;

    // Constructeurs
    public ChefDeParc() {}
    public ChefDeParc(LocalDate dateEmbauche, String zoneAffectee, Parc parc) {
        this.dateEmbauche = dateEmbauche;
        this.zoneAffectee = zoneAffectee;
        this.parc = parc;
    }

    // Getters et Setters
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public String getZoneAffectee() { return zoneAffectee; }
    public void setZoneAffectee(String zoneAffectee) { this.zoneAffectee = zoneAffectee; }

    public Parc getParc() { return parc; }
    public void setParc(Parc parc) { this.parc = parc; }
}