import api from './api';

// Endpoints pour la gestion des congés
export const createDemandeConge = (data) => api.post('/conges', data);
export const getDemandesEnAttente = () => api.get('/conges/demandes');
export const getMesDemandes = () => api.get('/conges/mes-demandes');
export const repondreDemande = (id, statut, message) => 
  api.put(`/conges/${id}/repondre`, null, { params: { statut, message } });
export const getHistoriqueConges = () => api.get('/conges/historique');
