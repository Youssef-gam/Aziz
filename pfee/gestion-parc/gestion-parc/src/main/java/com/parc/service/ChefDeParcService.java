package com.parc.service;

import com.parc.dto.ChefDeParcDTO;
import com.parc.domain.entity.ChefDeParc;
import com.parc.domain.entity.Parc;
import com.parc.repository.ChefDeParcRepository;
import com.parc.repository.ParcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChefDeParcService {

    private final ChefDeParcRepository chefDeParcRepository;
    private final ParcRepository parcRepository;

    // Convertir entité en DTO (tous les champs)
    private ChefDeParcDTO toDTO(ChefDeParc chef) {
        ChefDeParcDTO dto = new ChefDeParcDTO();
        dto.setId(chef.getId());
        dto.setNom(chef.getNom());
        dto.setPrenom(chef.getPrenom());
        dto.setEmail(chef.getEmail());
        dto.setTelephone(chef.getTelephone());
        dto.setDateEmbauche(chef.getDateEmbauche());
        dto.setZoneAffectee(chef.getZoneAffectee());

        if (chef.getParc() != null) {
            dto.setParcId(chef.getParc().getId());
            dto.setParcNom(chef.getParc().getNom()); // suppose que Parc a un champ "nom"
        }

        return dto;
    }

    // Convertir DTO en entité (tous les champs)
    private ChefDeParc toEntity(ChefDeParcDTO dto) {
        ChefDeParc chef = new ChefDeParc();
        chef.setNom(dto.getNom());
        chef.setPrenom(dto.getPrenom());
        chef.setEmail(dto.getEmail());
        chef.setTelephone(dto.getTelephone());
        chef.setDateEmbauche(dto.getDateEmbauche());
        chef.setZoneAffectee(dto.getZoneAffectee());

        // Gestion du parc
        if (dto.getParcId() != null) {
            Parc parc = parcRepository.findById(dto.getParcId())
                    .orElseThrow(() -> new RuntimeException("Parc non trouvé avec id : " + dto.getParcId()));
            // Vérifier si un autre chef n'est déjà affecté à ce parc
            if (chefDeParcRepository.existsByParcId(parc.getId())) {
                throw new RuntimeException("Ce parc a déjà un chef.");
            }
            chef.setParc(parc);
        }

        return chef;
    }

    // Création d'un chef de parc
    public ChefDeParcDTO createChef(ChefDeParcDTO dto) {
        ChefDeParc chef = toEntity(dto);
        ChefDeParc saved = chefDeParcRepository.save(chef);
        return toDTO(saved);
    }

    // Récupérer tous les chefs
    public List<ChefDeParcDTO> getAllChefs() {
        return chefDeParcRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer un chef par id
    public ChefDeParcDTO getChefById(Long id) {
        return chefDeParcRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Chef non trouvé avec id : " + id));
    }

    // Mise à jour d'un chef
    public ChefDeParcDTO updateChef(Long id, ChefDeParcDTO dto) {
        ChefDeParc existing = chefDeParcRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chef non trouvé avec id : " + id));

        // Mise à jour des champs de base
        existing.setNom(dto.getNom());
        existing.setPrenom(dto.getPrenom());
        existing.setEmail(dto.getEmail());
        existing.setTelephone(dto.getTelephone());
        existing.setDateEmbauche(dto.getDateEmbauche());
        existing.setZoneAffectee(dto.getZoneAffectee());

        // Mise à jour du parc
        if (dto.getParcId() != null) {
            Parc parc = parcRepository.findById(dto.getParcId())
                    .orElseThrow(() -> new RuntimeException("Parc non trouvé avec id : " + dto.getParcId()));
            // Vérifier si le nouveau parc n'est pas déjà affecté à un autre chef
            Long currentParcId = existing.getParc() != null ? existing.getParc().getId() : null;
            if (!parc.getId().equals(currentParcId) && chefDeParcRepository.existsByParcId(parc.getId())) {
                throw new RuntimeException("Ce parc a déjà un chef.");
            }
            existing.setParc(parc);
        } else {
            // Si on veut permettre de retirer l'association à un parc
            existing.setParc(null);
        }

        ChefDeParc updated = chefDeParcRepository.save(existing);
        return toDTO(updated);
    }

    // Supprimer un chef
    public void deleteChef(Long id) {
        ChefDeParc existing = chefDeParcRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chef non trouvé avec id : " + id));
        chefDeParcRepository.delete(existing);
    }
}