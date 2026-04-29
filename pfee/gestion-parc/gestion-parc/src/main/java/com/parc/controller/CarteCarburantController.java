package com.parc.controller;

import com.parc.dto.CarteCarburantDTO;
import com.parc.dto.TransactionCarburantDTO;
import com.parc.domain.entity.Chauffeur;
import com.parc.service.CarteCarburantService;
import com.parc.service.ChauffeurService;
import com.parc.service.TransactionCarburantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartes")
@CrossOrigin(origins = "*")
public class CarteCarburantController {

    private final CarteCarburantService carteService;
    private final TransactionCarburantService transactionService;
    private final ChauffeurService chauffeurService;

    public CarteCarburantController(CarteCarburantService carteService,
                                    TransactionCarburantService transactionService,
                                    ChauffeurService chauffeurService) {
        this.carteService = carteService;
        this.transactionService = transactionService;
        this.chauffeurService = chauffeurService;
    }

    // ========== Endpoint spécifique pour la consommation ==========
    @PostMapping("/consommation")
    public ResponseEntity<TransactionCarburantDTO> consommer(
            @RequestParam Long carteId,
            @RequestParam Double montant,
            @RequestParam(required = false) Double litres,
            @RequestParam(required = false) Long missionId,
            @RequestParam String numeroPermis) {

        Chauffeur chauffeur = chauffeurService.getChauffeurByNumeroPermis(numeroPermis);
        TransactionCarburantDTO transaction = transactionService.enregistrerConsommation(
                carteId, montant, litres, missionId, chauffeur.getId());
        return ResponseEntity.ok(transaction);
    }

    // ========== CRUD standard ==========
    @PostMapping
    public ResponseEntity<CarteCarburantDTO> create(@RequestBody CarteCarburantDTO dto) {
        return ResponseEntity.ok(carteService.createCarte(dto));
    }

    @GetMapping
    public ResponseEntity<List<CarteCarburantDTO>> getAll() {
        return ResponseEntity.ok(carteService.getAllCartes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarteCarburantDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(carteService.getCarteById(id));
    }

    @GetMapping("/chauffeur/{chauffeurId}")
    public ResponseEntity<List<CarteCarburantDTO>> getByChauffeur(@PathVariable Long chauffeurId) {
        return ResponseEntity.ok(carteService.getCartesByChauffeur(chauffeurId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarteCarburantDTO> update(@PathVariable Long id, @RequestBody CarteCarburantDTO dto) {
        return ResponseEntity.ok(carteService.updateCarte(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carteService.deleteCarte(id);
        return ResponseEntity.noContent().build();
    }

    // ========== Endpoints métier ==========
    @PutMapping("/{id}/recharger")
    public ResponseEntity<CarteCarburantDTO> recharger(@PathVariable Long id, @RequestParam Double montant) {
        return ResponseEntity.ok(carteService.rechargerSolde(id, montant));
    }

    // Optionnel : consommation directe via l'ID de la carte (peut être utile pour l'admin)
    @PutMapping("/{id}/consommer")
    public ResponseEntity<CarteCarburantDTO> consommerCarte(@PathVariable Long id, @RequestParam Double montant) {
        return ResponseEntity.ok(carteService.consommerCarburant(id, montant));
    }
}