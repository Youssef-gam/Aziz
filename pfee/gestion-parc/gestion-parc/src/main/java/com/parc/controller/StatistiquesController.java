package com.parc.controller;

import com.parc.service.StatistiquesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StatistiquesController {

    private final StatistiquesService statsService;

    @GetMapping("/consommation/parc/{parcId}")
    public ResponseEntity<Double> getConsommationParc(@PathVariable Long parcId) {
        return ResponseEntity.ok(statsService.getConsommationParc(parcId));
    }

    @GetMapping("/consommation/vehicule/{vehiculeId}")
    public ResponseEntity<Double> getConsommationVehicule(@PathVariable Long vehiculeId) {
        return ResponseEntity.ok(statsService.getConsommationVehicule(vehiculeId));
    }
}