import api from './api';

// Récupérer tous les accidents du chauffeur
export const getAccidents = () => api.get('/accidents');

// Récupérer les accidents par chauffeur
export const getAccidentsByChauffeur = (chauffeurId) => api.get(`/accidents/chauffeur/${chauffeurId}`);

// Créer une nouvelle déclaration d'accident
export const createAccident = (data) => api.post('/accidents', data);

// Mettre à jour un accident
export const updateAccident = (id, data) => api.put(`/accidents/${id}`, data);

// Supprimer un accident
export const deleteAccident = (id) => api.delete(`/accidents/${id}`);

// Récupérer les détails d'un accident
export const getAccident = (id) => api.get(`/accidents/${id}`);
