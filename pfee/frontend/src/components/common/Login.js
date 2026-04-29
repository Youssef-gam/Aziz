import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();
  const { showError } = useNotification();
  const [showTestCredentials, setShowTestCredentials] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const userData = await login(username, password);
      // Redirection selon le rôle
      if (userData.role === 'ADMIN') {
        navigate('/admin');
      } else if (userData.role === 'CHEF') {
        navigate('/chef');
      } else if (userData.role === 'CHAUFFEUR') {
        navigate('/chauffeur');
      } else if (userData.role === 'OPERATEUR_MAINTENANCE') {
        navigate('/operateur-maintenance/dashboard');
      } else {
        navigate('/');
      }
    } catch (err) {
      showError('❌ Identifiants incorrects. Vérifie ton email et ton mot de passe.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <div className="logo">
            <span>AGIL</span> ENERGY
          </div>
          <h2>Gestion de parc automobile</h2>
        </div>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              placeholder="exemple@agil.com"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Mot de passe</label>
            <input
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit" disabled={loading}>
            {loading ? 'Connexion...' : 'Se connecter'}
          </button>
        </form>
        
        <div className="login-help">
          <button 
            type="button" 
            onClick={() => setShowTestCredentials(!showTestCredentials)}
            className="test-credentials-btn"
          >
            {showTestCredentials ? '❌ Masquer' : '💡'} Identifiants de test
          </button>
          
          {showTestCredentials && (
            <div className="test-credentials-box">
              <h3>🔐 Compte de test</h3>
              <div className="credential-item">
                <strong>Admin:</strong>
                <p>Email: <code>admin@agil.com</code></p>
                <p>Mot de passe: <code>test123</code></p>
              </div>
              <div className="credential-item">
                <strong>Chef de parc:</strong>
                <p>Email: <code>chef@agil.com</code></p>
                <p>Mot de passe: <code>test123</code></p>
              </div>
              <div className="credential-item">
                <strong>Chauffeur:</strong>
                <p>Email: <code>pierre@agil.com</code></p>
                <p>Mot de passe: <code>test123</code></p>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Login;