import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { NotificationProvider } from './context/NotificationContext';
import PrivateRoute from './components/common/PrivateRoute';
import NotificationContainer from './components/common/NotificationContainer';
import Login from './components/common/Login';
import AdminDashboard from './pages/AdminDashboard';
import ChefDashboard from './pages/ChefDashboard';
import OperateurMaintenanceDashboard from './pages/OperateurMaintenanceDashboard';
import ChauffeurDashboardPage from './pages/ChauffeurDashboard';

// Styles
import './styles/global.css';
import './styles/sidebar.css';
import './styles/header.css';
import './styles/admin.css';
import './styles/dashboard.css';
import './styles/chef.css';
import './styles/notifications.css';
import './components/styles/chauffeur-dashboard.css';
import './components/styles/conge-cards.css';
import './components/styles/form.css';
import './components/styles/list.css';

function App() {
  return (
    <NotificationProvider>
      <AuthProvider>
        <BrowserRouter>
          <NotificationContainer />
          <Routes>
          {/* Page de connexion */}
          <Route path="/login" element={<Login />} />
          
          {/* Espace Administrateur */}
          <Route
            path="/admin/*"
            element={
              <PrivateRoute>
                <AdminDashboard />
              </PrivateRoute>
            }
          />
          
          {/* Espace Chef de parc */}
          <Route
            path="/chef/*"
            element={
              <PrivateRoute>
                <ChefDashboard />
              </PrivateRoute>
            }
          />

          {/* Espace Opérateur Maintenance */}
          <Route
            path="/operateur-maintenance/*"
            element={
              <PrivateRoute>
                <OperateurMaintenanceDashboard />
              </PrivateRoute>
            }
          />

          {/* Espace Chauffeur */}
          <Route
            path="/chauffeur/*"
            element={
              <PrivateRoute>
                <ChauffeurDashboardPage />
              </PrivateRoute>
            }
          />
          
          {/* Redirection par défaut */}
          <Route path="/" element={<Navigate to="/login" />} />
          
          {/* Redirection pour les routes non trouvées */}
          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
    </NotificationProvider>
  );
}

export default App;