import React, { useState, useEffect } from 'react';
import { getUtilisateurs } from '../../services/utilisateur';

function ChefChauffeursList({ user }) {
  const [chauffeurs, setChauffeurs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterDisponible, setFilterDisponible] = useState('');

  useEffect(() => { loadChauffeurs(); }, []);

  const loadChauffeurs = async () => {
    try {
      const response = await getUtilisateurs();
      let chauffeursList = response.data.filter(u => u.role === 'CHAUFFEUR');
      if (user?.parcId) chauffeursList = chauffeursList.filter(c => c.parcId === user.parcId);
      setChauffeurs(chauffeursList);
    } catch (err) { console.error(err); } finally { setLoading(false); }
  };

  const getDisponibleLabel = (d) => {
    const labels = { 'disponible': { text: 'Disponible', class: 'active' }, 'occupe': { text: 'En mission', class: 'warning' }, 'congé': { text: 'En congé', class: 'inactive' } };
    return labels[d] || { text: 'Inconnu', class: 'inactive' };
  };

  const filtered = chauffeurs.filter(c => {
    const matchSearch = c.nom?.toLowerCase().includes(searchTerm.toLowerCase()) || c.prenom?.toLowerCase().includes(searchTerm.toLowerCase()) || c.email?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchDispo = filterDisponible === '' || c.disponible === filterDisponible;
    return matchSearch && matchDispo;
  });

  if (loading) return <div className="loading">Chargement...</div>;

  return (
    <div className="user-list-container">
      <div className="page-header"><h2>👨‍✈️ Consultation des chauffeurs</h2><p className="header-info">Liste des chauffeurs de votre parc (consultation uniquement)</p></div>
      <div className="filters-bar">
        <div className="search-box"><input type="text" placeholder="Rechercher..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} /></div>
        <div className="filter-select"><select value={filterDisponible} onChange={(e) => setFilterDisponible(e.target.value)}><option value="">Tous</option><option value="disponible">Disponible</option><option value="occupe">En mission</option><option value="congé">En congé</option></select></div>
      </div>
      <div className="table-container">
        <table className="data-table">
          <thead><tr><th>ID</th><th>Nom complet</th><th>Email</th><th>Téléphone</th><th>N° Permis</th><th>Statut</th><th>Véhicule</th></tr></thead>
          <tbody>{filtered.map(c => { const d = getDisponibleLabel(c.disponible); return (<tr key={c.id}><td>{c.id}</td><td><strong>{c.prenom} {c.nom}</strong></td><td>{c.email}</td><td>{c.telephone || '-'}</td><td>{c.numeroPermis || '-'}</td><td><span className={`status-badge ${d.class}`}>{d.text}</span></td><td>{c.vehiculeMatricule || 'Non affecté'}</td></tr>); })}</tbody>
        </table>
      </div>
      <div className="stats-footer"><p>Total : {filtered.length} chauffeur(s)</p></div>
      <div className="info-note"><p>ℹ️ L'ajout, modification et suppression sont réservés à l'administrateur.</p></div>
    </div>
  );
}

export default ChefChauffeursList;