package com.parc.service;

import com.parc.dto.ChauffeurDTO;
import com.parc.domain.entity.Chauffeur;
import com.parc.domain.entity.Parc;
import com.parc.repository.ChauffeurRepository;
import com.parc.repository.ParcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChauffeurService {

    private final ChauffeurRepository chauffeurRepository;
    private final ParcRepository parcRepository;

    public ChauffeurService(ChauffeurRepository chauffeurRepository, ParcRepository parcRepository) {
        this.chauffeurRepository = chauffeurRepository;
        this.parcRepository = parcRepository;
    }

    // ===== Conversions =====
    private ChauffeurDTO toDTO(Chauffeur chauffeur) {
        ChauffeurDTO dto = new ChauffeurDTO();
        dto.setId(chauffeur.getId());
        dto.setNom(chauffeur.getNom());
        dto.setPrenom(chauffeur.getPrenom());
        dto.setEmail(chauffeur.getEmail());
        dto.setTelephone(chauffeur.getTelephone());
        dto.setNumeroPermis(chauffeur.getNumeroPermis());
        dto.setDateExpirationPermis(chauffeur.getDateExpirationPermis());
        dto.setDisponible(chauffeur.getDisponible());

        if (chauffeur.getParc() != null) {
            dto.setParcId(chauffeur.getParc().getId());
            dto.setParcNom(chauffeur.getParc().getNom());
        }
        return dto;
    }

    private Chauffeur toEntity(ChauffeurDTO dto) {
        Chauffeur chauffeur = new Chauffeur();
        chauffeur.setNom(dto.getNom());
        chauffeur.setPrenom(dto.getPrenom());
        chauffeur.setEmail(dto.getEmail());
        chauffeur.setTelephone(dto.getTelephone());
        chauffeur.setNumeroPermis(dto.getNumeroPermis());
        chauffeur.setDateExpirationPermis(dto.getDateExpirationPermis());
        chauffeur.setDisponible(dto.getDisponible());

        if (dto.getParcId() != null) {
            Parc parc = parcRepository.findById(dto.getParcId())
                    .orElseThrow(() -> new RuntimeException("Parc non trouvé"));
            chauffeur.setParc(parc);
        }
        return chauffeur;
    }

    // ===== CRUD =====
    public ChauffeurDTO createChauffeur(ChauffeurDTO dto) {
        if (chauffeurRepository.existsByNumeroPermis(dto.getNumeroPermis())) {
            throw new RuntimeException("Numéro de permis déjà utilisé");
        }
        Chauffeur chauffeur = toEntity(dto);
        return toDTO(chauffeurRepository.save(chauffeur));
    }

    public List<ChauffeurDTO> getAllChauffeurs() {
        List<Chauffeur> chauffeurs = chauffeurRepository.findAll();
        System.out.println("🔍 [ChauffeurService] getAllChauffeurs() - Total en base de données: " + chauffeurs.size());
        chauffeurs.forEach(c -> System.out.println("  - ID: " + c.getId() + " | Nom: " + c.getNom() + " | Email: " + c.getEmail() + " | Parc: " + (c.getParc() != null ? c.getParc().getNom() : "NULL")));
        
        List<ChauffeurDTO> dtos = chauffeurs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        System.out.println("✅ getAllChauffeurs() retournant: " + dtos.size() + " chauffeurs");
        return dtos;
    }

    public ChauffeurDTO getChauffeurById(Long id) {
        return chauffeurRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));
    }

    public ChauffeurDTO updateChauffeur(Long id, ChauffeurDTO dto) {
        Chauffeur existing = chauffeurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));

        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setEmail(dto.getEmail());
        existing.setTelephone(dto.getTelephone());
        existing.setNumeroPermis(dto.getNumeroPermis());
        existing.setDateExpirationPermis(dto.getDateExpirationPermis());
        existing.setDisponible(dto.getDisponible());

        if (dto.getParcId() != null) {
            Parc parc = parcRepository.findById(dto.getParcId())
                    .orElseThrow(() -> new RuntimeException("Parc non trouvé"));
            existing.setParc(parc);
        } else {
            existing.setParc(null);
        }

        return toDTO(chauffeurRepository.save(existing));
    }

    public void deleteChauffeur(Long id) {
        Chauffeur existing = chauffeurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"));
        chauffeurRepository.delete(existing);
    }

    // ===== Recherches =====
    public Chauffeur getChauffeurByEmail(String email) {
        return chauffeurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé avec email : " + email));
    }

    public Chauffeur getChauffeurByNumeroPermis(String numeroPermis) {
        return chauffeurRepository.findByNumeroPermis(numeroPermis)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé avec numéro de permis : " + numeroPermis));
    }
}