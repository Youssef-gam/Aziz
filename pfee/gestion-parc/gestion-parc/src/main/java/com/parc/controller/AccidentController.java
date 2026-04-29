package com.parc.controller;

import com.parc.dto.AccidentDTO;
import com.parc.service.AccidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accidents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccidentController {

    private final AccidentService accidentService;

    @PostMapping
    public ResponseEntity<AccidentDTO> create(@RequestBody AccidentDTO dto) {
        return ResponseEntity.ok(accidentService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<AccidentDTO>> getAll() {
        return ResponseEntity.ok(accidentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccidentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(accidentService.getById(id));
    }

    @GetMapping("/chauffeur/{chauffeurId}")
    public ResponseEntity<List<AccidentDTO>> getByChauffeur(@PathVariable Long chauffeurId) {
        return ResponseEntity.ok(accidentService.getByChauffeur(chauffeurId));
    }

    @GetMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<List<AccidentDTO>> getByVehicule(@PathVariable Long vehiculeId) {
        return ResponseEntity.ok(accidentService.getByVehicule(vehiculeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccidentDTO> update(@PathVariable Long id, @RequestBody AccidentDTO dto) {
        return ResponseEntity.ok(accidentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accidentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}