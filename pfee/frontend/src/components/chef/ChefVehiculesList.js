import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getVehicules, deleteVehicule } from '../../services/vehicule';
import { useAuth } from '../../context/AuthContext';

function ChefVehiculesList() {
  const { user } = useAuth();
  const [vehicules, setVehicules] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatut, setFilterStatut] = useState('');

  useEffect(() => {
    loadVehicules();
  }, []);

  const loadVehicules = async () => {
    try {
      const response = await getVehicules();
      let list = response.data;
      // Filtrer par parc du chef
      if (user?.parcId) {
        list = list.filter(v => v.parcId === user.parcId);
      }
      setVehicules(list);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id, matricule) => {
    if (window.confirm(`Supprimer le véhicule ${matricule} ?`)) {
      try {
        await deleteVehicule(id);
        setVehicules(vehicules.filter(v => v.id !== id));
      } catch (err) {
        alert('Erreur lors de la suppression');
      }
    }
  };

  const getStatutLabel = (statut) => {
    const labels = {
      'disponible': { text: 'Disponible', class: 'active' },
      'occupe': { text: 'En mission', class: 'warning' },
      'maintenance': { text: 'En maintenance', class: 'inactive' }
    };
    return labels[statut] || { text: statut || 'Inconnu', class: 'inactive' };
  };

  const filtered = vehicules.filter(v => {
    const matchSearch = v.matricule?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                        v.marque?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                        v.modele?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchStatut = filterStatut === '' || v.statut === filterStatut;
    return matchSearch && matchStatut;
  });

  if (loading) return <div className="loading">Chargement...</div>;

  return (
    <div className="user-list-container">
      <div className="page-header">
        <h2>🚗 Gestion des véhicules</h2>
        <Link to="/chef/vehicules/new" className="btn-primary">+ Ajouter un véhicule</Link>
      </div>

      <div className="filters-bar">
        <div className="search-box">
          <input type="text" placeholder="Rechercher par matricule, marque..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} />
        </div>
        <div className="filter-select">
          <select value={filterStatut} onChange={(e) => setFilterStatut(e.target.value)}>
            <option value="">Tous les statuts</option>
            <option value="disponible">Disponible</option>
            <option value="occupe">En mission</option>
            <option value="maintenance">En maintenance</option>
          </select>
        </div>
      </div>

      <div className="table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>Matricule</th><th>Marque</th><th>Modèle</th><th>Carburant</th><th>Kilométrage</th><th>Statut</th><th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map(v => {
              const s = getStatutLabel(v.statut);
              return (
                <tr key={v.id}>
                  <td><strong>{v.matricule}</strong></td>
                  <td>{v.marque}</td>
                  <td>{v.modele}</td>
                  <td>{v.typeCarburant || '-'}</td>
                  <td>{v.kilometrage?.toLocaleString()} km</td>
                  <td><span className={`status-badge ${s.class}`}>{s.text}</span></td>
                  <td className="actions">
                    <Link to={`/chef/vehicules/edit/${v.id}`} className="btn-edit">✏️ Modifier</Link>
                    <button onClick={() => handleDelete(v.id, v.matricule)} className="btn-delete">🗑️ Supprimer</button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
      <div className="stats-footer"><p>Total : {filtered.length} véhicule(s)</p></div>
    </div>
  );
}

export default ChefVehiculesList;