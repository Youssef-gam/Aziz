package com.parc.controller;

import com.parc.dto.AmendeDTO;
import com.parc.service.AmendeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/amendes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AmendeController {

    private final AmendeService amendeService;

    @PostMapping
    public ResponseEntity<AmendeDTO> create(@RequestBody AmendeDTO dto) {
        return ResponseEntity.ok(amendeService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<AmendeDTO>> getAll() {
        return ResponseEntity.ok(amendeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmendeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(amendeService.getById(id));
    }

    @GetMapping("/chauffeur/{chauffeurId}")
    public ResponseEntity<List<AmendeDTO>> getByChauffeur(@PathVariable Long chauffeurId) {
        return ResponseEntity.ok(amendeService.getByChauffeur(chauffeurId));
    }

    @GetMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<List<AmendeDTO>> getByVehicule(@PathVariable Long vehiculeId) {
        return ResponseEntity.ok(amendeService.getByVehicule(vehiculeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AmendeDTO> update(@PathVariable Long id, @RequestBody AmendeDTO dto) {
        return ResponseEntity.ok(amendeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        amendeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/payer")
    public ResponseEntity<AmendeDTO> marquerPayee(@PathVariable Long id) {
        return ResponseEntity.ok(amendeService.marquerPayee(id));
    }
}