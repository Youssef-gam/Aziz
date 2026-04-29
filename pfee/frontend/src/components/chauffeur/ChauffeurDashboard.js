import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import { getMissionsByChauffeur } from '../../services/mission';
import { getMesDemandes } from '../../services/conge';
import '../styles/chauffeur-dashboard.css';

function ChauffeurDashboard() {
  const { user } = useAuth();
  const { showSuccess, showError } = useNotification();
  const [missions, setMissions] = useState([]);
  const [conges, setConges] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user && user.id) {
      loadDashboardData();
    }
  }, [user]);

  const loadDashboardData = async () => {
    setLoading(true);
    try {
      console.log('🚀 Chargement données - User ID:', user.id);
      
      // Récupérer les missions du chauffeur
      const missionsRes = await getMissionsByChauffeur(user.id);
      console.log('📋 Missions reçues:', missionsRes.data);
      setMissions(missionsRes.data || []);

      // Récupérer les demandes de congé
      const congesRes = await getMesDemandes();
      console.log('📅 Congés reçus:', congesRes.data);
      setConges(congesRes.data || []);

      console.log('✅ Dashboard data loaded');
      showSuccess('✅ Données chargées');
    } catch (err) {
      console.error('❌ Erreur chargement:', err);
      showError('❌ Erreur lors du chargement: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Chargement...</div>;
  }

  return (
    <div className="chauffeur-dashboard">
      <div className="dashboard-header">
        <h1>👤 Bienvenue, {user.prenom} {user.nom}</h1>
        <p className="role-badge">🚗 Chauffeur</p>
      </div>

      <div className="dashboard-grid">
        {/* Missions assignées */}
        <div className="dashboard-card missions">
          <div className="card-header">
            <h2>🚗 Mes Missions ({missions.length})</h2>
          </div>
          <div className="card-content">
            {missions.length === 0 ? (
              <p className="empty-state">Aucune mission assignée</p>
            ) : (
              <div className="missions-list">
                {missions.slice(0, 5).map(mission => (
                  <div key={mission.id} className="mission-item">
                    <div className="mission-info">
                      <p className="mission-destination">📍 {mission.destination}</p>
                      <p className="mission-date">{mission.dateDebut}</p>
                    </div>
                    <span className="mission-status">{mission.statut}</span>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* Demandes de congé */}
        <div className="dashboard-card conges">
          <div className="card-header">
            <h2>📅 Mes Demandes de Congé ({conges.length})</h2>
          </div>
          <div className="card-content">
            {conges.length === 0 ? (
              <p className="empty-state">Aucune demande de congé</p>
            ) : (
              <div className="conges-list">
                {conges.slice(0, 5).map(conge => (
                  <div key={conge.id} className={`conge-item status-${conge.statut}`}>
                    <div className="conge-info">
                      <p className="conge-dates">
                        {conge.dateDebut} à {conge.dateFin}
                      </p>
                      <small className="conge-reason">{conge.motif}</small>
                    </div>
                    <span className={`conge-status ${conge.statut}`}>
                      {conge.statut === 'APPROUVE' && '✅ Approuvé'}
                      {conge.statut === 'REFUSE' && '❌ Refusé'}
                      {conge.statut === 'EN_ATTENTE' && '⏳ En attente'}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ChauffeurDashboard;
