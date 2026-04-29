package com.parc.mapper;

import com.parc.domain.entity.Utilisateur;
import com.parc.dto.UtilisateurDTO;
import org.springframework.stereotype.Component;

@Component
public class UtilisateurMapper {

    public UtilisateurDTO toDTO(Utilisateur user) {
        if (user == null) return null;
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setTelephone(user.getTelephone());
        dto.setRole(user.getRole());
        dto.setActif(user.isActif());
        // ⚠️ NE JAMAIS exposer le mot de passe au frontend
        // dto.setMotDePasse(user.getMotDePasse());

        return dto;
    }

    public Utilisateur toEntity(UtilisateurDTO dto) {
        if (dto == null) return null;
        Utilisateur user = new Utilisateur();
        user.setId(dto.getId());
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setTelephone(dto.getTelephone());
        user.setRole(dto.getRole());
        user.setActif(dto.isActif());
        user.setMotDePasse(dto.getMotDePasse());
        return user;
    }

    public void updateEntity(Utilisateur user, UtilisateurDTO dto) {
        if (user == null || dto == null) return;
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setTelephone(dto.getTelephone());
        user.setRole(dto.getRole());
        user.setActif(dto.isActif());
        // ⚠️ NE PAS mettre à jour le motDePasse ici
        // Le mot de passe doit être géré uniquement par UtilisateurService
        // qui gère le hachage BCrypt
    }
}