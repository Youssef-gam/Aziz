package com.parc.domain.entity;

import com.parc.domain.enums.StatutConge;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "conges")
public class Conge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chauffeur_id")
    private Chauffeur chauffeur;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String motif;

    @Enumerated(EnumType.STRING)
    private StatutConge statut = StatutConge.EN_ATTENTE;

    private LocalDateTime dateDemande = LocalDateTime.now();
    private String reponseMessage;

    // Constructeurs
    public Conge() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Chauffeur getChauffeur() { return chauffeur; }
    public void setChauffeur(Chauffeur chauffeur) { this.chauffeur = chauffeur; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public StatutConge getStatut() { return statut; }
    public void setStatut(StatutConge statut) { this.statut = statut; }

    public LocalDateTime getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDateTime dateDemande) { this.dateDemande = dateDemande; }

    public String getReponseMessage() { return reponseMessage; }
    public void setReponseMessage(String reponseMessage) { this.reponseMessage = reponseMessage; }
}