import React, { useState, useEffect } from 'react';

function ChefDeclarationsList({ user }) {
  const [declarations, setDeclarations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [type, setType] = useState('all');

  useEffect(() => {
    setTimeout(() => {
      setDeclarations([
        { id: 1, type: 'amende', chauffeur: 'Karim Ben Salah', montant: 45, date: '2026-04-05', statut: 'en_attente' },
        { id: 2, type: 'accident', chauffeur: 'Mohamed Ali', description: 'Accident mineur', date: '2026-04-03', statut: 'traite' }
      ]);
      setLoading(false);
    }, 500);
  }, []);

  const filtered = declarations.filter(d => type === 'all' || d.type === type);

  if (loading) return <div className="loading">Chargement...</div>;

  return (
    <div className="user-list-container">
      <div className="page-header"><h2>📄 Déclarations</h2></div>
      <div className="filters-bar"><div className="filter-select"><select value={type} onChange={(e) => setType(e.target.value)}><option value="all">Toutes</option><option value="amende">Amendes</option><option value="accident">Accidents</option></select></div></div>
      <div className="table-container">
        <table className="data-table">
          <thead><tr><th>ID</th><th>Type</th><th>Chauffeur</th><th>Détails</th><th>Date</th><th>Statut</th></tr></thead>
          <tbody>{filtered.map(d => (<tr key={d.id}><td>{d.id}</td><td>{d.type === 'amende' ? '💰 Amende' : '💥 Accident'}</td><td>{d.chauffeur}</td><td>{d.type === 'amende' ? `${d.montant} DT` : d.description}</td><td>{d.date}</td><td><span className={`status-badge ${d.statut === 'traite' ? 'success' : 'warning'}`}>{d.statut === 'traite' ? 'Traité' : 'En attente'}</span></td></tr>))}</tbody>
        </table>
      </div>
    </div>
  );
}

export default ChefDeclarationsList;