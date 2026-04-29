package com.parc.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehicules")
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String matricule;

    @Column(name = "numero_chassis", unique = true, nullable = false)
    private String numeroChassis;

    private String marque;
    private String modele;

    @Column(name = "type_carburant")
    private String typeCarburant;

    private String couleur;

    private Double kilometre;

    @Column(name = "capacite_reservoir")
    private Double capaciteReservoir;

    @Column(name = "date_mise_en_service")
    private LocalDate dateMiseEnService;

    @Column(name = "date_expiration_visite_technique")
    private LocalDate dateExpirationVisiteTechnique;

    @Column(name = "date_expiration_carte_grise")
    private LocalDate dateExpirationCarteGrise;

    @Column(name = "puissance_fiscale")
    private Integer puissanceFiscale;   // correspond à "poursuivanteFiscale" (puissance fiscale)

    private String statut;               // ex: DISPONIBLE, EN_MAINTENANCE, HORS_SERVICE

    @Column(name = "nom_societe_assurance")
    private String nomSocieteAssurance;

    @Column(name = "date_expiration_assurance")
    private LocalDate dateExpirationAssurance;

    @Column(name = "montant_assurance")
    private Double montantAssurance;

    // Relation avec Parc (many-to-one)
    @ManyToOne
    @JoinColumn(name = "parc_id")
    private Parc parc;

    // Constructeurs
    public Vehicule() {}

    // Getters et Setters (explicites)
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

    public Parc getParc() { return parc; }
    public void setParc(Parc parc) { this.parc = parc; }
}