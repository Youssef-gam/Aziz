package com.parc.controller;

import com.parc.dto.FeuilleDeRouteDTO;
import com.parc.dto.MissionDTO;
import com.parc.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MissionController {

    private final MissionService missionService;

    // CRUD Mission
    @PostMapping
    public ResponseEntity<MissionDTO> create(@RequestBody MissionDTO dto) {
        return ResponseEntity.ok(missionService.createMission(dto));
    }

    @GetMapping
    public ResponseEntity<List<MissionDTO>> getAll() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MissionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(missionService.getMissionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MissionDTO> update(@PathVariable Long id, @RequestBody MissionDTO dto) {
        return ResponseEntity.ok(missionService.updateMission(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ResponseEntity.noContent().build();
    }

    // Filtres
    @GetMapping("/chauffeur/{chauffeurId}")
    public ResponseEntity<List<MissionDTO>> getByChauffeur(@PathVariable Long chauffeurId) {
        return ResponseEntity.ok(missionService.getMissionsByChauffeur(chauffeurId));
    }

    @GetMapping("/vehicule/{vehiculeId}")
    public ResponseEntity<List<MissionDTO>> getByVehicule(@PathVariable Long vehiculeId) {
        return ResponseEntity.ok(missionService.getMissionsByVehicule(vehiculeId));
    }

    // Gestion de la feuille de route
    @PostMapping("/{missionId}/feuille")
    public ResponseEntity<FeuilleDeRouteDTO> addFeuilleDeRoute(@PathVariable Long missionId, @RequestBody FeuilleDeRouteDTO dto) {
        return ResponseEntity.ok(missionService.addFeuilleDeRoute(missionId, dto));
    }

    @GetMapping("/{missionId}/feuille")
    public ResponseEntity<FeuilleDeRouteDTO> getFeuilleDeRoute(@PathVariable Long missionId) {
        return ResponseEntity.ok(missionService.getFeuilleDeRouteByMission(missionId));
    }

    @PutMapping("/{missionId}/feuille")
    public ResponseEntity<FeuilleDeRouteDTO> updateFeuilleDeRoute(@PathVariable Long missionId, @RequestBody FeuilleDeRouteDTO dto) {
        return ResponseEntity.ok(missionService.updateFeuilleDeRoute(missionId, dto));
    }

    @DeleteMapping("/{missionId}/feuille")
    public ResponseEntity<Void> deleteFeuilleDeRoute(@PathVariable Long missionId) {
        missionService.deleteFeuilleDeRoute(missionId);
        return ResponseEntity.noContent().build();
    }

    // Affectation de véhicule
    @PutMapping("/{missionId}/affecter-vehicule/{vehiculeId}")
    public ResponseEntity<MissionDTO> affecterVehicule(@PathVariable Long missionId, @PathVariable Long vehiculeId) {
        return ResponseEntity.ok(missionService.affecterVehicule(missionId, vehiculeId));
    }
}