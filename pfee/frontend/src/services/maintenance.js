import api from './api';

// Endpoints pour la gestion des maintenances
export const createMaintenance = (data) => api.post('/maintenances', data);
export const getMaintenances = () => api.get('/maintenances');
export const getMaintenanceById = (id) => api.get(`/maintenances/${id}`);
export const updateMaintenance = (id, data) => api.put(`/maintenances/${id}`, data);
export const deleteMaintenance = (id) => api.delete(`/maintenances/${id}`);
export const getMaintenancesByVehicule = (vehiculeId) => api.get(`/maintenances/vehicule/${vehiculeId}`);
export const getMaintenancesByGarage = (garageId) => api.get(`/maintenances/garage/${garageId}`);
export const envoyerRapportMaintenanceAuChef = (maintenanceId) =>
  api.post(`/operateurs-maintenance/maintenances/${maintenanceId}/envoyer-rapport-chef`);
