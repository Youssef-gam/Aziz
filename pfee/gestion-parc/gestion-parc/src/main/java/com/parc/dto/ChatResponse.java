package com.parc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Réponse du chat retournée par l'IA
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {
    private String reply;           // La réponse de l'IA
    private String action;          // Action suggérée optionnelle
    private String[] suggestions;   // Suggestions optionnelles
    private boolean success;        // Indicateur de succès
    private String errorMessage;    // Message d'erreur si applicable
    private long timestamp;         // Timestamp de la réponse
}
