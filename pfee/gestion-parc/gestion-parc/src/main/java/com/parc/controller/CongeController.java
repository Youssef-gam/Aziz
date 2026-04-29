package com.parc.controller;

import com.parc.dto.CongeDTO;
import com.parc.repository.ChauffeurRepository;
import com.parc.service.CongeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conges")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CongeController {

    private final CongeService congeService;
    private final ChauffeurRepository chauffeurRepository;

    private Long getChauffeurIdByEmail(String email) {
        return chauffeurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Chauffeur non trouvé"))
                .getId();
    }

    @PostMapping
    public ResponseEntity<CongeDTO> createDemande(@RequestBody CongeDTO dto,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        Long chauffeurId = getChauffeurIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(congeService.createDemande(dto, chauffeurId));
    }

    @GetMapping("/demandes")
    public ResponseEntity<List<CongeDTO>> getDemandesEnAttente() {
        return ResponseEntity.ok(congeService.getDemandesEnAttente());
    }

    @GetMapping("/mes-demandes")
    public ResponseEntity<List<CongeDTO>> getMesDemandes(@AuthenticationPrincipal UserDetails userDetails) {
        Long chauffeurId = getChauffeurIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(congeService.getMesDemandes(chauffeurId));
    }

    @PutMapping("/{id}/repondre")
    public ResponseEntity<CongeDTO> repondreDemande(@PathVariable Long id,
                                                     @RequestParam String statut,
                                                     @RequestParam(required = false) String message) {
        return ResponseEntity.ok(congeService.repondreDemande(id, statut, message));
    }

    @GetMapping("/historique")
    public ResponseEntity<List<CongeDTO>> getHistorique() {
        return ResponseEntity.ok(congeService.getHistorique());
    }
}