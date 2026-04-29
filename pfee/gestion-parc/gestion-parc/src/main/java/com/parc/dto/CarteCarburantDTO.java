package com.parc.dto;

import com.parc.domain.enums.StatutCarte;

public class CarteCarburantDTO {

    private Long id;
    private String numCarte;
    private Double plafond;
    private Double solde;
    private StatutCarte statut;
    private Long chauffeurId;
    private String chauffeurNom; // pour affichage

    // Constructeurs
    public CarteCarburantDTO() {}

    public CarteCarburantDTO(Long id, String numCarte, Double plafond, Double solde, StatutCarte statut, Long chauffeurId, String chauffeurNom) {
        this.id = id;
        this.numCarte = numCarte;
        this.plafond = plafond;
        this.solde = solde;
        this.statut = statut;
        this.chauffeurId = chauffeurId;
        this.chauffeurNom = chauffeurNom;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumCarte() { return numCarte; }
    public void setNumCarte(String numCarte) { this.numCarte = numCarte; }

    public Double getPlafond() { return plafond; }
    public void setPlafond(Double plafond) { this.plafond = plafond; }

    public Double getSolde() { return solde; }
    public void setSolde(Double solde) { this.solde = solde; }

    public StatutCarte getStatut() { return statut; }
    public void setStatut(StatutCarte statut) { this.statut = statut; }

    public Long getChauffeurId() { return chauffeurId; }
    public void setChauffeurId(Long chauffeurId) { this.chauffeurId = chauffeurId; }

    public String getChauffeurNom() { return chauffeurNom; }
    public void setChauffeurNom(String chauffeurNom) { this.chauffeurNom = chauffeurNom; }
}