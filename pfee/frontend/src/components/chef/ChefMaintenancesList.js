import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import { useNotification } from '../../context/NotificationContext';
import { getMaintenances, deleteMaintenance } from '../../services/maintenance';
import '../../styles/chef.css';

function ChefMaintenancesList({ user }) {
  const { showSuccess, showError } = useNotification();
  const [maintenances, setMaintenances] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filterStatut, setFilterStatut] = useState('');
  const [filterType, setFilterType] = useState('');

  const loadMaintenances = useCallback(async () => {
    try {
      setLoading(true);
      const response = await getMaintenances();
      setMaintenances(response.data);
    } catch (err) {
      console.error('Erreur chargement maintenances', err);
      setError('Erreur lors du chargement des maintenances');
      showError('Erreur lors du chargement des maintenances');
    } finally {
      setLoading(false);
    }
  }, [showError]);

  useEffect(() => {
    loadMaintenances();
  }, [loadMaintenances]);

  const handleDelete = async (id) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cette maintenance ?')) {
      try {
        await deleteMaintenance(id);
        setMaintenances(maintenances.filter(m => m.id !== id));
        showSuccess('✅ Maintenance supprimée avec succès');
      } catch (err) {
        console.error('Erreur suppression maintenance', err);
        showError('❌ Erreur lors de la suppression');
      }
    }
  };

  const getStatutLabel = (statut) => {
    const labels = {
      'PLANIFIEE': { text: '📅 Planifiée', class: 'warning' },
      'EN_COURS': { text: '🔧 En cours', class: 'active' },
      'TERMINEE': { text: '✅ Terminée', class: 'success' },
      'ANNULEE': { text: '❌ Annulée', class: 'danger' }
    };
    return labels[statut] || { text: 'Inconnu', class: 'inactive' };
  };

  const getTypeLabel = (type) => {
    const labels = {
      'REVISION': 'Révision',
      'REPARATION': 'Réparation',
      'INSPECTION': 'Inspection',
      'ENTRETIEN': 'Entretien',
      'PNEUMATIQUES': 'Pneumatiques',
      'VIDANGE': 'Vidange',
      'FREINS': 'Freins',
      'MAINTENANCE_PREVENTIVE': 'Maintenance préventive'
    };
    return labels[type] || type;
  };

  const filtered = maintenances.filter(m => 
    (filterStatut === '' || m.statut === filterStatut) &&
    (filterType === '' || m.type === filterType)
  );

  if (loading) return <div className="loading">Chargement...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="user-list-container">
      <div className="page-header">
        <h2>🔧 Gestion des maintenances</h2>
        <Link to="/chef/maintenances/new" className="btn-primary">+ Programmer une maintenance</Link>
      </div>

      <div className="filters-bar">
        <div className="filter-select">
          <label>Type :</label>
          <select value={filterType} onChange={(e) => setFilterType(e.target.value)}>
            <option value="">Tous les types</option>
            <option value="REVISION">Révision</option>
            <option value="REPARATION">Réparation</option>
            <option value="INSPECTION">Inspection</option>
            <option value="ENTRETIEN">Entretien</option>
            <option value="PNEUMATIQUES">Pneumatiques</option>
            <option value="VIDANGE">Vidange</option>
            <option value="FREINS">Freins</option>
            <option value="MAINTENANCE_PREVENTIVE">Maintenance préventive</option>
          </select>
        </div>
        <div className="filter-select">
          <label>Statut :</label>
          <select value={filterStatut} onChange={(e) => setFilterStatut(e.target.value)}>
            <option value="">Tous les statuts</option>
            <option value="PLANIFIEE">Planifiée</option>
            <option value="EN_COURS">En cours</option>
            <option value="TERMINEE">Terminée</option>
            <option value="ANNULEE">Annulée</option>
          </select>
        </div>
      </div>

      <div className="table-container">
        {filtered.length === 0 ? (
          <div className="empty-state">Aucune maintenance trouvée</div>
        ) : (
          <table className="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Véhicule</th>
                <th>Garage</th>
                <th>Date prévue</th>
                <th>Date réalisation</th>
                <th>Statut</th>
                <th>Coût</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(m => {
                const s = getStatutLabel(m.statut);
                return (
                  <tr key={m.id}>
                    <td>{m.id}</td>
                    <td><strong>{getTypeLabel(m.type)}</strong></td>
                    <td>{m.vehiculeMatricule}</td>
                    <td>{m.garageNom || '-'}</td>
                    <td>{m.datePrevue}</td>
                    <td>{m.dateRealisation || '-'}</td>
                    <td><span className={`status-badge ${s.class}`}>{s.text}</span></td>
                    <td>{m.cout ? `${m.cout} €` : '-'}</td>
                    <td className="actions">
                      <Link to={`/chef/maintenances/edit/${m.id}`} className="btn-edit">✏️ Modifier</Link>
                      <button onClick={() => handleDelete(m.id)} className="btn-delete">🗑️ Supprimer</button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default ChefMaintenancesList;
