# 🤖 Guide Complet - Assistant IA Chef de Parc avec Ollama

## 📋 Vue d'Ensemble
Cet assistant IA intègre **Ollama** (modèles IA locaux gratuits) avec votre base de données pour fournir des réponses intelligentes sur votre flotte, véhicules, chauffeurs, missions, etc.

---

## ⚙️ Installation et Configuration

### **Étape 1: Installer Ollama**

1. **Télécharger Ollama**
   - Allez sur: https://ollama.ai
   - Téléchargez la version **Windows**
   - Installez normalement (comme n'importe quelle application)

2. **Lancer Ollama**
   ```bash
   # Ouvrir PowerShell et exécuter:
   ollama pull mistral
   
   # Attendez que le modèle se télécharge (~4GB)
   # Puis lancez Ollama:
   ollama serve
   ```

3. **Vérifier la connexion**
   ```bash
   # Ouvrir une autre PowerShell et tester:
   curl http://localhost:11434/api/tags
   
   # Vous devez voir une réponse JSON
   ```

---

### **Étape 2: Démarrer le Backend Java**

1. **Vérifier application.properties**
   - Allez dans: `gestion-parc/src/main/resources/application.properties`
   - Les configs Ollama sont déjà ajoutées:
   ```properties
   ollama.url=http://localhost:11434
   ollama.model=mistral
   ```

2. **Lancer le backend**
   ```bash
   cd gestion-parc
   mvn spring-boot:run
   
   # Ou depuis l'IDE (Run > Run 'GestionParcApplication')
   ```

3. **Vérifier l'endpoint**
   ```bash
   # Tester l'endpoint d'assistant (doit répondre):
   curl http://localhost:8080/api/assistant/health
   
   # Réponse attendue: {"response":"✅ Ollama Connecté","success":true}
   ```

---

### **Étape 3: Démarrer le Frontend React**

```bash
cd frontend
npm install
npm start

# L'app s'ouvre sur http://localhost:3000
```

---

## 🎯 Utilisation de l'Assistant

### **Comment utiliser?**

1. **Se connecter** en tant que Chef de Parc
2. **Cliquer sur le bouton 💬** en bas à droite (ou ❌ si Ollama n'est pas connecté)
3. **Poser une question** sur votre parc

### **Exemples de Questions**

- "Quel est l'état de ma flotte?"
- "Quels véhicules nécessitent une maintenance?"
- "Montre-moi les chauffeurs disponibles"
- "Combien de missions en cours?"
- "Quel est le coût total des amendes ce mois-ci?"
- "Qui sont les chauffeurs en congé?"
- "État des carburants?"

---

## 📊 Architecture

```
Frontend (React)
    ↓
[ChefAssistant.js]  → Affiche l'interface ChatBot
    ↓
[ollamaService.js]  → Communique avec le backend
    ↓
Backend (Java Spring Boot)
    ↓
[AssistantController] → Reçoit les requêtes
    ↓
[AssistantAIService] → Enrichit le contexte avec les données DB
    ↓
    ├→ [VehiculeService] → Données véhicules
    ├→ [ChauffeurService] → Données chauffeurs
    ├→ [MissionService] → Données missions
    └→ [MaintenanceService] → Données maintenance
    ↓
[Ollama API] (http://localhost:11434)
    ↓
Modèle IA (Mistral/Neural Chat/etc)
    ↓
Réponse → Frontend
```

---

## 🛠️ Fichiers Créés/Modifiés

### **Frontend**
- ✅ `frontend/src/components/chef/ChefAssistant.js` - Composant chatbot
- ✅ `frontend/src/services/ollamaService.js` - Service API
- ✅ `frontend/src/styles/chatbot.css` - Styles améliorés
- ✅ `frontend/src/pages/ChefDashboard.js` - Intégration du chatbot

### **Backend**
- ✅ `gestion-parc/src/main/java/com/parc/controller/AssistantController.java` - Contrôleur
- ✅ `gestion-parc/src/main/java/com/parc/service/AssistantAIService.java` - Service IA
- ✅ `gestion-parc/src/main/java/com/parc/config/OllamaConfig.java` - Configuration Ollama
- ✅ `gestion-parc/src/main/java/com/parc/dto/AssistantRequest.java` - DTO requête
- ✅ `gestion-parc/src/main/java/com/parc/dto/AssistantResponse.java` - DTO réponse
- ✅ `gestion-parc/src/main/resources/application.properties` - Config Ollama

---

## 📝 Démarrage Rapide (Checklist)

- [ ] Installer Ollama
- [ ] Exécuter `ollama pull mistral`
- [ ] Lancer `ollama serve`
- [ ] Démarrer le backend Spring Boot
- [ ] Démarrer le frontend React
- [ ] Se connecter comme Chef de Parc
- [ ] Cliquer sur le bouton ChatBot (💬) en bas à droite
- [ ] Poser une question!

---

## 🔧 Troubleshooting

### **❌ "Ollama n'est pas accessible"**
```bash
# Vérifier que Ollama fonctionne
ollama serve

# Vérifier la connexion
curl http://localhost:11434/api/tags

# Si ça ne marche pas, redémarrer Ollama
```

### **❌ "Erreur de connexion au backend"**
```bash
# Vérifier que le backend tourne
curl http://localhost:8080/api/assistant/health

# Redémarrer le backend
mvn spring-boot:run
```

### **❌ "Les réponses sont lentes"**
- Ollama peut être lent au première requête (initialisation)
- Les réponses suivantes seront plus rapides
- Si c'est trop lent, essayez un modèle plus rapide: `neural-chat`

### **❌ "Changer le modèle IA"**
```bash
# Lister les modèles disponibles
ollama list

# Télécharger un nouveau modèle
ollama pull neural-chat
ollama pull zephyr

# Modifier dans application.properties:
ollama.model=neural-chat
```

---

## 🚀 Prochaines Étapes

### **Améliorations Possibles**

1. **Historique Chat** - Sauvegarder les conversations
2. **Analyse Avancée** - Graphiques et tendances
3. **Recommandations Auto** - Suggestions proactives
4. **Multi-langue** - Support de plusieurs langues
5. **Fine-tuning** - Entraîner Ollama sur vos données
6. **Integration API** - Connecter à d'autres services

---

## 📚 Ressources

- **Ollama**: https://ollama.ai
- **Modèles disponibles**: https://ollama.ai/library
- **Documentation Spring Boot**: https://spring.io/projects/spring-boot
- **React Docs**: https://react.dev

---

## 💡 Points Clés à Retenir

✅ **Ollama est gratuit** et fonctionne en local
✅ **Aucune clé API** requise
✅ **Données privées** - tout reste sur votre machine
✅ **Hors-ligne compatible** une fois les modèles téléchargés
✅ **Performance variable** selon le modèle et votre PC

---

**Créé le**: 16 Avril 2026
**Version**: 1.0
**Auteur**: Assistant IA
