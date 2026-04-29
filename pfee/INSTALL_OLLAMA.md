# Installation & Démarrage Ollama

## 1. Télécharger Ollama
- Site: https://ollama.ai
- Télécharger pour Windows
- Installer (installation simple)

## 2. Démarrer Ollama
```bash
# Ouvrir PowerShell et exécuter:
ollama pull mistral

# Démarrer le serveur (automatique ou):
ollama serve
```

## 3. Vérifier que ça fonctionne
```bash
# Dans une autre console:
curl http://localhost:11434/api/generate -d '{
  "model": "mistral",
  "prompt": "Bonjour"
}'
```

## 4. Modèles recommandés (gratuits)
- `ollama pull mistral` - Rapide, bon français
- `ollama pull neural-chat` - Conversation naturelle
- `ollama pull zephyr` - Très bon pour instructions

## 5. Port par défaut
- Ollama tourne sur `http://localhost:11434`
