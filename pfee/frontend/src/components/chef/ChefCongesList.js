import React, { useState, useEffect, useCallback } from 'react';
import { useNotification } from '../../context/NotificationContext';
import { getDemandesEnAttente, repondreDemande, getHistoriqueConges } from '../../services/conge';
import '../../styles/chef.css';

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

function ChefCongesList({ user }) {
  const { showSuccess, showError } = useNotification();
  const [conges, setConges] = useState([]);
  const [historique, setHistorique] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('demandes');
  const [responseData, setResponseData] = useState({});

  const loadConges = useCallback(async () => {
    try {
      setLoading(true);
      console.log('📥 Chargement des demandes de congés...');
      
      const demandesResponse = await getDemandesEnAttente();
      console.log('✅ Demandes chargées:', demandesResponse.data);
      setConges(demandesResponse.data);
      
      const historiqueResponse = await getHistoriqueConges();
      console.log('✅ Historique chargé:', historiqueResponse.data);
      setHistorique(historiqueResponse.data);
      
      setError(null);
    } catch (err) {
      console.error('❌ Erreur complète:', err);
      console.error('Status:', err.response?.status);
      console.error('Data:', err.response?.data);
      
      let errorMessage = 'Erreur lors du chargement des demandes de congés';
      
      if (err.response?.status === 401) {
        errorMessage = 'Session expirée. Veuillez vous reconnecter.';
      } else if (err.response?.status === 403) {
        errorMessage = 'Accès refusé. Vous n\'avez pas les permissions nécessaires.';
      } else if (err.response?.status === 404) {
        errorMessage = 'Endpoint non trouvé. Vérifiez la configuration du serveur.';
      }
      
      setError(errorMessage);
      showError(errorMessage);
    } finally {
      setLoading(false);
    }
  }, [showError]);

  useEffect(() => {
    loadConges();
  }, [loadConges]);

  const handleRepondre = async (id, statut) => {
    const message = responseData[id] || '';
    if (!window.confirm(`Êtes-vous sûr de vouloir ${statut === 'APPROUVE' ? 'approuver' : 'refuser'} cette demande ?`)) {
      return;
    }
    
    try {
      await repondreDemande(id, statut, message);
      showSuccess(`✅ Demande ${statut === 'APPROUVE' ? 'approuvée' : 'refusée'} avec succès`);
      setResponseData({ ...responseData, [id]: '' });
      loadConges(); // Rafraîchir les deux listes (demandes et historique)
    } catch (err) {
      console.error('Erreur réponse demande', err);
      showError('❌ Erreur lors du traitement de la demande');
    }
  };

  const getStatutLabel = (statut) => {
    const labels = {
      'EN_ATTENTE': { text: '⏳ En attente', class: 'warning' },
      'APPROUVE': { text: '✅ Approuvé', class: 'success' },
      'REFUSE': { text: '❌ Refusé', class: 'danger' }
    };
    return labels[statut] || { text: 'Inconnu', class: 'inactive' };
  };

  if (loading) return <div className="loading">Chargement...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="user-list-container">
      <div className="page-header">
        <h2>📅 Gestion des congés</h2>
      </div>

      <div className="tabs">
        <button 
          className={`tab-button ${activeTab === 'demandes' ? 'active' : ''}`}
          onClick={() => setActiveTab('demandes')}
        >
          Demandes en attente ({conges.length})
        </button>
        <button 
          className={`tab-button ${activeTab === 'historique' ? 'active' : ''}`}
          onClick={() => setActiveTab('historique')}
        >
          Historique
        </button>
      </div>

      {activeTab === 'demandes' && (
        <div className="table-container">
          {conges.length === 0 ? (
            <div className="empty-state">✓ Aucune demande de congé en attente</div>
          ) : (
            <div className="conges-cards-list">
              {conges.map(c => (
                <div key={c.id} className="conge-request-card">
                  <div className="card-header">
                    <div className="chauffeur-info">
                      <h3>{c.chauffeurNom}</h3>
                      <span className="request-id">ID: {c.id}</span>
                    </div>
                    <span className="request-date">Demandé le {formatDate(c.dateDemande)}</span>
                  </div>

                  <div className="card-body">
                    <div className="date-info">
                      <div className="date-block">
                        <span className="label">📅 Début</span>
                        <span className="value">{formatDate(c.dateDebut)}</span>
                      </div>
                      <div className="date-block">
                        <span className="label">📅 Fin</span>
                        <span className="value">{formatDate(c.dateFin)}</span>
                      </div>
                      <div className="date-block">
                        <span className="label">⏱️ Durée</span>
                        <span className="value">
                          {Math.ceil((new Date(c.dateFin) - new Date(c.dateDebut)) / (1000 * 60 * 60 * 24)) + 1} jours
                        </span>
                      </div>
                    </div>

                    {c.motif && (
                      <div className="motif-section">
                        <strong>Motif:</strong>
                        <p>{c.motif}</p>
                      </div>
                    )}

                    <div className="message-section">
                      <label>Note/Commentaire (optionnel):</label>
                      <textarea
                        placeholder="Ajouter un message avant validation..."
                        value={responseData[c.id] || ''}
                        onChange={(e) => setResponseData({ ...responseData, [c.id]: e.target.value })}
                        className="response-message-card"
                        rows="3"
                      />
                    </div>
                  </div>

                  <div className="card-footer">
                    <button 
                      onClick={() => handleRepondre(c.id, 'APPROUVE')} 
                      className="btn-approve"
                    >
                      ✅ Approuver
                    </button>
                    <button 
                      onClick={() => handleRepondre(c.id, 'REFUSE')} 
                      className="btn-reject"
                    >
                      ❌ Refuser
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {activeTab === 'historique' && (
        <div className="table-container">
          {historique.length === 0 ? (
            <div className="empty-state">Aucun historique de congé</div>
          ) : (
            <table className="data-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Chauffeur</th>
                  <th>Date début</th>
                  <th>Date fin</th>
                  <th>Motif</th>
                  <th>Statut</th>
                  <th>Réponse</th>
                </tr>
              </thead>
              <tbody>
                {historique.map(c => {
                  const s = getStatutLabel(c.statut);
                  return (
                    <tr key={c.id}>
                      <td>{c.id}</td>
                      <td><strong>{c.chauffeurNom}</strong></td>
                      <td>{formatDate(c.dateDebut)}</td>
                      <td>{formatDate(c.dateFin)}</td>
                      <td>{c.motif || '-'}</td>
                      <td><span className={`status-badge ${s.class}`}>{s.text}</span></td>
                      <td>{c.reponseMessage || '-'}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  );
}

export default ChefCongesList;
