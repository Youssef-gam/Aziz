package com.parc.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CongeDTO {

    private Long id;
    private Long chauffeurId;
    private String chauffeurNom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;
    private String statut;
    private LocalDateTime dateDemande;
    private String reponseMessage;

    // Constructeurs
    public CongeDTO() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getChauffeurId() { return chauffeurId; }
    public void setChauffeurId(Long chauffeurId) { this.chauffeurId = chauffeurId; }

    public String getChauffeurNom() { return chauffeurNom; }
    public void setChauffeurNom(String chauffeurNom) { this.chauffeurNom = chauffeurNom; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }

    public String getReponseMessage() { return reponseMessage; }
    public void setReponseMessage(String reponseMessage) { this.reponseMessage = reponseMessage; }
}