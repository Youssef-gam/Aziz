package com.parc.dto;

import java.time.LocalDate;

public class VehiculeDTO {

    private Long id;
    private String matricule;
    private String numeroChassis;
    private String marque;
    private String modele;
    private String typeCarburant;
    private String couleur;
    private Double kilometre;
    private Double capaciteReservoir;
    private LocalDate dateMiseEnService;
    private LocalDate dateExpirationVisiteTechnique;
    private LocalDate dateExpirationCarteGrise;
    private Integer puissanceFiscale;
    private String statut;
    private String nomSocieteAssurance;
    private LocalDate dateExpirationAssurance;
    private Double montantAssurance;
    private Long parcId;          // ID du parc associé
    private String parcNom;       // Nom du parc (optionnel, pour affichage)

    // Constructeurs
    public VehiculeDTO() {}

    public VehiculeDTO(Long id, String matricule, String marque, String modele, Double kilometre, Long parcId) {
        this.id = id;
        this.matricule = matricule;
        this.marque = marque;
        this.modele = modele;
        this.kilometre = kilometre;
        this.parcId = parcId;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getNumeroChassis() { return numeroChassis; }
    public void setNumeroChassis(String numeroChassis) { this.numeroChassis = numeroChassis; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }

    public String getTypeCarburant() { return typeCarburant; }
    public void setTypeCarburant(String typeCarburant) { this.typeCarburant = typeCarburant; }

    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }

    public Double getKilometre() { return kilometre; }
    public void setKilometre(Double kilometre) { this.kilometre = kilometre; }

    public Double getCapaciteReservoir() { return capaciteReservoir; }
    public void setCapaciteReservoir(Double capaciteReservoir) { this.capaciteReservoir = capaciteReservoir; }

    public LocalDate getDateMiseEnService() { return dateMiseEnService; }
    public void setDateMiseEnService(LocalDate dateMiseEnService) { this.dateMiseEnService = dateMiseEnService; }

    public LocalDate getDateExpirationVisiteTechnique() { return dateExpirationVisiteTechnique; }
    public void setDateExpirationVisiteTechnique(LocalDate dateExpirationVisiteTechnique) { this.dateExpirationVisiteTechnique = dateExpirationVisiteTechnique; }

    public LocalDate getDateExpirationCarteGrise() { return dateExpirationCarteGrise; }
    public void setDateExpirationCarteGrise(LocalDate dateExpirationCarteGrise) { this.dateExpirationCarteGrise = dateExpirationCarteGrise; }

    public Integer getPuissanceFiscale() { return puissanceFiscale; }
    public void setPuissanceFiscale(Integer puissanceFiscale) { this.puissanceFiscale = puissanceFiscale; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getNomSocieteAssurance() { return nomSocieteAssurance; }
    public void setNomSocieteAssurance(String nomSocieteAssurance) { this.nomSocieteAssurance = nomSocieteAssurance; }

    public LocalDate getDateExpirationAssurance() { return dateExpirationAssurance; }
    public void setDateExpirationAssurance(LocalDate dateExpirationAssurance) { this.dateExpirationAssurance = dateExpirationAssurance; }

    public Double getMontantAssurance() { return montantAssurance; }
    public void setMontantAssurance(Double montantAssurance) { this.montantAssurance = montantAssurance; }

    public Long getParcId() { return parcId; }
    public void setParcId(Long parcId) { this.parcId = parcId; }

    public String getParcNom() { return parcNom; }
    public void setParcNom(String parcNom) { this.parcNom = parcNom; }
}