import api from './api';
import axios from 'axios';

export const getMissions = () => api.get('/missions');
export const getMission = (id) => api.get(`/missions/${id}`);
export const createMission = (data) => api.post('/missions', data);
export const updateMission = (id, data) => api.put(`/missions/${id}`, data);
export const deleteMission = (id) => api.delete(`/missions/${id}`);
export const getMissionsByChauffeur = (chauffeurId) => api.get(`/missions/chauffeur/${chauffeurId}`);

/**
 * Télécharge la feuille de route PDF pour une mission
 * @param {number} missionId - ID de la mission
 * @param {string} description - Description de la mission (pour le nom du fichier)
 * @returns {Promise<boolean>} true si succès, reject si erreur
 */
export const downloadFeuilleDeRoutePdf = async (missionId, description) => {
  try {
    console.log(`📥 Téléchargement PDF pour mission ${missionId}...`);
    
    // Récupérer le token JWT stocké
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    const token = user?.token;
    
    if (!token) {
      throw {
        code: 'NO_AUTH',
        message: '🔐 Authentification requise - Veuillez vous reconnecter',
        action: 'Déconnecter puis vous reconnecter'
      };
    }
    
    // Vérifier que l'utilisateur a le bon rôle
    if (!user.role || !['CHEF', 'ADMIN'].includes(user.role)) {
      throw {
        code: 'INVALID_ROLE',
        message: '⚠️ Accès réservé aux Chefs de Parc et Administrateurs',
        action: 'Contacter un administrateur'
      };
    }
    
    const apiUrl = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
    console.log(`🔗 URL API: ${apiUrl}/feuilles/mission/${missionId}/pdf`);
    
    // Effectuer la requête avec axios directement pour mieux contrôler responseType
    const response = await axios.get(
      `${apiUrl}/feuilles/mission/${missionId}/pdf`,
      {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Accept': 'application/pdf'
        },
        responseType: 'blob',
        timeout: 30000 // 30 secondes
      }
    );

    // Vérifier que nous avons bien reçu un PDF
    const contentType = response.headers['content-type'];
    if (!contentType || !contentType.includes('application/pdf')) {
      throw {
        code: 'INVALID_CONTENT_TYPE',
        message: '❌ Réponse invalide: Le serveur n\'a pas retourné un PDF',
        action: 'Vérifier que la feuille de route existe'
      };
    }

    console.log(`✅ PDF reçu: ${response.data.size} bytes`);

    // response.data est un Blob
    const blob = response.data;
    
    if (blob.size === 0) {
      throw {
        code: 'EMPTY_PDF',
        message: '❌ Le PDF généré est vide',
        action: 'Vérifier les détails de la mission'
      };
    }
    
    // Nettoyer le nom du fichier
    const sanitizedDescription = (description || 'mission')
      .replace(/[^a-zA-Z0-9-_]/g, '_')
      .substring(0, 30);
    
    const filename = `feuille-de-route-${missionId}-${sanitizedDescription}.pdf`;
    
    // Créer un lien de téléchargement
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    
    console.log(`✅ Téléchargement du fichier: ${filename}`);
    link.click();
    
    // Nettoyer
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);

    return true;
    
  } catch (error) {
    // Si c'est une erreur structurée que nous avons lancée
    if (error.code) {
      console.error('❌ Erreur structurée:', error);
      throw new Error(`${error.message}\n\n💡 ${error.action}`);
    }
    
    // Sinon, traiter les erreurs Axios
    const status = error.response?.status;
    const errorData = error.response?.data;
    
    console.error('❌ Erreur détaillée:', {
      message: error.message,
      status: status,
      statusText: error.response?.statusText,
      data: errorData,
      fullError: error
    });
    
    // Construire un message d'erreur informatif
    let userMessage = '❌ Impossible de télécharger le PDF';
    let debugInfo = '';
    
    // Essayer de extraire un message depuis la réponse
    if (errorData) {
      try {
        let errorText = errorData;
        if (errorData instanceof Blob) {
          errorText = await new Blob([errorData]).text();
        }
        const parsed = typeof errorText === 'string' ? JSON.parse(errorText) : errorText;
        debugInfo = parsed.error || parsed.message || '';
      } catch (e) {
        debugInfo = errorData?.message || String(errorData);
      }
    }
    
    // Messages d'erreur spécifiques par code HTTP
    switch (status) {
      case 400:
        userMessage = '❌ Requête invalide\n\n💡 Vérifiez que l\'ID de mission est correct';
        break;
      case 401:
        userMessage = '🔐 Authentification requise\n\n💡 Veuillez vous reconnecter';
        break;
      case 403:
        userMessage = '⚠️ Accès refusé\n\n💡 Vous n\'avez pas les droits pour cette action';
        break;
      case 404:
        userMessage = '❌ Feuille de route non trouvée\n\n💡 Créez d\'abord la feuille de route (elle doit être créée automatiquement)';
        break;
      case 500:
        userMessage = '❌ Erreur serveur\n\n💡 Le serveur a rencontré un problème. Vérifications:\n- Mission complète (chauffeur + véhicule assignés)\n- Chauffeur: nom et prénom remplis\n- Véhicule: marque, modèle, immatriculation remplis\n- Consultez les logs du serveur';
        break;
      case 408:
      case 504:
        userMessage = '⏰ Délai d\'attente dépassé\n\n💡 Le serveur est peut-être surchargé, réessayez dans quelques secondes';
        break;
      case 0:
        // Erreur de réseau (pas de réponse du serveur)
        userMessage = '🌐 Erreur de connectivité\n\n💡 Vérifiez que:\n- Le serveur est actif (port 8080)\n- Pas de problème de réseau\n- L\'URL API est correcte';
        break;
      default:
        if (debugInfo) {
          userMessage = `❌ Erreur (${status}): ${debugInfo}`;
        }
    }
    
    throw new Error(userMessage);
  }
};