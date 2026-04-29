package com.parc.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "feuilles_de_route")
public class FeuilleDeRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_feuille")
    private String numeroFeuille;       // peut être généré automatiquement

    @Column(name = "nombre_participants")
    private Integer nombreParticipants;

    private String destination;
    private String objetMission;

    // Relation avec Mission (one-to-one)
    @OneToOne
    @JoinColumn(name = "mission_id", unique = true)
    private Mission mission;

    // Constructeurs
    public FeuilleDeRoute() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFeuille() { return numeroFeuille; }
    public void setNumeroFeuille(String numeroFeuille) { this.numeroFeuille = numeroFeuille; }

    public Integer getNombreParticipants() { return nombreParticipants; }
    public void setNombreParticipants(Integer nombreParticipants) { this.nombreParticipants = nombreParticipants; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getObjetMission() { return objetMission; }
    public void setObjetMission(String objetMission) { this.objetMission = objetMission; }

    public Mission getMission() { return mission; }
    public void setMission(Mission mission) { this.mission = mission; }
}