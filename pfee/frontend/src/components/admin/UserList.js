import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getUtilisateurs, deleteUtilisateur, deleteChauffeur } from '../../services/utilisateur';

function UserList() {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [roleFilter, setRoleFilter] = useState('');

  useEffect(() => { fetchUsers(); }, []);

  const fetchUsers = async () => {
    try {
      const response = await getUtilisateurs();
      setUsers(response.data);
    } catch (err) { setError('Erreur de chargement'); }
    finally { setLoading(false); }
  };

  const handleDelete = async (id, email, role) => {
    if (window.confirm(`Supprimer ${email} ?`)) {
      try {
        // Pour les chauffeurs: utiliser l'endpoint /chauffeurs/{id}
        if (role === 'CHAUFFEUR') {
          await deleteChauffeur(id);
        } else {
          // Pour les autres rôles: utiliser l'endpoint /utilisateurs/{id}
          await deleteUtilisateur(id);
        }
        setUsers(users.filter(u => u.id !== id));
      } catch (err) {
        console.error('Erreur suppression:', err);
        alert('❌ Erreur suppression: ' + (err.response?.data?.message || err.message));
      }
    }
  };

  const filteredUsers = users.filter(u =>
    (u.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
     u.nom?.toLowerCase().includes(searchTerm.toLowerCase()) ||
     u.prenom?.toLowerCase().includes(searchTerm.toLowerCase())) &&
    (roleFilter === '' || u.role === roleFilter)
  );

  const getRoleLabel = (role) => {
    const roles = { ADMIN: 'Admin', CHEF: 'Chef de parc', CHAUFFEUR: 'Chauffeur', OPERATEUR_MAINTENANCE: 'Opérateur' };
    return roles[role] || role;
  };

  const getRoleBadge = (role) => {
    const classes = { ADMIN: 'badge-admin', CHEF: 'badge-chef', CHAUFFEUR: 'badge-chauffeur', OPERATEUR_MAINTENANCE: 'badge-operateur' };
    return classes[role] || 'badge-default';
  };

  if (loading) return <div className="loading">Chargement...</div>;
  if (error) return <div className="error-message">{error}</div>;

  return (
    <div className="user-list-container">
      <div className="page-header">
        <h2>Utilisateurs</h2>
        <button onClick={() => navigate('/admin/users/new')} className="btn-primary">+ Ajouter</button>
      </div>
      <div className="filters-bar">
        <input type="text" placeholder="Rechercher..." value={searchTerm} onChange={e => setSearchTerm(e.target.value)} className="search-box" />
        <select value={roleFilter} onChange={e => setRoleFilter(e.target.value)} className="filter-select">
          <option value="">Tous les rôles</option>
          <option value="CHEF">Chef de parc</option>
          <option value="CHAUFFEUR">Chauffeur</option>
          <option value="OPERATEUR_MAINTENANCE">Opérateur</option>
        </select>
      </div>
      <table className="data-table">
        <thead><tr><th>ID</th><th>Nom</th><th>Prénom</th><th>Email</th><th>Rôle</th><th>Statut</th><th>Parc</th><th>Actions</th></tr></thead>
        <tbody>
          {filteredUsers.map(u => (
            <tr key={u.id}>
              <td>{u.id}</td><td>{u.nom || '-'}</td><td>{u.prenom || '-'}</td><td>{u.email}</td>
              <td><span className={`role-badge ${getRoleBadge(u.role)}`}>{getRoleLabel(u.role)}</span></td>
              <td><span className={`status-badge ${u.actif ? 'active' : 'inactive'}`}>{u.actif ? 'Actif' : 'Inactif'}</span></td>
              <td>{u.parcNom || '-'}</td>
              <td className="actions">
                <Link to={`/admin/users/edit/${u.id}`} className="btn-edit">Modifier</Link>
                <button onClick={() => handleDelete(u.id, u.email, u.role)} className="btn-delete">Supprimer</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="stats-footer">Total : {filteredUsers.length}</div>
    </div>
  );
}

export default UserList;