import api from './api';

export const getParcs = () => api.get('/parcs');
export const getParc = (id) => api.get(`/parcs/${id}`);
export const createParc = (data) => api.post('/parcs', data);
export const updateParc = (id, data) => api.put(`/parcs/${id}`, data);
export const deleteParc = (id) => api.delete(`/parcs/${id}`);