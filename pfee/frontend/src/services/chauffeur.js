import api from './api';

export const getChauffeurs = () => api.get('/chauffeurs');
export const getChauffeur = (id) => api.get(`/chauffeurs/${id}`);
export const createChauffeur = (data) => api.post('/chauffeurs', data);
export const updateChauffeur = (id, data) => api.put(`/chauffeurs/${id}`, data);
export const deleteChauffeur = (id) => api.delete(`/chauffeurs/${id}`);
