package com.parc.dto;

import java.time.LocalDate;

public class ChefDeParcDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private LocalDate dateEmbauche;
    private String zoneAffectee;
    private Long parcId;
    private String parcNom;

    // Constructeurs
    public ChefDeParcDTO() {}
    public ChefDeParcDTO(Long id, String nom, LocalDate dateEmbauche, Long parcId) {
        this.id = id;
        this.nom = nom;
        this.dateEmbauche = dateEmbauche;
        this.parcId = parcId;
    }

    // Getters et Setters
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

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public String getZoneAffectee() { return zoneAffectee; }
    public void setZoneAffectee(String zoneAffectee) { this.zoneAffectee = zoneAffectee; }

    public Long getParcId() { return parcId; }
    public void setParcId(Long parcId) { this.parcId = parcId; }

    public String getParcNom() { return parcNom; }
    public void setParcNom(String parcNom) { this.parcNom = parcNom; }
}