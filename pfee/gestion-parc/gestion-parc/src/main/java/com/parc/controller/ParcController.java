package com.parc.controller;

import com.parc.dto.ParcDTO;
import com.parc.service.ParcService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ParcController {

    private final ParcService parcService;

    // Récupérer tous les parcs
    @GetMapping
    public ResponseEntity<List<ParcDTO>> getAll() {
        return ResponseEntity.ok(parcService.getAll());
    }

    // Récupérer un parc par son id
    @GetMapping("/{id}")
    public ResponseEntity<ParcDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(parcService.getById(id));
    }

    // Créer un parc
    @PostMapping
    public ResponseEntity<ParcDTO> create(@RequestBody ParcDTO dto) {
        return ResponseEntity.ok(parcService.create(dto));
    }

    // Modifier un parc
    @PutMapping("/{id}")
    public ResponseEntity<ParcDTO> update(@PathVariable Long id, @RequestBody ParcDTO dto) {
        return ResponseEntity.ok(parcService.update(id, dto));
    }

    // Supprimer un parc
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        parcService.delete(id);
        return ResponseEntity.noContent().build();
    }
}