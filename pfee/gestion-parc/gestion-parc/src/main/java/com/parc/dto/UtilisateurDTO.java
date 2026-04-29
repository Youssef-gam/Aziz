package com.parc.dto;

import com.parc.domain.enums.Role;
import java.time.LocalDate;

public class UtilisateurDTO {

    // Champs communs
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Role role;
    private boolean actif;
    private String motDePasse;

    // Chauffeur
    private String numeroPermis;
    private LocalDate dateExpirationPermis;
    private String disponible;
    private Long id_parc;

    // Chef de parc
    private LocalDate dateEmbauche;
    private String zoneAffectation;

    // Opérateur maintenance
    private String specialite;
    private String niveau;
    private Long id_garage;

    // Constructeurs
    public UtilisateurDTO() {}

    // Getters & Setters (tous les champs)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    // Chauffeur
    public String getNumeroPermis() { return numeroPermis; }
    public void setNumeroPermis(String numeroPermis) { this.numeroPermis = numeroPermis; }

    public LocalDate getDateExpirationPermis() { return dateExpirationPermis; }
    public void setDateExpirationPermis(LocalDate dateExpirationPermis) { this.dateExpirationPermis = dateExpirationPermis; }

    public String getDisponible() { return disponible; }
    public void setDisponible(String disponible) { this.disponible = disponible; }

    public Long getId_parc() { return id_parc; }
    public void setId_parc(Long id_parc) { this.id_parc = id_parc; }

    // Chef de parc
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public String getZoneAffectation() { return zoneAffectation; }
    public void setZoneAffectation(String zoneAffectation) { this.zoneAffectation = zoneAffectation; }

    // Opérateur maintenance
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }

    public Long getId_garage() { return id_garage; }
    public void setId_garage(Long id_garage) { this.id_garage = id_garage; }
}