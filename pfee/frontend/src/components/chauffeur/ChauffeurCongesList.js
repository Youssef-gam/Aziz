import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import { getMesDemandes } from '../../services/conge';
import '../styles/list.css';

function ChauffeurCongesList() {
  const navigate = useNavigate();
  const { user } = useAuth();
  const { showSuccess, showError } = useNotification();
  const [conges, setConges] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filterStatut, setFilterStatut] = useState('');

  useEffect(() => {
    loadConges();
  }, []);

  const loadConges = async () => {
    setLoading(true);
    try {
      const response = await getMesDemandes();
      console.log('✅ Congés reçus:', response.data);
      setConges(response.data || []);
    } catch (err) {
      console.error('❌ Erreur chargement:', err);
      showError('❌ Erreur lors du chargement');
    } finally {
      setLoading(false);
    }
  };

  const getStatutLabel = (statut) => {
    const labels = {
      'EN_ATTENTE': { text: '⏳ En attente', class: 'badge-warning' },
      'APPROUVE': { text: '✅ Approuvé', class: 'badge-success' },
      'REFUSE': { text: '❌ Refusé', class: 'badge-danger' }
    };
    return labels[statut] || { text: '❓ ' + statut, class: 'badge-default' };
  };

  const getTypeLabel = (type) => {
    const labels = {
      'CONGÉ_ANNUEL': 'Congé annuel',
      'MALADIE': 'Congé maladie',
      'MATERNITÉ': 'Congé maternité',
      'PATERNITÉ': 'Congé paternité',
      'DEUIL': 'Congé de deuil',
      'SANS_SOLDE': 'Congé sans solde'
    };
    return labels[type] || type;
  };

  const calculateJours = (debut, fin) => {
    const d1 = new Date(debut);
    const d2 = new Date(fin);
    return Math.ceil((d2 - d1) / (1000 * 60 * 60 * 24)) + 1;
  };

  const filteredConges = filterStatut
    ? conges.filter(c => c.statut === filterStatut)
    : conges;

  if (loading) {
    return <div className="loading">Chargement des congés...</div>;
  }

  return (
    <div className="list-container">
      <div className="page-header">
        <h2>📅 Mes Demandes de Congé</h2>
        <button 
          onClick={() => navigate('/chauffeur/conge')} 
          className="btn-primary"
        >
          + Nouvelle demande
        </button>
      </div>

      <div className="filters-bar">
        <select 
          value={filterStatut} 
          onChange={(e) => setFilterStatut(e.target.value)}
          className="filter-select"
        >
          <option value="">📊 Tous les statuts</option>
          <option value="EN_ATTENTE">⏳ En attente</option>
          <option value="APPROUVE">✅ Approuvé</option>
          <option value="REFUSE">❌ Refusé</option>
        </select>
      </div>

      {filteredConges.length === 0 ? (
        <div className="empty-state">
          <p>🚫 Aucune demande de congé trouvée</p>
          <button 
            onClick={() => navigate('/chauffeur/conge')} 
            className="btn-primary"
          >
            Créer la première demande
          </button>
        </div>
      ) : (
        <div className="cards-container">
          {filteredConges.map(conge => {
            const statut = getStatutLabel(conge.statut);
            const jours = calculateJours(conge.dateDebut, conge.dateFin);
            return (
              <div key={conge.id} className="conge-card">
                <div className="card-header">
                  <div className="card-title-section">
                    <h3>{getTypeLabel(conge.type)}</h3>
                    <span className={`status-badge ${statut.class}`}>
                      {statut.text}
                    </span>
                  </div>
                </div>

                <div className="card-content">
                  <div className="info-row">
                    <label>📅 Période:</label>
                    <span>{conge.dateDebut} à {conge.dateFin}</span>
                  </div>

                  <div className="info-row">
                    <label>📊 Durée:</label>
                    <span>{jours} jour(s)</span>
                  </div>

                  <div className="info-row">
                    <label>📝 Motif:</label>
                    <p className="motif-text">{conge.motif}</p>
                  </div>

                  {conge.reponseMessage && (
                    <div className="info-row rejection-reason">
                      <label>💬 Note du chef:</label>
                      <p className="rejection-text">{conge.reponseMessage}</p>
                    </div>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}

      <div className="stats-footer">
        Total: <strong>{filteredConges.length}</strong> demande(s)
        {`  |  En attente: ${conges.filter(c => c.statut === 'EN_ATTENTE').length}`}
        {`  |  Approuvées: ${conges.filter(c => c.statut === 'APPROUVE').length}`}
        {`  |  Refusées: ${conges.filter(c => c.statut === 'REFUSE').length}`}
      </div>
    </div>
  );
}

export default ChauffeurCongesList;
