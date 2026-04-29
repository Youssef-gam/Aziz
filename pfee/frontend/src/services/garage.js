import api from './api';

// Endpoints pour la gestion des garages
export const getGarages = () => api.get('/garages');
export const getGarageById = (id) => api.get(`/garages/${id}`);
export const createGarage = (data) => api.post('/garages', data);
export const updateGarage = (id, data) => api.put(`/garages/${id}`, data);
export const deleteGarage = (id) => api.delete(`/garages/${id}`);
