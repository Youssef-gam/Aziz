package com.parc.controller;

import com.parc.dto.FeuilleDeRouteDTO;
import com.parc.service.FeuilleDeRouteService;
import com.parc.service.PdfGenerationService;
import com.parc.domain.entity.FeuilleDeRoute;
import com.parc.repository.FeuilleDeRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feuilles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FeuilleDeRouteController {

    private final FeuilleDeRouteService feuilleDeRouteService;
    private final PdfGenerationService pdfGenerationService;
    private final FeuilleDeRouteRepository feuilleDeRouteRepository;

    // Créer une feuille de route pour une mission
    @PostMapping("/mission/{missionId}")
    public ResponseEntity<FeuilleDeRouteDTO> createForMission(@PathVariable Long missionId,
                                                              @RequestBody FeuilleDeRouteDTO dto) {
        return ResponseEntity.ok(feuilleDeRouteService.createFeuilleDeRoute(missionId, dto));
    }

    // Récupérer la feuille de route d'une mission
    @GetMapping("/mission/{missionId}")
    public ResponseEntity<FeuilleDeRouteDTO> getByMission(@PathVariable Long missionId) {
        return ResponseEntity.ok(feuilleDeRouteService.getFeuilleByMission(missionId));
    }

    // Récupérer toutes les feuilles de route
    @GetMapping
    public ResponseEntity<List<FeuilleDeRouteDTO>> getAll() {
        return ResponseEntity.ok(feuilleDeRouteService.getAllFeuilles());
    }

    // Mettre à jour une feuille de route par son ID
    @PutMapping("/{id}")
    public ResponseEntity<FeuilleDeRouteDTO> update(@PathVariable Long id,
                                                    @RequestBody FeuilleDeRouteDTO dto) {
        return ResponseEntity.ok(feuilleDeRouteService.updateFeuilleDeRoute(id, dto));
    }

    // Supprimer une feuille de route
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        feuilleDeRouteService.deleteFeuilleDeRoute(id);
        return ResponseEntity.noContent().build();
    }

    // TEST ENDPOINT - GÉNÉRATION PDF SANS AUTH
    @GetMapping("/test/pdf/{id}")
    public ResponseEntity<?> testPdfGeneration(@PathVariable Long id) {
        try {
            System.out.println("🧪 TEST Endpoint - Génération PDF pour ID: " + id);
            FeuilleDeRoute feuilleDeRoute = feuilleDeRouteRepository.findById(id)
                    .orElse(null);
            
            if (feuilleDeRoute == null) {
                System.out.println("⚠️  TEST - Feuille non trouvée");
                return ResponseEntity.ok("{\"status\":\"error\",\"message\":\"Feuille " + id + " not found\"}");
            }
            
            System.out.println("✅ TEST - Feuille trouvée: " + feuilleDeRoute.getNumeroFeuille());
            byte[] pdfBytes = pdfGenerationService.generateFeuilleDeRoutePdf(feuilleDeRoute);
            
            System.out.println("✅ TEST - PDF généré: " + pdfBytes.length + " bytes");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "feuille_" + id + ".pdf");
            
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            System.err.println("❌ TEST - Erreur PDF: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }

    // Télécharger la feuille de route en PDF
    @GetMapping("/{id}/pdf")
    @PreAuthorize("hasAnyRole('CHEF', 'ADMIN')")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        try {
            FeuilleDeRoute feuilleDeRoute = feuilleDeRouteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Feuille de route non trouvée avec id : " + id));
            
            byte[] pdfBytes = pdfGenerationService.generateFeuilleDeRoutePdf(feuilleDeRoute);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                    "feuille-de-route-" + feuilleDeRoute.getNumeroFeuille() + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Télécharger le PDF de la feuille de route d'une mission
    @GetMapping("/mission/{missionId}/pdf")
    @PreAuthorize("hasAnyRole('CHEF', 'ADMIN')")
    public ResponseEntity<?> downloadPdfByMission(@PathVariable Long missionId) {
        try {
            // Chercher la feuille de route
            var feuilleOptional = feuilleDeRouteRepository.findByMissionId(missionId);
            
            if (feuilleOptional.isEmpty()) {
                System.out.println("❌ Feuille de route non trouvée pour mission: " + missionId);
                return ResponseEntity
                    .status(404)
                    .body("{\"error\": \"Aucune feuille de route trouvée pour la mission: " + missionId + "\"}");
            }
            
            FeuilleDeRoute feuilleDeRoute = feuilleOptional.get();
            
            // Vérifier que la feuille a les données essentielles
            if (feuilleDeRoute.getNumeroFeuille() == null || feuilleDeRoute.getNumeroFeuille().isEmpty()) {
                System.err.println("⚠️ FeuilleDeRoute #" + feuilleDeRoute.getId() + " n'a pas de numérO");
                return ResponseEntity
                    .status(400)
                    .body("{\"error\": \"Feuille de route incomplète (numéro manquant)\"}");
            }
            
            // Générer le PDF
            byte[] pdfBytes = pdfGenerationService.generateFeuilleDeRoutePdf(feuilleDeRoute);
            
            if (pdfBytes == null || pdfBytes.length == 0) {
                System.err.println("❌ PDF généré vide pour feuille: " + feuilleDeRoute.getNumeroFeuille());
                return ResponseEntity
                    .status(500)
                    .body("{\"error\": \"Le PDF généré est vide\"}");
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                    "feuille-de-route-" + feuilleDeRoute.getNumeroFeuille() + ".pdf");
            headers.setContentLength(pdfBytes.length);
            
            System.out.println("✅ PDF généré avec succès: " + feuilleDeRoute.getNumeroFeuille() + " (" + pdfBytes.length + " bytes)");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
                    
        } catch (Exception e) {
            System.err.println("❌ ERREUR 500 - Génération PDF: " + e.getClass().getSimpleName());
            System.err.println("   Message: " + e.getMessage());
            e.printStackTrace();
            
            String errorMsg = e.getMessage() != null ? e.getMessage() : "Erreur interne du serveur";
            return ResponseEntity
                .status(500)
                .body("{\"error\": \"" + errorMsg + "\", \"type\": \"" + e.getClass().getSimpleName() + "\"}");
        }
    }
    
    // Endpoint de test/debug - vérifier si la feuille de route existe
    @GetMapping("/debug/mission/{missionId}")
    public ResponseEntity<?> debugGetPdfByMission(@PathVariable Long missionId) {
        try {
            var feuilleOptional = feuilleDeRouteRepository.findByMissionId(missionId);
            
            if (feuilleOptional.isEmpty()) {
                return ResponseEntity.ok("{\"found\": false, \"message\": \"Aucune feuille de route trouvée pour mission: " + missionId + "\"}");
            }
            
            FeuilleDeRoute feuille = feuilleOptional.get();
            return ResponseEntity.ok(new Object() {
                public Long id = feuille.getId();
                public String numeroFeuille = feuille.getNumeroFeuille();
                public String destination = feuille.getDestination();
                public String objetMission = feuille.getObjetMission();
                public Long missionId = feuille.getMission() != null ? feuille.getMission().getId() : null;
                public boolean found = true;
            });
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}