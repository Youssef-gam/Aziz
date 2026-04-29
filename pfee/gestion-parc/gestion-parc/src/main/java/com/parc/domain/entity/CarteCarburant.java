package com.parc.domain.entity;

import com.parc.domain.enums.StatutCarte;
import jakarta.persistence.*;

@Entity
@Table(name = "cartes_carburant")
public class CarteCarburant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_carte", unique = true, nullable = false)
    private String numCarte;

    private Double plafond;
    private Double solde;

    @Enumerated(EnumType.STRING)
    private StatutCarte statut;

    @ManyToOne
    @JoinColumn(name = "chauffeur_id")
    private Chauffeur chauffeur;

    public CarteCarburant() {}

    // Getters & Setters
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

    public Chauffeur getChauffeur() { return chauffeur; }
    public void setChauffeur(Chauffeur chauffeur) { this.chauffeur = chauffeur; }
}