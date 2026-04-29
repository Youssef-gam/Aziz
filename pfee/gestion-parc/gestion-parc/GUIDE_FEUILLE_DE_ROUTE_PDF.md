# 📋 GUIDE D'INTÉGRATION - FEUILLE DE ROUTE EN PDF

## Vue d'ensemble
Quand une **mission est créée**, une **feuille de route est générée automatiquement** en PDF.

---

## 🔌 ENDPOINTS API

### 1. **Créer une mission** (génère automatiquement la feuille de route)
```
POST /api/missions
Content-Type: application/json

{
  "description": "Livraison urgente",
  "destination": "Tunis Centre",
  "dateDebut": "2026-04-14",
  "dateFin": "2026-04-14",
  "statut": "PLANIFIEE",
  "vehiculeId": 1,
  "chauffeurId": 1
}
```

**Réponse:** La mission est créée ET une `FeuilleDeRoute` est générée automatiquement

---

### 2. **Télécharger le PDF de la feuille de route (par ID)**
```
GET /api/feuilles/{id}/pdf
```

**Exemple:**
```
GET /api/feuilles/1/pdf
```

**Réponse:** Fichier PDF `feuille-de-route-FDR-1-TIMESTAMP.pdf`

---

### 3. **Télécharger le PDF de la feuille de route (par Mission ID)**
```
GET /api/feuilles/mission/{missionId}/pdf
```

**Exemple:**
```
GET /api/feuilles/mission/1/pdf
```

**Réponse:** Fichier PDF de la feuille associée à la mission

---

### 4. **Récupérer la feuille de route d'une mission**
```
GET /api/feuilles/mission/{missionId}
```

---

## 📱 INTÉGRATION FRONTEND (React)

### Créer une mission
```javascript
const createMissionWithPdf = async (missionData) => {
  try {
    const response = await fetch('http://localhost:8080/api/missions', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(missionData)
    });

    const mission = await response.json();
    console.log('✅ Mission créée avec ID:', mission.id);
    console.log('📋 Feuille de route ID:', mission.feuilleDeRoute.id);
    
    return mission;
  } catch (error) {
    console.error('Erreur:', error);
  }
};
```

---

### Télécharger le PDF
```javascript
const downloadPdf = async (feuilleDeRouteId) => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/feuilles/${feuilleDeRouteId}/pdf`,
      {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }
    );

    if (!response.ok) {
      throw new Error('Erreur lors du téléchargement');
    }

    // Créer un blob et télécharger
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `feuille-de-route-${feuilleDeRouteId}.pdf`;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
    a.remove();
    
    console.log('✅ PDF téléchargé');
  } catch (error) {
    console.error('Erreur:', error);
  }
};
```

---

### Bouton de téléchargement dans le formulaire
```jsx
<button 
  onClick={() => downloadPdf(mission.feuilleDeRoute.id)}
  disabled={!mission.feuilleDeRoute}
>
  📥 Télécharger PDF
</button>
```

---

## 📄 CONTENU DU PDF GÉNÉRÉ

Le PDF contient:
- ✅ **Numéro de feuille** (FDR-{missionId}-{timestamp})
- ✅ **Destination**
- ✅ **Objet de la mission**
- ✅ **Chauffeur** (nom, prénom)
- ✅ **Véhicule** (immatriculation)
- ✅ **Dates** (début, fin)
- ✅ **Nombre de participants**
- ✅ **Zone de signature**
- ✅ **Date de création**

---

## 🔧 CONFIGURATION SPRING BOOT

### Dépendance Maven (déjà ajoutée)
```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext7-core</artifactId>
    <version>7.2.5</version>
</dependency>
```

---

## ⚙️ FLUX COMPLET

1. **User crée une mission** via le formulaire
2. **Backend reçoit la requête POST /api/missions**
3. **Mission est sauvegardée** dans la base
4. **FeuilleDeRoute est créée automatiquement** (ID, numéro, destination, etc.)
5. **La mission retournée inclut feuilleDeRoute**
6. **Frontend affiche le bouton "Télécharger PDF"**
7. **User clique sur "Télécharger PDF"**
8. **Backend génère le PDF** en temps réel (iText7)
9. **Utilisateur reçoit le fichier PDF**

---

## 🚀 PROCHAINES ÉTAPES

- [ ] Tester la création de mission dans le frontend
- [ ] Implémenter le bouton de téléchargement PDF
- [ ] Ajouter un aperçu du PDF avant téléchargement
- [ ] Implémenter l'envoi par email de la feuille de route
- [ ] Ajouter de la signature électronique (optionnel)

---

## 📞 SUPPORT

Si tu as des questions, fais-moi signe!
