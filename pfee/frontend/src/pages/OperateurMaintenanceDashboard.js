import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Sidebar from '../components/common/Sidebar';
import Header from '../components/common/Header';
import OperateurMaintenanceHome from '../components/operateur-maintenance/OperateurMaintenanceHome';
import OperateurMaintenanceList from '../components/operateur-maintenance/OperateurMaintenanceList';
import OperateurMaintenanceReports from '../components/operateur-maintenance/OperateurMaintenanceReports';
import { useAuth } from '../context/AuthContext';

function OperateurMaintenanceDashboard() {
  const { user } = useAuth();

  return (
    <div className="dashboard-layout">
      <Sidebar />
      <div className="dashboard-main">
        <Header />
        <div className="dashboard-content">
          <Routes>
            <Route index element={<OperateurMaintenanceHome user={user} />} />
            <Route path="dashboard" element={<OperateurMaintenanceHome user={user} />} />
            <Route path="maintenances" element={<OperateurMaintenanceList user={user} />} />
            <Route path="rapports" element={<OperateurMaintenanceReports user={user} />} />
          </Routes>
        </div>
      </div>
    </div>
  );
}

export default OperateurMaintenanceDashboard;
