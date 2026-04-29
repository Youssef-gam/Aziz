import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const Sidebar = () => {
  const { user, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  // Menu selon le rôle
  const getMenuItems = () => {
    if (user?.role === 'ADMIN') {
      return [
        { path: '/admin/dashboard', label: 'Tableau de bord', icon: '📊' },
        { path: '/admin/users', label: 'Utilisateurs', icon: '👥' },
      ];
    } else if (user?.role === 'CHEF') {
      return [
        { path: '/chef/dashboard', label: 'Tableau de bord', icon: '📊' },
        { path: '/chef/chauffeurs', label: 'Chauffeurs', icon: '👨‍✈️' },
        { path: '/chef/vehicules', label: 'Véhicules', icon: '🚗' },
        { path: '/chef/missions', label: 'Missions', icon: '📋' },
        { path: '/chef/conges', label: 'Congés', icon: '📅' },
        { path: '/chef/maintenances', label: 'Maintenances', icon: '🔧' },
        { path: '/chef/profil', label: 'Mon profil', icon: '👤' },
      ];
    } else if (user?.role === 'OPERATEUR_MAINTENANCE') {
      return [
        { path: '/operateur-maintenance/dashboard', label: 'Tableau de bord', icon: '📊' },
        { path: '/operateur-maintenance/maintenances', label: 'Mes Maintenances', icon: '🔧' },
        { path: '/operateur-maintenance/rapports', label: 'Rapports', icon: '📋' },
      ];
    } else if (user?.role === 'CHAUFFEUR') {
      return [
        { path: '/chauffeur/dashboard', label: 'Tableau de bord', icon: '📊' },
        { path: '/chauffeur/conges', label: 'Mes congés', icon: '📅' },
        { path: '/chauffeur/amende', label: 'Signaler une amende', icon: '⚠️' },
        { path: '/chauffeur/accident', label: 'Déclarer accident', icon: '🚨' },
      ];
    }
    return [];
  };

  const menuItems = getMenuItems();

  // Libellé du rôle affiché en bas
  const getRoleLabel = () => {
    if (user?.role === 'ADMIN') return 'Administrateur';
    if (user?.role === 'CHEF') return 'Chef de parc';
    if (user?.role === 'OPERATEUR_MAINTENANCE') return 'Opérateur Maintenance';
    if (user?.role === 'CHAUFFEUR') return 'Chauffeur';
    return 'Utilisateur';
  };

  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <img src="/agilphoto.png" alt="AGIL ENERGY" className="sidebar-logo" />
      </div>
      <nav className="sidebar-nav">
        {menuItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={`nav-item ${location.pathname === item.path ? 'active' : ''}`}
          >
            <span className="nav-icon">{item.icon}</span>
            <span className="nav-label">{item.label}</span>
          </Link>
        ))}
      </nav>
      <div className="sidebar-footer">
        <div className="user-info">
          <div className="user-avatar">
            {user?.prenom?.charAt(0) || user?.email?.charAt(0) || 'A'}
          </div>
          <div className="user-details">
            <div className="user-name">{user?.prenom} {user?.nom}</div>
            <div className="user-role">{getRoleLabel()}</div>
          </div>
        </div>
        <button onClick={handleLogout} className="logout-btn">
          <span>🚪</span> Déconnexion
        </button>
      </div>
    </div>
  );
};

export default Sidebar;