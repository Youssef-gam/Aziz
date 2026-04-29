package com.parc.controller;

import com.parc.dto.MaintenanceDTO;
import com.parc.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping
    public ResponseEntity<MaintenanceDTO> create(@RequestBody MaintenanceDTO dto) {
        return ResponseEntity.ok(maintenanceService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceDTO>> getAll() {
        return ResponseEntity.ok(maintenanceService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getById(id));
    }

    @GetMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<List<MaintenanceDTO>> getByVehicule(@PathVariable Long vehiculeId) {
        return ResponseEntity.ok(maintenanceService.getByVehicule(vehiculeId));
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<MaintenanceDTO>> getByGarage(@PathVariable Long garageId) {
        return ResponseEntity.ok(maintenanceService.getByGarage(garageId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceDTO> update(@PathVariable Long id, @RequestBody MaintenanceDTO dto) {
        return ResponseEntity.ok(maintenanceService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        maintenanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}