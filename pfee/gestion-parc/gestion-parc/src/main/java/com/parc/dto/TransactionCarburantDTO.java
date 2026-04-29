package com.parc.dto;

import java.time.LocalDateTime;

public class TransactionCarburantDTO {
    private Long id;
    private LocalDateTime date;
    private Double montant;
    private Double litres;
    private String type;          // "CONSOMMATION" ou "RECHARGE"
    private Long carteId;
    private String carteNumero;
    private Long chauffeurId;
    private String chauffeurNom;
    private Long missionId;
    private Long vehiculeId;
    private String vehiculeMatricule;

    // Constructeur par défaut
    public TransactionCarburantDTO() {
    }

    // Constructeur avec tous les champs
    public TransactionCarburantDTO(Long id, LocalDateTime date, Double montant, Double litres,
                                   String type, Long carteId, String carteNumero,
                                   Long chauffeurId, String chauffeurNom,
                                   Long missionId, Long vehiculeId, String vehiculeMatricule) {
        this.id = id;
        this.date = date;
        this.montant = montant;
        this.litres = litres;
        this.type = type;
        this.carteId = carteId;
        this.carteNumero = carteNumero;
        this.chauffeurId = chauffeurId;
        this.chauffeurNom = chauffeurNom;
        this.missionId = missionId;
        this.vehiculeId = vehiculeId;
        this.vehiculeMatricule = vehiculeMatricule;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Double getLitres() {
        return litres;
    }

    public void setLitres(Double litres) {
        this.litres = litres;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCarteId() {
        return carteId;
    }

    public void setCarteId(Long carteId) {
        this.carteId = carteId;
    }

    public String getCarteNumero() {
        return carteNumero;
    }

    public void setCarteNumero(String carteNumero) {
        this.carteNumero = carteNumero;
    }

    public Long getChauffeurId() {
        return chauffeurId;
    }

    public void setChauffeurId(Long chauffeurId) {
        this.chauffeurId = chauffeurId;
    }

    public String getChauffeurNom() {
        return chauffeurNom;
    }

    public void setChauffeurNom(String chauffeurNom) {
        this.chauffeurNom = chauffeurNom;
    }

    public Long getMissionId() {
        return missionId;
    }

    public void setMissionId(Long missionId) {
        this.missionId = missionId;
    }

    public Long getVehiculeId() {
        return vehiculeId;
    }

    public void setVehiculeId(Long vehiculeId) {
        this.vehiculeId = vehiculeId;
    }

    public String getVehiculeMatricule() {
        return vehiculeMatricule;
    }

    public void setVehiculeMatricule(String vehiculeMatricule) {
        this.vehiculeMatricule = vehiculeMatricule;
    }
}