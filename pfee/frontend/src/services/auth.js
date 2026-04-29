import api from './api';

export const login = async (username, password) => {
  try {
    const authResponse = await api.post('/auth/login', {
      email: username,
      motDePasse: password,
    });

    const userData = {
      token: authResponse.data.token,
      email: authResponse.data.email,
      role: authResponse.data.role,
      id: authResponse.data.id,
      nom: authResponse.data.nom || authResponse.data.name || '',
      prenom: authResponse.data.prenom || '',
    };

    localStorage.setItem('user', JSON.stringify(userData));
    return userData;
  } catch (error) {
    throw new Error('Identifiants incorrects');
  }
};

export const logout = () => {
  localStorage.removeItem('user');
};

export const getCurrentUser = () => {
  const user = localStorage.getItem('user');
  return user ? JSON.parse(user) : null;
};

export const isAuthenticated = () => {
  return localStorage.getItem('user') !== null;
};