package com.parc.dto;

import com.parc.domain.enums.Disponibilite;
import java.time.LocalDate;

public class ChauffeurDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String numeroPermis;
    private LocalDate dateExpirationPermis;
    private Disponibilite disponible;
    private Long parcId;
    private String parcNom;

    // Constructeurs
    public ChauffeurDTO() {}
    public ChauffeurDTO(Long id, String nom, String prenom, String email, String telephone, String numeroPermis, LocalDate dateExpirationPermis, Disponibilite disponible, Long parcId, String parcNom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.numeroPermis = numeroPermis;
        this.dateExpirationPermis = dateExpirationPermis;
        this.disponible = disponible;
        this.parcId = parcId;
        this.parcNom = parcNom;
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

    public String getNumeroPermis() { return numeroPermis; }
    public void setNumeroPermis(String numeroPermis) { this.numeroPermis = numeroPermis; }

    public LocalDate getDateExpirationPermis() { return dateExpirationPermis; }
    public void setDateExpirationPermis(LocalDate dateExpirationPermis) { this.dateExpirationPermis = dateExpirationPermis; }

    public Disponibilite getDisponible() { return disponible; }
    public void setDisponible(Disponibilite disponible) { this.disponible = disponible; }

    public Long getParcId() { return parcId; }
    public void setParcId(Long parcId) { this.parcId = parcId; }

    public String getParcNom() { return parcNom; }
    public void setParcNom(String parcNom) { this.parcNom = parcNom; }
}