package com.parc.domain.entity;

import com.parc.domain.enums.StatutMaintenance;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "maintenances")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Enumerated(EnumType.STRING)
    private StatutMaintenance statut;

    @Column(name = "date_prevue")
    private LocalDate datePrevue;

    private Double cout;

    private String operateur;

    @Column(name = "rapport_probleme", length = 1000)
    private String rapportProbleme;

    @Column(name = "date_realisation")
    private LocalDate dateRealisation;

    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;

    // Constructeurs
    public Maintenance() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public StatutMaintenance getStatut() { return statut; }
    public void setStatut(StatutMaintenance statut) { this.statut = statut; }

    public LocalDate getDatePrevue() { return datePrevue; }
    public void setDatePrevue(LocalDate datePrevue) { this.datePrevue = datePrevue; }

    public Double getCout() { return cout; }
    public void setCout(Double cout) { this.cout = cout; }

    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }

    public String getRapportProbleme() { return rapportProbleme; }
    public void setRapportProbleme(String rapportProbleme) { this.rapportProbleme = rapportProbleme; }

    public LocalDate getDateRealisation() { return dateRealisation; }
    public void setDateRealisation(LocalDate dateRealisation) { this.dateRealisation = dateRealisation; }

    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }

    public Garage getGarage() { return garage; }
    public void setGarage(Garage garage) { this.garage = garage; }
}