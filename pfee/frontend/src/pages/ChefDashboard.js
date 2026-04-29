import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Sidebar from '../components/common/Sidebar';
import Header from '../components/common/Header';
import ChefDashboardHome from '../components/chef/ChefDashboardHome';
import ChefChauffeursList from '../components/chef/ChefChauffeursList';
import ChefVehiculesList from '../components/chef/ChefVehiculesList';
import ChefVehiculesForm from '../components/chef/ChefVehiculesForm';
import ChefMissionsList from '../components/chef/ChefMissionsList';
import ChefMissionsForm from '../components/chef/ChefMissionsForm';
import ChefProfil from '../components/chef/ChefProfil';
import ChefDeclarationsList from '../components/chef/ChefDeclarationsList';
import ChefCongesList from '../components/chef/ChefCongesList';
import ChefMaintenancesList from '../components/chef/ChefMaintenancesList';
import ChefMaintenancesForm from '../components/chef/ChefMaintenancesForm';
import ChefAssistant from '../components/chef/ChefAssistant';
import { useAuth } from '../context/AuthContext';

function ChefDashboard() {
  const { user } = useAuth();

  return (
    <div className="dashboard-layout">
      <Sidebar />
      <div className="dashboard-main">
        <Header />
        <div className="dashboard-content">
          <Routes>
            <Route index element={<ChefDashboardHome user={user} />} />
            <Route path="dashboard" element={<ChefDashboardHome user={user} />} />
            <Route path="chauffeurs" element={<ChefChauffeursList user={user} />} />
            <Route path="vehicules" element={<ChefVehiculesList user={user} />} />
            <Route path="vehicules/new" element={<ChefVehiculesForm />} />
            <Route path="vehicules/edit/:id" element={<ChefVehiculesForm />} />
            <Route path="missions" element={<ChefMissionsList user={user} />} />
            <Route path="missions/new" element={<ChefMissionsForm />} />
            <Route path="missions/edit/:id" element={<ChefMissionsForm />} />
            <Route path="declarations" element={<ChefDeclarationsList user={user} />} />
            <Route path="conges" element={<ChefCongesList user={user} />} />
            <Route path="maintenances" element={<ChefMaintenancesList user={user} />} />
            <Route path="maintenances/new" element={<ChefMaintenancesForm />} />
            <Route path="maintenances/edit/:id" element={<ChefMaintenancesForm />} />
            <Route path="profil" element={<ChefProfil />} />
          </Routes>
        </div>
      </div>
      <ChefAssistant user={user} />
    </div>
  );
}

export default ChefDashboard;