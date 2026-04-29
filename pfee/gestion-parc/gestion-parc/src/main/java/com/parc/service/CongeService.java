package com.parc.service;

import com.parc.dto.CongeDTO;
import com.parc.domain.entity.Conge;
import com.parc.domain.entity.Chauffeur;
import com.parc.domain.enums.StatutConge;
import com.parc.repository.CongeRepository;
import com.parc.repository.ChauffeurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CongeService {

    private final CongeRepository congeRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final NotificationService notificationService;

    private CongeDTO toDTO(Conge c) {
        CongeDTO dto = new CongeDTO();
        dto.setId(c.getId());
        dto.setDateDebut(c.getDateDebut());
        dto.setDateFin(c.getDateFin());
        dto.setMotif(c.getMotif());
        dto.setStatut(c.getStatut().name());
        dto.setDateDemande(c.getDateDemande());
        dto.setReponseMessage(c.getReponseMessage());
        if (c.getChauffeur() != null) {
            dto.setChauffeurId(c.getChauffeur().getId());
            dto.setChauffeurNom(c.getChauffeur().getNom() + " " + c.getChauffeur().getPrenom());
        }
        return dto;
    }

    private Conge toEntity(CongeDTO dto, Chauffeur chauffeur) {
        Conge c = new Conge();
        c.setChauffeur(chauffeur);
        c.setDateDebut(dto.getDateDebut());
        c.setDateFin(dto.getDateFin());
        c.setMotif(dto.getMotif());
        c.setStatut(StatutConge.EN_ATTENTE);
        return c;
    }

    @Transactional
    public CongeDTO createDemande(CongeDTO dto, Long chauffeurId) {
        Chauffeur chauffeur = chauffeurRepository.findById(chauffeurId)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));
        Conge conge = toEntity(dto, chauffeur);
        conge = congeRepository.save(conge);

        // Notification au chef de parc avec détails complets
        notificationService.notifierNouvelleDemandeConge(
            chauffeur.getParc().getChef().getId(),
            conge
        );

        return toDTO(conge);
    }

    public List<CongeDTO> getDemandesEnAttente() {
        return congeRepository.findByStatut(StatutConge.EN_ATTENTE).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<CongeDTO> getMesDemandes(Long chauffeurId) {
        return congeRepository.findByChauffeurId(chauffeurId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public CongeDTO repondreDemande(Long id, String statut, String message) {
        Conge conge = congeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
        
        StatutConge nouveauStatut = StatutConge.valueOf(statut);
        conge.setStatut(nouveauStatut);
        conge.setReponseMessage(message);
        conge = congeRepository.save(conge);

        // Notification au chauffeur avec distinction APPROUVE/REFUSE
        if (nouveauStatut == StatutConge.APPROUVE) {
            notificationService.notifierCongeApprouve(conge.getChauffeur().getId(), conge);
        } else if (nouveauStatut == StatutConge.REFUSE) {
            notificationService.notifierCongeRefuse(conge.getChauffeur().getId(), conge, message);
        }
        
        return toDTO(conge);
    }

    public List<CongeDTO> getHistorique() {
        return congeRepository.findAll().stream()
                .map(this::toDTO).collect(Collectors.toList());
    }
}