package com.parc.domain.entity;

import com.parc.domain.enums.Disponibilite;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "chauffeurs")
@PrimaryKeyJoinColumn(name = "utilisateur_id")
public class Chauffeur extends Utilisateur {

    @Column(name = "numero_permis", unique = true, nullable = false)
    private String numeroPermis;

    @Column(name = "date_expiration_permis")
    private LocalDate dateExpirationPermis;

    @Enumerated(EnumType.STRING)
    private Disponibilite disponible;

    @ManyToOne
    @JoinColumn(name = "parc_id")
    private Parc parc;

    @Column(name = "fcm_token")
    private String fcmToken; // ← ADDED

    public Chauffeur() {}

    public Chauffeur(String numeroPermis, LocalDate dateExpirationPermis, Disponibilite disponible, Parc parc) {
        this.numeroPermis = numeroPermis;
        this.dateExpirationPermis = dateExpirationPermis;
        this.disponible = disponible;
        this.parc = parc;
    }

    // Getters et Setters
    public String getNumeroPermis() { return numeroPermis; }
    public void setNumeroPermis(String numeroPermis) { this.numeroPermis = numeroPermis; }

    public LocalDate getDateExpirationPermis() { return dateExpirationPermis; }
    public void setDateExpirationPermis(LocalDate dateExpirationPermis) { this.dateExpirationPermis = dateExpirationPermis; }

    public Disponibilite getDisponible() { return disponible; }
    public void setDisponible(Disponibilite disponible) { this.disponible = disponible; }

    public Parc getParc() { return parc; }
    public void setParc(Parc parc) { this.parc = parc; }

    public String getFcmToken() { return fcmToken; }           // ← ADDED
    public void setFcmToken(String fcmToken) { this.fcmToken = fcmToken; } // ← ADDED
}