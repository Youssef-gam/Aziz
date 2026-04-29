package com.parc.controller;

import com.parc.dto.MaintenanceDTO;
import com.parc.dto.OperateurMaintenanceDTO;
import com.parc.domain.entity.OperateurMaintenance;
import com.parc.service.OperateurMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operateurs-maintenance")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OperateurMaintenanceController {

    private final OperateurMaintenanceService operateurService;

    // ========== CRUD (réservé à l'admin) ==========
    @PostMapping
    public ResponseEntity<OperateurMaintenanceDTO> create(@RequestBody OperateurMaintenanceDTO dto) {
        return ResponseEntity.ok(operateurService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<OperateurMaintenanceDTO>> getAll() {
        return ResponseEntity.ok(operateurService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperateurMaintenanceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(operateurService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperateurMaintenanceDTO> update(@PathVariable Long id,
                                                          @RequestBody OperateurMaintenanceDTO dto) {
        return ResponseEntity.ok(operateurService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        operateurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ========== Fonctions métier (opérateur connecté) ==========
    @GetMapping("/demandes")
    public ResponseEntity<List<MaintenanceDTO>> consulterDemandes(@AuthenticationPrincipal UserDetails userDetails) {
        OperateurMaintenance operateur = operateurService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(operateurService.consulterDemandesMaintenance(operateur));
    }

    @PutMapping("/maintenances/{id}/valider")
    public ResponseEntity<MaintenanceDTO> valider(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        OperateurMaintenance operateur = operateurService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(operateurService.validerMaintenance(id, operateur));
    }

    @PutMapping("/maintenances/{id}/planifier")
    public ResponseEntity<MaintenanceDTO> planifier(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        OperateurMaintenance operateur = operateurService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(operateurService.planifierMaintenance(id, operateur));
    }

    @PutMapping("/maintenances/{id}/realiser")
    public ResponseEntity<MaintenanceDTO> realiser(@PathVariable Long id,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        OperateurMaintenance operateur = operateurService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(operateurService.realiserMaintenance(id, operateur));
    }

    @PutMapping("/maintenances/{id}/rapporter-probleme")
    public ResponseEntity<MaintenanceDTO> rapporterProbleme(@PathVariable Long id,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        OperateurMaintenance operateur = operateurService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(operateurService.rapporterProbleme(id, operateur));
    }

    @GetMapping("/historique")
    public ResponseEntity<List<MaintenanceDTO>> consulterHistorique(@AuthenticationPrincipal UserDetails userDetails) {
        OperateurMaintenance operateur = operateurService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(operateurService.consulterHistoriqueMaintenance(operateur));
    }

    @PostMapping("/maintenances/{id}/envoyer-rapport-chef")
    public ResponseEntity<String> envoyerRapportPdfAuChef(@PathVariable Long id,
                                                          @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        operateurService.envoyerRapportMaintenanceAuChef(id, userDetails.getUsername());
        return ResponseEntity.ok("Rapport PDF envoye au chef de parc");
    }

    @GetMapping(value = "/maintenances/{id}/rapport-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> telechargerRapportPdf(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        byte[] pdf = operateurService.telechargerRapportMaintenancePdf(id, userDetails.getUsername());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rapport-maintenance-" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // Filtrer par disponibilité (public)
    @GetMapping("/disponibilite/{disponibilite}")
    public ResponseEntity<List<OperateurMaintenanceDTO>> getByDisponibilite(@PathVariable String disponibilite) {
        return ResponseEntity.ok(operateurService.getByDisponibilite(disponibilite));
    }
}