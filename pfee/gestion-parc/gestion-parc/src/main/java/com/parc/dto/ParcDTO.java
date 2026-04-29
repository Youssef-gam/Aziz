package com.parc.dto;

public class ParcDTO {
    private Long id;
    private String nom;
    private String adresse;
    private Integer capacite;

    // Constructeur par défaut (obligatoire pour JPA/DTO)
    public ParcDTO() {}

    // Constructeur avec paramètres
    public ParcDTO(Long id, String nom, String adresse, Integer capacite) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.capacite = capacite;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }
}