package com.parc.controller;

import com.parc.dto.ChefDeParcDTO;
import com.parc.service.ChefDeParcService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chefs-parc")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChefDeParcController {

    private final ChefDeParcService chefDeParcService;

    // Créer un chef de parc
    @PostMapping
    public ResponseEntity<ChefDeParcDTO> createChef(@RequestBody ChefDeParcDTO dto) {
        ChefDeParcDTO created = chefDeParcService.createChef(dto);
        return ResponseEntity.ok(created);
    }

    // Récupérer tous les chefs
    @GetMapping
    public ResponseEntity<List<ChefDeParcDTO>> getAllChefs() {
        List<ChefDeParcDTO> list = chefDeParcService.getAllChefs();
        return ResponseEntity.ok(list);
    }

    // Récupérer un chef par id
    @GetMapping("/{id}")
    public ResponseEntity<ChefDeParcDTO> getChefById(@PathVariable Long id) {
        ChefDeParcDTO dto = chefDeParcService.getChefById(id);
        return ResponseEntity.ok(dto);
    }

    // Mettre à jour un chef
    @PutMapping("/{id}")
    public ResponseEntity<ChefDeParcDTO> updateChef(@PathVariable Long id, @RequestBody ChefDeParcDTO dto) {
        ChefDeParcDTO updated = chefDeParcService.updateChef(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Supprimer un chef
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChef(@PathVariable Long id) {
        chefDeParcService.deleteChef(id);
        return ResponseEntity.noContent().build();
    }
}