import api from './api';

// Service pour les notifications de maintenance
export const notifyMaintenanceCreated = async (maintenanceData) => {
  try {
    return await api.post('/notifications/maintenance/created', {
      type: 'MAINTENANCE_CREATED',
      title: `📋 Nouvelle Maintenance Assignée`,
      message: `Une nouvelle maintenance a été créée pour le véhicule ${maintenanceData.vehicule?.immatriculation || 'N/A'}: ${maintenanceData.type}`,
      data: maintenanceData,
      recipientRole: 'OPERATEUR_MAINTENANCE'
    });
  } catch (error) {
    console.error('Erreur envoi notification maintenance:', error);
  }
};

export const notifyMaintenanceUpdated = async (maintenanceData, previousStatus) => {
  try {
    const statusMessages = {
      'EN_COURS': 'Cette maintenance est maintenant EN COURS',
      'TERMINEE': 'Cette maintenance a été marquée TERMINÉE',
      'PLANIFIEE': 'Cette maintenance a été replannifiée'
    };

    return await api.post('/notifications/maintenance/updated', {
      type: 'MAINTENANCE_UPDATED',
      title: `🔧 Maintenance Mise à Jour`,
      message: `${statusMessages[maintenanceData.statut] || 'Maintenance mise à jour'} - ${maintenanceData.type}`,
      data: maintenanceData,
      recipientRole: maintenanceData.statut === 'TERMINEE' ? 'CHEF' : 'OPERATEUR_MAINTENANCE'
    });
  } catch (error) {
    console.error('Erreur envoi notification mise à jour:', error);
  }
};

export const notifyMaintenanceDeleted = async (vehiculeImmatriculation, type) => {
  try {
    return await api.post('/notifications/maintenance/deleted', {
      type: 'MAINTENANCE_DELETED',
      title: `🗑️ Maintenance Supprimée`,
      message: `La maintenance "${type}" pour le véhicule ${vehiculeImmatriculation} a été supprimée`,
      recipientRole: 'OPERATEUR_MAINTENANCE'
    });
  } catch (error) {
    console.error('Erreur envoi notification suppression:', error);
  }
};

export const notifyMaintenanceUrgent = async (maintenanceData) => {
  try {
    if (maintenanceData.cout > 1000) {
      return await api.post('/notifications/maintenance/urgent', {
        type: 'MAINTENANCE_URGENT',
        title: `🔴 URGENT - Maintenance à Coût Élevé`,
        message: `Une maintenance de ${maintenanceData.cout} TND a été assignée pour le véhicule ${maintenanceData.vehicule?.immatriculation}. Action immédiate requise!`,
        data: maintenanceData,
        recipientRole: 'OPERATEUR_MAINTENANCE'
      });
    }
  } catch (error) {
    console.error('Erreur envoi notification urgent:', error);
  }
};

export const getMaintenanceNotifications = () => {
  try {
    return api.get('/notifications/maintenance/list');
  } catch (error) {
    console.error('Erreur récupération notifications:', error);
    return [];
  }
};

export const markNotificationAsRead = (notificationId) => {
  try {
    return api.put(`/notifications/${notificationId}/read`);
  } catch (error) {
    console.error('Erreur marquer notification:', error);
  }
};
