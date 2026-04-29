import axios from 'axios';

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
});

// Intercepteur pour ajouter le token Bearer si l'utilisateur est connecté
api.interceptors.request.use(config => {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  if (user && user.token) {
    console.log('🔐 Token Bearer envoyé');
    config.headers.Authorization = `Bearer ${user.token}`;
  } else {
    console.warn('⚠️ Aucun token trouvé dans localStorage');
  }
  return config;
});

// Intercepteur pour gérer les erreurs de réponse
api.interceptors.response.use(
  response => response,
  error => {
    const isLoginRequest = error.config?.url?.includes('/auth/login');
    if (error.response?.status === 401 && !isLoginRequest) {
      console.error('❌ Authentification expirée');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;