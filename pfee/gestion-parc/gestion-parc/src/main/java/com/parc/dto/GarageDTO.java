package com.parc.dto;

public class GarageDTO {

    private Long id;
    private String nom;
    private String specialite;
    private String adresse;
    private Integer capacite;
    private String telephone;

    // Constructeurs
    public GarageDTO() {}

    public GarageDTO(Long id, String nom, String specialite, String adresse, Integer capacite, String telephone) {
        this.id = id;
        this.nom = nom;
        this.specialite = specialite;
        this.adresse = adresse;
        this.capacite = capacite;
        this.telephone = telephone;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}