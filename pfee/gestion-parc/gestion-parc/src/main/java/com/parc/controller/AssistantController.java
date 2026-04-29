package com.parc.controller;

import com.parc.dto.AssistantRequest;
import com.parc.dto.AssistantResponse;
import com.parc.service.AssistantAIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AssistantController {

    private final AssistantAIService assistantService;

    /**
     * Endpoint pour interroger l'assistant IA
     * @param request - Message et contexte utilisateur
     * @return Réponse de l'IA
     */
    @PostMapping("/ask")
    public ResponseEntity<AssistantResponse> askAssistant(@RequestBody AssistantRequest request) {
        try {
            String response = assistantService.processUserQuery(
                request.getMessage(),
                request.getContext()
            );
            return ResponseEntity.ok(new AssistantResponse(response, true));
        } catch (Exception e) {
            return ResponseEntity.ok(
                new AssistantResponse("❌ Erreur: " + e.getMessage(), false)
            );
        }
    }

    /**
     * Vérifier la connexion Ollama
     */
    @GetMapping("/health")
    public ResponseEntity<AssistantResponse> checkHealth() {
        return ResponseEntity.ok(new AssistantResponse("✅ Assistant IA prêt", true));
    }
}
