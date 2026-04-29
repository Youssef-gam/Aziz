import api from './api';

// Récupérer toutes les amendes du chauffeur
export const getAmendes = () => api.get('/amendes');

// Récupérer les amendes par chauffeur
export const getAmendesByChauffeur = (chauffeurId) => api.get(`/amendes/chauffeur/${chauffeurId}`);

// Créer une nouvelle amende (déclaration)
export const createAmende = (data) => api.post('/amendes', data);

// Mettre à jour une amende
export const updateAmende = (id, data) => api.put(`/amendes/${id}`, data);

// Supprimer une amende
export const deleteAmende = (id) => api.delete(`/amendes/${id}`);

// Récupérer les détails d'une amende
export const getAmende = (id) => api.get(`/amendes/${id}`);
