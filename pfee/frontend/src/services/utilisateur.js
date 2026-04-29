import api from './api';

export const getUtilisateurs = () => api.get('/utilisateurs');
export const getUtilisateur = (id) => api.get(`/utilisateurs/${id}`);
export const createUtilisateur = (data) => api.post('/utilisateurs', data);
export const updateUtilisateur = (id, data) => api.put(`/utilisateurs/${id}`, data);
export const deleteUtilisateur = (id) => api.delete(`/utilisateurs/${id}`);
export const deleteChauffeur = (id) => api.delete(`/chauffeurs/${id}`);