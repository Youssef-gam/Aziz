package com.parc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Requête de chat envoyée par le client
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRequest {
    private String message;
    private String context;  // Contexte optionnel pour enrichir la requête
    private String category; // Catégorie pour générer des suggestions
}
