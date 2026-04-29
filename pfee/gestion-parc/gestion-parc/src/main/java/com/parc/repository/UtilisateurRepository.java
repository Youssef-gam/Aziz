package com.parc.repository;

import com.parc.domain.entity.Utilisateur;
import com.parc.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;  // ← ajouter cet import
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    Optional<Utilisateur> findByEmailIgnoreCase(String email);
    List<Utilisateur> findByRole(String role);
    List<Utilisateur> findByRole(Role role);
}