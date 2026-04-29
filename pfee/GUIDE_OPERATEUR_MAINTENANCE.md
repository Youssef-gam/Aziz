# 🔧 Guide - Espace Opérateur Maintenance

## 📋 Vue d'Ensemble

L'espace **Opérateur Maintenance** permet aux opérateurs de maintenance de:
- ✅ Consulter les maintenances assignées par le chef de parc
- ✅ Recevoir des notifications en temps réel
- ✅ Mettre à jour le statut des maintenances
- ✅ Générer des rapports détaillés
- ✅ Suivre les maintenances par véhicule

---

## 🎯 Fonctionnalités Principales

### **1. Tableau de Bord (Dashboard)**

**Visible**: Page d'accueil Opérateur Maintenance

**Affiche**:
- 📊 **Total Maintenances** - Nombre total à gérer
- 📅 **Maintenances Planifiées** - À commencer
- ⚙️ **En Cours** - Actuellement en traitement
- ✅ **Terminées** - Complétées
- ⚠️ **En Retard** - À traiter en priorité

**Tableau Récent**: Les 5 dernières maintenances

**Alertes**: 
- ⚠️ Maintenances en retard
- ℹ️ Maintenances en cours

---

### **2. Gestion des Maintenances**

**Menu**: `Mes Maintenances` → Liste complète

**Fonctionnalités**:

#### **Filtres**
- Par **Statut**: Planifiée, En cours, Terminée
- Par **Urgence**: Urgent (>1000 TND), Normal

#### **Actions sur chaque Maintenance**

| Action | Description |
|--------|-------------|
| **✏️ Modifier** | Éditer le type, coût, description |
| **▶️ Commencer** | Changer le statut en "EN_COURS" |
| **✓ Terminer** | Marquer comme "TERMINEE" |

#### **Informations Affichées**
- Véhicule (Immatriculation)
- Type de Maintenance
- Date Prévue
- Urgence (basée sur le coût)
- Statut actuel
- Coût estimé

---

### **3. Rapports de Maintenance**

**Menu**: `Rapports`

**3 Types de Rapports**:

#### **A. Rapport Résumé** 📊
```
Affiche:
- Total maintenances
- % Complétées
- % En cours
- % Planifiées
- Coût total
- Coût moyen
```

**Utilité**: Vue rapide de la santé générale des maintenances

#### **B. Rapport Détaillé** 📋
```
Tableau avec colonnes:
- Date
- Véhicule
- Type Maintenance
- Statut
- Coût
- Description
```

**Utilité**: Audit complet et traçabilité

#### **C. Rapport par Véhicule** 🚗
```
Pour chaque véhicule:
- Total maintenances
- Coût total
- Nombre planifiées/en cours/terminées
- Liste détaillée des maintenances
```

**Utilité**: Analyser l'historique maintenance d'un véhicule

#### **Filtres Rapports**
- **Période**: Ce mois, Ce trimestre, Cette année
- **Type**: Résumé, Détaillé, Par Véhicule

#### **Options**
- 🖨️ **Imprimer** - Générer un PDF imprimable

---

## 📱 Workflow Typique

### **Scénario 1: Recevoir et Traiter une Maintenance**

1. **Chef de Parc assigne une maintenance**
   - Chef crée une maintenance dans `Maintenances`
   - Choisit un véhicule et un type
   - Définit une date

2. **Notification Reçue** 🔔
   - **Titre**: "📋 Nouvelle Maintenance Assignée"
   - **Message**: "Une nouvelle maintenance a été créée pour le véhicule ABC123: Révision moteur"
   - **Action**: Cliquer pour aller à la liste

3. **Opérateur Consulte la Maintenance**
   - Va dans `Mes Maintenances`
   - Voit la nouvelle maintenance en **Planifiée**
   - Peut modifier les détails si nécessaire

4. **Opérateur Commence le Travail**
   - Clique **▶️ Commencer**
   - Statut change en **EN_COURS**
   - Chef de parc peut voir que c'est en cours

5. **Travail Terminé**
   - Clique **✓ Terminer**
   - Statut change en **TERMINEE**
   - **Notification envoyée au Chef**: "Maintenance terminée"

---

### **Scénario 2: Analyser les Maintenances**

1. **Aller dans `Rapports`**
2. **Choisir la période**: "Ce mois"
3. **Choisir le type**: "Par Véhicule"
4. **Consulter le résumé**:
   - Quels véhicules ont eu le plus de maintenances?
   - Quel est le coût moyen?
   - Quels sont les véhicules problématiques?
5. **Imprimer le rapport** (🖨️) pour le chef

---

### **Scénario 3: Suivre les Maintenances Urgentes**

1. **Dashboard affiche ⚠️ En Retard**
2. **Filtrer par "Urgent"** dans la liste
3. **Voir les maintenances > 1000 TND en priorité**
4. **Agir rapidement** pour respecter les délais

---

## 🔔 Système de Notifications

### **Types de Notifications Reçues**

| Type | Titre | Quand? |
|------|-------|--------|
| **MAINTENANCE_CREATED** | 📋 Nouvelle Maintenance | Chef assigne une maintenance |
| **MAINTENANCE_URGENT** | 🔴 URGENT | Coût > 1000 TND |
| **MAINTENANCE_UPDATED** | 🔧 Mise à Jour | Vous changez le statut |

### **Actions sur Notifications**

- 👁️ **Consulter** - Cliquer pour voir le détail
- ✓ **Marquer comme lue** - Disparaît des alertes
- 🔔 **Badge** - Montre le nombre de notifications non lues

---

## 📊 Statistiques Affichées

### **Sur le Dashboard**

```
🔧 TOTAL MAINTENANCES: 45
   ├─ 📅 Planifiées: 15
   ├─ ⚙️ En Cours: 8
   ├─ ✅ Terminées: 20
   └─ ⚠️ En Retard: 2
```

### **Dans la Liste**

```
Résumé en bas:
├─ Total: 45
├─ Planifiées: 15
├─ En cours: 8
└─ Terminées: 20
```

### **Dans les Rapports**

```
Résumé Détaillé:
├─ 📊 Total: 45
├─ ✅ Complétées: 20 (44.4%)
├─ ⚙️ En cours: 8 (17.8%)
├─ 📅 Planifiées: 15 (33.3%)
├─ 💰 Coût Total: 15,500 TND
└─ 💵 Coût Moyen: 344.44 TND
```

---

## ⌨️ Raccourcis & Tips

### **Filtrer Rapidement**
- Utilisez les filtres "Statut" et "Urgence" au-dessus du tableau
- Filtrez par période "Planifiée" pour voir ce qui vient

### **Modifier en Masse**
- Cliquez sur **✏️ Modifier** pour éditer une maintenance
- Changez le type, le coût, ou la description
- Cliquez **💾 Sauvegarder**

### **Suivre les Maintenances Urgentes**
- Filtre par "Urgent" pour voir les > 1000 TND
- Traitez-les en priorité!

### **Générer un Rapport pour le Chef**
- Allez dans `Rapports`
- Sélectionnez la période et le type
- Cliquez **🖨️ Imprimer**
- Envoyez au chef

---

## 🎨 Légendes des Icônes

| Icône | Signification |
|-------|--------------|
| 📊 | Statistiques/Dashboard |
| 🔧 | Maintenance |
| 📋 | Rapports/Documents |
| 📅 | Planifiée |
| ⚙️ | En cours |
| ✅ | Terminée |
| ⚠️ | Alerte/Urgent |
| 🔴 | Urgent (> 1000 TND) |
| 🟠 | Important (500-1000 TND) |
| 🟢 | Normal (< 500 TND) |

---

## ❓ Questions Fréquentes

### **Q: Comment recevoir les notifications?**
A: Les notifications apparaissent automatiquement quand le chef assigne une maintenance. Vérifiez le panneau de notifications (🔔) en haut à droite.

### **Q: Puis-je modifier une maintenance terminée?**
A: Oui, vous pouvez toujours cliquer **✏️ Modifier** sur une maintenance TERMINEE pour corriger les détails.

### **Q: Comment imprimer un rapport?**
A: Allez dans `Rapports`, choisissez la période et le type, puis cliquez **🖨️ Imprimer**.

### **Q: Que se passe-t-il si je marque une maintenance comme "Terminée"?**
A: Le chef de parc reçoit une notification et la maintenance disparaît de la liste "Planifiée" pour apparaître dans "Terminée".

### **Q: Comment suivre les maintenances les plus coûteuses?**
A: Utilisez le rapport "Par Véhicule" pour voir le coût total, ou filtrez par "Urgent" pour les > 1000 TND.

---

## 🚀 Bonnes Pratiques

✅ **À Faire**
- Consultez les notifications régulièrement
- Mettez à jour le statut dès que vous avancez
- Générez des rapports mensuels pour le chef
- Signalez immédiatement les maintenances en retard

❌ **À Éviter**
- Oublier de marquer comme "EN_COURS" quand vous commencez
- Laisser des maintenances sans mise à jour pendant longtemps
- Ne pas consulter les alertes "En Retard"
- Modifier les dates sans raison valide

---

## 📞 Support

- **Problème d'accès?** Contactez l'administrateur
- **Maintenance non visible?** Vérifiez que le Chef l'a bien assignée
- **Notification perdue?** Consultez l'historique des notifications

---

**Créé**: 16 Avril 2026
**Version**: 1.0
**Auteur**: Équipe Gestion Parc
