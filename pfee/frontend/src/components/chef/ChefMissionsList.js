import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useNotification } from '../../context/NotificationContext';
import { getMissions, deleteMission } from '../../services/mission';
import { downloadFeuilleDeRoutePdf } from '../../services/mission';

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
      console.log('📋 Missions reçues:', response.data?.length || 0);
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
        showSuccess('✅ Mission supprimée');
      } catch (err) {
        console.error('❌ Erreur suppression:', err);
        showError('❌ Erreur: ' + (err.response?.data?.message || err.message));
      }
    }
  };

  const handleDownloadPdf = async (missionId, description) => {
    try {
      await downloadFeuilleDeRoutePdf(missionId, description);
      showSuccess('✅ PDF téléchargé avec succès');
    } catch (err) {
      console.error('❌ Erreur téléchargement PDF:', err);
      
      // Afficher le message d'erreur complet avec emojis et suggestions
      const errorMessage = err.message || 'Impossible de télécharger le PDF';
      
      showError(errorMessage);
      
      // Afficher aussi des suggestion basées sur le type d'erreur
      if (errorMessage.includes('Authentification requise')) {
        setTimeout(() => {
          showError('💡 Astuce: Déconnectez-vous et reconnectez-vous pour rafraîchir votre session');
        }, 500);
      } else if (errorMessage.includes('Feuille de route non trouvée')) {
        setTimeout(() => {
          showError('💡 La feuille de route doit être créée automatiquement. Vérifiez les logs du serveur');
        }, 500);
      } else if (errorMessage.includes('connectivité')) {
        setTimeout(() => {
          showError('💡 Redémarrez le serveur backend: mvn spring-boot:run');
        }, 500);
      }
    }
  };

  const getStatutLabel = (statut) => {
    const labels = {
      'PLANIFIEE': { text: 'Planifiée', color: 'warning' },
      'EN_COURS': { text: 'En cours', color: 'active' },
      'TERMINEE': { text: 'Terminée', color: 'success' },
      'SUSPENDUE': { text: 'Suspendue', color: 'danger' }
    };
    return labels[statut] || { text: statut, color: 'default' };
  };

  const filteredMissions = filterStatut
    ? missions.filter(m => m.statut === filterStatut)
    : missions;

  if (loading) {
    return <div className="loading">Chargement des missions...</div>;
  }

  return (
    <div className="user-list-container">
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
          <option value="">Tous les statuts</option>
          <option value="PLANIFIEE">Planifiée</option>
          <option value="EN_COURS">En cours</option>
          <option value="TERMINEE">Terminée</option>
          <option value="SUSPENDUE">Suspendue</option>
        </select>
      </div>

      {filteredMissions.length === 0 ? (
        <div style={{ padding: '40px', textAlign: 'center' }}>
          <p>🚫 Aucune mission trouvée</p>
          <Link to="/chef/missions/new" className="btn-primary">Créer une mission</Link>
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
              {filteredMissions.map(m => {
                const statut = getStatutLabel(m.statut);
                return (
                  <tr key={m.id}>
                    <td>{m.id}</td>
                    <td><strong>{m.description || '-'}</strong></td>
                    <td>{m.destination || '-'}</td>
                    <td>{m.dateDebut || '-'}</td>
                    <td>{m.chauffeurNom || 'En attente'}</td>
                    <td>{m.vehiculeMatricule || 'En attente'}</td>
                    <td>
                      <span className={`status-badge ${statut.color}`}>
                        {statut.text}
                      </span>
                    </td>
                    <td className="actions">
                      <Link to={`/chef/missions/edit/${m.id}`} className="btn-edit">
                        ✏️ Modifier
                      </Link>
                      <button 
                        onClick={() => handleDownloadPdf(m.id, m.description)}
                        className="btn-download btn-download-document"
                        title="Télécharger la feuille de route en PDF"
                        disabled={!m.feuilleDeRoute}
                      >
                        📄 PDF
                      </button>
                      <button 
                        onClick={() => handleDelete(m.id, m.description)}
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