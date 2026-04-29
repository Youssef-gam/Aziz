import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Sidebar from '../components/common/Sidebar';
import Header from '../components/common/Header';
import ChauffeurDashboard from '../components/chauffeur/ChauffeurDashboard';
import ChauffeurAmendeForm from '../components/chauffeur/ChauffeurAmendeForm';
import ChauffeurAccidentForm from '../components/chauffeur/ChauffeurAccidentForm';
import ChauffeurCongeForm from '../components/chauffeur/ChauffeurCongeForm';
import ChauffeurCongesList from '../components/chauffeur/ChauffeurCongesList';
import { useAuth } from '../context/AuthContext';

function ChauffeurDashboardPage() {
  const { user } = useAuth();

  return (
    <div className="dashboard-layout">
      <Sidebar />
      <div className="dashboard-main">
        <Header />
        <div className="dashboard-content">
          <Routes>
            <Route index element={<ChauffeurDashboard user={user} />} />
            <Route path="dashboard" element={<ChauffeurDashboard user={user} />} />
            <Route path="amende" element={<ChauffeurAmendeForm />} />
            <Route path="accident" element={<ChauffeurAccidentForm />} />
            <Route path="conge" element={<ChauffeurCongeForm />} />
            <Route path="conges" element={<ChauffeurCongesList />} />
          </Routes>
        </div>
      </div>
    </div>
  );
}

export default ChauffeurDashboardPage;
