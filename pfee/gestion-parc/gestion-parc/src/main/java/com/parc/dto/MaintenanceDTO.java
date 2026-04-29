package com.parc.dto;

import com.parc.domain.enums.StatutMaintenance;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintenanceDTO {

    private Long id;
    private String type;
    private StatutMaintenance statut;
    private LocalDate datePrevue;
    private Double cout;
    private String operateur;
    private String rapportProbleme;
    private LocalDate dateRealisation;

    private Long vehiculeId;
    private String vehiculeMatricule;

    private Long garageId;
    private String garageNom;
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

public Long getVehiculeId() { return vehiculeId; }
public void setVehiculeId(Long vehiculeId) { this.vehiculeId = vehiculeId; }

public String getVehiculeMatricule() { return vehiculeMatricule; }
public void setVehiculeMatricule(String vehiculeMatricule) { this.vehiculeMatricule = vehiculeMatricule; }

public Long getGarageId() { return garageId; }
public void setGarageId(Long garageId) { this.garageId = garageId; }

public String getGarageNom() { return garageNom; }
public void setGarageNom(String garageNom) { this.garageNom = garageNom; }
}