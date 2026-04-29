package com.parc.domain.entity;

import com.parc.domain.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs")
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Column(unique = true)
    private String email;

    private String motDePasse;
    private String telephone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String photo;
    private boolean actif;
    private LocalDateTime derniereConnexion;

    // JPA Callbacks
    @PostLoad
    public void postLoad() {
        // Ensure role is never null
        if (this.role == null) {
            // Default to CHAUFFEUR if role is null (shouldn't happen)
            this.role = Role.CHAUFFEUR;
        }
    }

    // Constructeurs
    public Utilisateur() {}

    public Utilisateur(Long id, String nom, String prenom, String email, String motDePasse,
                       String telephone, Role role, String photo, boolean actif, LocalDateTime derniereConnexion) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.role = role;
        this.photo = photo;
        this.actif = actif;
        this.derniereConnexion = derniereConnexion;
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

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public LocalDateTime getDerniereConnexion() { return derniereConnexion; }
    public void setDerniereConnexion(LocalDateTime derniereConnexion) { this.derniereConnexion = derniereConnexion; }
}