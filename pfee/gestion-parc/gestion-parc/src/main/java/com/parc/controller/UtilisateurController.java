package com.parc.controller;

import com.parc.dto.UtilisateurDTO;
import com.parc.dto.PasswordResetRequest;
import com.parc.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/me")
    public ResponseEntity<UtilisateurDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UtilisateurDTO user = utilisateurService.getByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UtilisateurDTO> create(@RequestBody UtilisateurDTO dto) {
        return ResponseEntity.ok(utilisateurService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<UtilisateurDTO>> getAll() {
        return ResponseEntity.ok(utilisateurService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(utilisateurService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDTO> update(@PathVariable Long id, @RequestBody UtilisateurDTO dto) {
        return ResponseEntity.ok(utilisateurService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        utilisateurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint de diagnostic (sans authentification)
    @GetMapping("/diagnostic/{email}")
    public ResponseEntity<String> diagnostic(@PathVariable String email) {
        try {
            UtilisateurDTO user = utilisateurService.getByEmail(email);
            return ResponseEntity.ok(
                "✅ Utilisateur trouvé:\n" +
                "- ID: " + user.getId() + "\n" +
                "- Email: " + user.getEmail() + "\n" +
                "- Nom: " + user.getNom() + "\n" +
                "- Prénom: " + user.getPrenom() + "\n" +
                "- Rôle: " + user.getRole() + "\n" +
                "- Actif: " + (user.isActif() ? "✅ OUI" : "❌ NON") + "\n" +
                "- Mot de passe hashé: " + (user.getMotDePasse() != null && !user.getMotDePasse().isEmpty() ? "✅ Présent" : "❌ Absent")
            );
        } catch (Exception e) {
            return ResponseEntity.ok("❌ Utilisateur NON trouvé: " + email + "\nErreur: " + e.getMessage());
        }
    }

    // Endpoint pour réinitialiser le mot de passe (admin only)
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable Long id, @RequestBody PasswordResetRequest request) {
        try {
            UtilisateurDTO user = utilisateurService.getById(id);
            user.setMotDePasse(request.getNewPassword());
            utilisateurService.update(id, user);
            return ResponseEntity.ok("✅ Mot de passe réinitialisé avec succès pour: " + user.getEmail());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("❌ Erreur: " + e.getMessage());
        }
    }

    // Endpoint pour réinitialiser par email (simple)
    @PostMapping("/reset/{email}")
    public ResponseEntity<String> resetPasswordByEmail(@PathVariable String email, @RequestBody PasswordResetRequest request) {
        try {
            UtilisateurDTO user = utilisateurService.getByEmail(email);
            user.setMotDePasse(request.getNewPassword());
            utilisateurService.update(user.getId(), user);
            return ResponseEntity.ok("✅ Mot de passe réinitialisé pour: " + email + "\nNouveau MP: " + request.getNewPassword());
        } catch (Exception e) {
            return ResponseEntity.status(400).body("❌ Erreur: " + e.getMessage());
        }
    }
}