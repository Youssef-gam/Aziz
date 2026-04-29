import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useNotification } from '../../context/NotificationContext';
import { getMissions, deleteMission } from '../../services/mission';
import '../styles/list.css';

function ChefMissionsList() {
  const { showSuccess, showError } = useNotification();
  const [missions, setMissions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filterStatut, setFilterStatut] = useState('');

  useEffect(() => {
    loadMissions();
  }, []);

  const loadMissions = async () => {
    setLoading(true);
    try {
      const response = await getMissions();
      console.log('📋 Missions reçues:', response.data);
      setMissions(response.data || []);
    } catch (err) {
      console.error('❌ Erreur chargement missions:', err);
      showError('❌ Erreur lors du chargement des missions');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id, description) => {
    if (window.confirm(`Supprimer la mission "${description}" ?`)) {
      try {
        await deleteMission(id);
        setMissions(missions.filter(m => m.id !== id));
        showSuccess('✅ Mission supprimée avec succès');
      } catch (err) {
        console.error('❌ Erreur suppression:', err);
        showError('❌ Erreur suppression: ' + (err.response?.data?.message || err.message));
      }
    }
  };

  const getStatutLabel = (statut) => {
    const labels = {
      'PLANIFIEE': { text: '📅 Planifiée', class: 'badge-warning' },
      'EN_COURS': { text: '🚗 En cours', class: 'badge-active' },
      'TERMINEE': { text: '✅ Terminée', class: 'badge-success' },
      'SUSPENDUE': { text: '⏸️ Suspendue', class: 'badge-danger' }
    };
    return labels[statut] || { text: '❓ ' + statut, class: 'badge-default' };
  };

  const filteredMissions = filterStatut
    ? missions.filter(m => m.statut === filterStatut)
    : missions;

  if (loading) {
    return <div className="loading">Chargement des missions...</div>;
  }

  return (
    <div className="list-container">
      <div className="page-header">
        <h2>📋 Missions</h2>
        <Link to="/chef/missions/new" className="btn-primary">+ Nouvelle mission</Link>
      </div>

      <div className="filters-bar">
        <select 
          value={filterStatut} 
          onChange={(e) => setFilterStatut(e.target.value)}
          className="filter-select"
        >
          <option value="">📊 Tous les statuts</option>
          <option value="PLANIFIEE">📅 Planifiée</option>
          <option value="EN_COURS">🚗 En cours</option>
          <option value="TERMINEE">✅ Terminée</option>
          <option value="SUSPENDUE">⏸️ Suspendue</option>
        </select>
      </div>

      {filteredMissions.length === 0 ? (
        <div className="empty-state">
          <p>🚫 Aucune mission trouvée</p>
          <Link to="/chef/missions/new" className="btn-primary">Créer la première mission</Link>
        </div>
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Description</th>
                <th>Destination</th>
                <th>Date</th>
                <th>Chauffeur</th>
                <th>Véhicule</th>
                <th>Statut</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredMissions.map(mission => {
                const statut = getStatutLabel(mission.statut);
                return (
                  <tr key={mission.id}>
                    <td className="id-cell">{mission.id}</td>
                    <td><strong>{mission.description || 'N/A'}</strong></td>
                    <td>{mission.destination || '-'}</td>
                    <td>{mission.dateDebut || '-'}</td>
                    <td>{mission.chauffeurNom || 'En attente'}</td>
                    <td>{mission.vehiculeMatricule || 'En attente'}</td>
                    <td>
                      <span className={`status-badge ${statut.class}`}>
                        {statut.text}
                      </span>
                    </td>
                    <td className="actions">
                      <Link to={`/chef/missions/edit/${mission.id}`} className="btn-edit">
                        ✏️ Modifier
                      </Link>
                      <button 
                        onClick={() => handleDelete(mission.id, mission.description)}
                        className="btn-delete"
                      >
                        🗑️ Supprimer
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      <div className="stats-footer">
        Total: <strong>{filteredMissions.length}</strong> mission(s)
      </div>
    </div>
  );
}

export default ChefMissionsList;
