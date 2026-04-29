import React, { useState, useEffect } from 'react';
import { getChefDashboardStats } from '../../services/stats';
import { getDemandesEnAttente, repondreDemande } from '../../services/conge';
import { useNotification } from '../../context/NotificationContext';

// Formateur de date
const formatDate = (dateString) => {
  if (!dateString) return '-';
  try {
    const date = new Date(dateString);
    return date.toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    });
  } catch (e) {
    return dateString;
  }
};

function ChefDashboardHome({ user }) {
  const { showSuccess, showError } = useNotification();
  const [stats, setStats] = useState({
    totalChauffeurs: 0,
    chauffeursDisponibles: 0,
    chauffeursOccupees: 0,
    chauffeursConges: 0,
    totalVehicules: 0,
    vehiculesDisponibles: 0,
    vehiculesMaintenance: 0,
    totalMissions: 0,
    missionsEnCours: 0,
    missionsTerminees: 0
  });

  const [conges, setConges] = useState([]);
  const [loading, setLoading] = useState(true);
  const [responseData, setResponseData] = useState({}); // ✅ corrigé

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      console.log('📥 Chargement des statistiques et congés...');
      
      const statsData = await getChefDashboardStats(user?.parcId);
      console.log('✅ Stats chargées:', statsData);
      setStats(statsData);
      
      const congesData = await getDemandesEnAttente();
      console.log('✅ Congés chargés:', congesData);
      setConges(congesData.data || []);
    } catch (err) {
      console.error('❌ Erreur chargement données:', err);
      console.error('Details:', err.response?.data);
      showError('Erreur lors du chargement des données');
    } finally {
      setLoading(false);
    }
  };

  const handleRepondre = async (id, statut) => {
    const message = responseData[id] || '';
    if (!window.confirm(`Êtes-vous sûr de vouloir ${statut === 'APPROUVE' ? 'approuver' : 'refuser'} cette demande ?`)) {
      return;
    }
    
    try {
      await repondreDemande(id, statut, message);
      setConges(conges.filter(c => c.id !== id));
      setResponseData({ ...responseData, [id]: '' });
      showSuccess(`✅ Demande ${statut === 'APPROUVE' ? 'approuvée' : 'refusée'} avec succès`);
    } catch (err) {
      console.error('Erreur réponse demande', err);
      showError('❌ Erreur lors du traitement de la demande');
    }
  };

  if (loading) return <div className="loading">Chargement...</div>;

  return (
    <div className="chef-dashboard-container">

      {/* Statistiques Chauffeurs */}
      <div className="stats-grid">
        <div className="stats-card">
          <div className="stats-icon">👥</div>
          <div className="stats-content">
            <h3>Chauffeurs</h3>
            <div className="stats-value">{stats.totalChauffeurs}</div>
            <p className="stats-label">dans votre parc</p>
          </div>
        </div>

        <div className="stats-card">
          <div className="stats-icon">✅</div>
          <div className="stats-content">
            <h3>Disponibles</h3>
            <div className="stats-value" style={{ color: '#10B981' }}>
              {stats.chauffeursDisponibles}
            </div>
            <p className="stats-label">chauffeurs</p>
          </div>
        </div>

        <div className="stats-card">
          <div className="stats-icon">🚀</div>
          <div className="stats-content">
            <h3>En mission</h3>
            <div className="stats-value" style={{ color: '#F59E0B' }}>
              {stats.chauffeursOccupees}
            </div>
            <p className="stats-label">en déplacement</p>
          </div>
        </div>

        <div className="stats-card">
          <div className="stats-icon">🏖️</div>
          <div className="stats-content">
            <h3>En congé</h3>
            <div className="stats-value" style={{ color: '#EF4444' }}>
              {stats.chauffeursConges}
            </div>
            <p className="stats-label">indisponibles</p>
          </div>
        </div>
      </div>

      {/* Statistiques Véhicules & Missions */}
      <div className="stats-grid">
        <div className="stats-card">
          <div className="stats-icon">🚗</div>
          <div className="stats-content">
            <h3>Véhicules</h3>
            <div className="stats-value">{stats.totalVehicules}</div>
            <p className="stats-label">dans le parc</p>
          </div>
        </div>

        <div className="stats-card">
          <div className="stats-icon">✅</div>
          <div className="stats-content">
            <h3>Disponibles</h3>
            <div className="stats-value" style={{ color: '#10B981' }}>
              {stats.vehiculesDisponibles}
            </div>
            <p className="stats-label">véhicules</p>
          </div>
        </div>

        <div className="stats-card">
          <div className="stats-icon">🔧</div>
          <div className="stats-content">
            <h3>En maintenance</h3>
            <div className="stats-value" style={{ color: '#F59E0B' }}>
              {stats.vehiculesMaintenance}
            </div>
            <p className="stats-label">en réparation</p>
          </div>
        </div>

        <div className="stats-card">
          <div className="stats-icon">📋</div>
          <div className="stats-content">
            <h3>Missions</h3>
            <div className="stats-value">{stats.totalMissions}</div>
            <p className="stats-label">ce mois</p>
          </div>
        </div>
      </div>

      {/* Section Demandes de Congés */}
      <div className="section-header">
        <h2>📅 Demandes de congés en attente ({conges.length})</h2>
      </div>

      <div className="conges-widget">
        {conges.length === 0 ? (
          <div className="empty-state">✓ Aucune demande de congé en attente</div>
        ) : (
          <div className="conges-list">
            {conges.map(c => (
              <div key={c.id} className="conge-card">
                <div className="conge-header">
                  <div className="conge-info">
                    <h4>{c.chauffeurNom}</h4>
                    <p className="conge-dates">
                      📅 {formatDate(c.dateDebut)} au {formatDate(c.dateFin)}
                    </p>
                    <p className="conge-motif">
                      <strong>Motif:</strong> {c.motif || 'Non spécifié'}
                    </p>
                  </div>
                  <div className="conge-actions">
                    <button 
                      onClick={() => handleRepondre(c.id, 'APPROUVE')} 
                      className="btn-success"
                      title="Approuver cette demande"
                    >
                      ✅ Approuver
                    </button>
                    <button 
                      onClick={() => handleRepondre(c.id, 'REFUSE')} 
                      className="btn-danger"
                      title="Refuser cette demande"
                    >
                      ❌ Refuser
                    </button>
                  </div>
                </div>
                <textarea
                  placeholder="Ajouter un message (optionnel)..."
                  value={responseData[c.id] || ''}
                  onChange={(e) => setResponseData({ ...responseData, [c.id]: e.target.value })}
                  className="response-message-textarea"
                  rows="2"
                />
              </div>
            ))}
          </div>
        )}
      </div>

    </div>
  );
}

export default ChefDashboardHome;