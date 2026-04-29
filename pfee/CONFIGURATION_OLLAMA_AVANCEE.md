# Configuration Avancée Ollama

## Modèles IA Disponibles et Recommandations

### **1. Mistral** (Par défaut - Recommandé)
```bash
ollama pull mistral
```
- **Taille**: ~4GB
- **Vitesse**: ⚡⚡⚡ Rapide
- **Qualité**: ⭐⭐⭐⭐ Très bon français
- **RAM requise**: 8GB minimum
- **Usecases**: Bon pour la plupart des tâches

### **2. Neural Chat** (Spécialisé Conversations)
```bash
ollama pull neural-chat
```
- **Taille**: ~4GB
- **Vitesse**: ⚡⚡⚡⚡ Très rapide
- **Qualité**: ⭐⭐⭐⭐ Excellent pour chats
- **RAM requise**: 8GB
- **Usecases**: Meilleur pour les conversations naturelles

### **3. Zephyr** (Suivi d'instructions)
```bash
ollama pull zephyr
```
- **Taille**: ~4GB
- **Vitesse**: ⚡⚡⚡ Rapide
- **Qualité**: ⭐⭐⭐⭐⭐ Excellent suivi d'instructions
- **RAM requise**: 8GB
- **Usecases**: Questions précises et requêtes structurées

### **4. Llama2** (Grand modèle)
```bash
ollama pull llama2
```
- **Taille**: ~7GB
- **Vitesse**: ⚡⚡ Plus lent
- **Qualité**: ⭐⭐⭐⭐⭐ Très complet
- **RAM requise**: 16GB
- **Usecases**: Tâches complexes, analyse approfondie

### **5. Orca-Mini** (Léger et rapide)
```bash
ollama pull orca-mini
```
- **Taille**: ~1.3GB
- **Vitesse**: ⚡⚡⚡⚡⚡ Super rapide
- **Qualité**: ⭐⭐⭐ Bon
- **RAM requise**: 4GB
- **Usecases**: PC faible ressources

---

## Configuration dans application.properties

```properties
# Modèles rapides (pour PC standard)
ollama.model=mistral
# ou
ollama.model=neural-chat

# Modèles puissants (pour PC haute performance)
ollama.model=zephyr
ollama.model=llama2

# Modèles légers (pour PC faible)
ollama.model=orca-mini
```

---

## Paramètres Avancés Ollama

### Dans AssistantAIService.java

```java
Map<String, Object> request = new HashMap<>();
request.put("model", ollamaModel);
request.put("prompt", prompt);
request.put("stream", false);

// Paramètres avancés:
request.put("temperature", 0.7);      // 0.0 = déterministe, 1.0 = créatif
request.put("top_k", 40);             // Limite les options
request.put("top_p", 0.9);            // Limite cumulatif de probabilité
request.put("num_predict", 512);      // Longueur max réponse
request.put("repeat_penalty", 1.1);   // Pénalité répétition
request.put("num_thread", 4);         // Nombre de threads
```

---

## Optimisation de la Performance

### **1. Augmenter la vitesse**
```bash
# Utiliser un modèle plus léger
ollama pull neural-chat

# Ou utiliser orca-mini pour PC faible
ollama pull orca-mini
```

### **2. Réduire l'usage RAM**
```bash
# Dans application.properties:
ollama.model=orca-mini

# Ou charger le modèle avec options:
ollama run mistral --num-predict 256
```

### **3. Garder le modèle en mémoire**
```bash
# Élevé en RAM mais plus rapide
ollama.keep_alive=24h
```

---

## Commandes Utiles

```bash
# Lister les modèles installés
ollama list

# Récupérer info d'un modèle
ollama show mistral

# Supprimer un modèle
ollama rm neural-chat

# Voir les modèles en cours d'exécution
ollama ps

# Changer le port Ollama (défaut 11434)
ollama serve --host 0.0.0.0:11434
```

---

## Dépannage Performance

### **Les réponses sont lentes**

**Diagnostic:**
```bash
# Vérifier CPU/RAM
ollama ps

# Vérifier la taille du modèle
ollama list
```

**Solutions:**
1. Utilisez un modèle plus léger (neural-chat, orca-mini)
2. Fermez d'autres applications
3. Réduisez `num_predict` dans AssistantAIService
4. Augmentez la RAM disponible

### **Erreur "out of memory"**

**Solutions:**
```bash
# Décharger le modèle actuel
ollama list

# Utiliser un modèle plus petit
ollama pull orca-mini

# Changer dans application.properties:
ollama.model=orca-mini
```

---

## Comparaison Modèles pour Chef de Parc

| Tâche | Modèle Recommandé | Raison |
|-------|-------------------|--------|
| Questions statistiques | Mistral, Zephyr | Précis pour l'analyse |
| Conversations naturelles | Neural Chat | Meilleur pour le dialogue |
| Éditions légères PC | Orca-Mini | Très léger (~1.3GB) |
| Analyses complexes | Llama2, Zephyr | Plus puissants |
| Recommandations | Mistral | Bon équilibre |

---

## Configuration Multi-Modèles

Vous pouvez avoir plusieurs modèles installés et changer dynamiquement:

```bash
# Installer plusieurs modèles
ollama pull mistral
ollama pull neural-chat
ollama pull zephyr

# Dans l'appli, changer via API:
PUT /api/config/model?model=neural-chat
```

---

**Dernière mise à jour**: 16 Avril 2026
