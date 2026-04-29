package com.parc.domain.entity;

import com.parc.domain.enums.StatutMission;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "missions")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String destination;
    
    @Column(name = "objet_a_livrer")
    private String objetALivrer;
    
    @Column(name = "nombre_de_participants")
    private Integer nombreDeParticipants;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutMission statut;

    // Relations
    @ManyToOne
    @JoinColumn(name = "vehicule_id")
    private Vehicule vehicule;

    @ManyToOne
    @JoinColumn(name = "chauffeur_id")
    private Chauffeur chauffeur;

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private FeuilleDeRoute feuilleDeRoute;

    // Constructeurs
    public Mission() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getObjetALivrer() { return objetALivrer; }
    public void setObjetALivrer(String objetALivrer) { this.objetALivrer = objetALivrer; }

    public Integer getNombreDeParticipants() { return nombreDeParticipants; }
    public void setNombreDeParticipants(Integer nombreDeParticipants) { this.nombreDeParticipants = nombreDeParticipants; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public StatutMission getStatut() { return statut; }
    public void setStatut(StatutMission statut) { this.statut = statut; }

    public Vehicule getVehicule() { return vehicule; }
    public void setVehicule(Vehicule vehicule) { this.vehicule = vehicule; }

    public Chauffeur getChauffeur() { return chauffeur; }
    public void setChauffeur(Chauffeur chauffeur) { this.chauffeur = chauffeur; }

    public FeuilleDeRoute getFeuilleDeRoute() { return feuilleDeRoute; }
    public void setFeuilleDeRoute(FeuilleDeRoute feuilleDeRoute) { this.feuilleDeRoute = feuilleDeRoute; }
}