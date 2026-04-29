package com.parc.controller;

import com.parc.dto.ChauffeurDTO;
import com.parc.service.ChauffeurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chauffeurs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChauffeurController {

    private final ChauffeurService chauffeurService;

    @PostMapping
    public ResponseEntity<ChauffeurDTO> create(@RequestBody ChauffeurDTO dto) {
        ChauffeurDTO created = chauffeurService.createChauffeur(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ChauffeurDTO>> getAll() {
        System.out.println("📡 [ChauffeurController] /api/chauffeurs GET - Requête reçue");
        List<ChauffeurDTO> chauffeurs = chauffeurService.getAllChauffeurs();
        System.out.println("📡 [ChauffeurController] Renvoi de " + chauffeurs.size() + " chauffeurs");
        return ResponseEntity.ok(chauffeurs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChauffeurDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(chauffeurService.getChauffeurById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChauffeurDTO> update(@PathVariable Long id, @RequestBody ChauffeurDTO dto) {
        return ResponseEntity.ok(chauffeurService.updateChauffeur(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        chauffeurService.deleteChauffeur(id);
        return ResponseEntity.ok("Chauffeur supprimé avec succès");
    }
}