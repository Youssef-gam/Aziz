import React, { useState, useEffect } from 'react';
import { getUtilisateurs } from '../../services/utilisateur';
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from 'recharts';

const COLORS = ['#4CAF50', '#2196F3', '#FF9800', '#F44336'];

function Dashboard() {
  const [stats, setStats] = useState({
    total: 0,
    actifs: 0,
    inactifs: 0,
    chauffeurs: 0,
    chefs: 0,
    operateurs: 0,
    admins: 0,
    derniers: [],
    repartition: [],
    tauxActivite: 0
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const response = await getUtilisateurs();
      const users = response.data;
      const total = users.length;
      const actifs = users.filter(u => u.actif).length;
      const inactifs = total - actifs;
      const chauffeurs = users.filter(u => u.role === 'CHAUFFEUR').length;
      const chefs = users.filter(u => u.role === 'CHEF').length;
      const operateurs = users.filter(u => u.role === 'OPERATEUR_MAINTENANCE').length;
      const admins = users.filter(u => u.role === 'ADMIN').length;
      const derniers = [...users].sort((a, b) => b.id - a.id).slice(0, 5);
      const repartition = [
        { name: 'Chauffeurs', value: chauffeurs },
        { name: 'Chefs de parc', value: chefs },
        { name: 'Opérateurs', value: operateurs },
        { name: 'Admins', value: admins }
      ];
      setStats({
        total, actifs, inactifs, chauffeurs, chefs, operateurs, admins, derniers, repartition,
        tauxActivite: total ? Math.round((actifs / total) * 100) : 0
      });
    } catch (err) {
      setError('Erreur lors du chargement des statistiques');
    } finally {
      setLoading(false);
    }
  };

  const getRoleLabel = (role) => {
    const roles = {
      'ADMIN': 'Admin',
      'CHEF': 'Chef de parc',
      'CHAUFFEUR': 'Chauffeur',
      'OPERATEUR_MAINTENANCE': 'Opérateur maint.'
    };
    return roles[role] || role;
  };

  if (loading) return <div className="loading">Chargement des statistiques...</div>;
  if (error) return <div className="error-message">{error}</div>;

  return (
    <div className="dashboard-container">
      <h2 className="dashboard-title">Tableau de bord</h2>
      <p className="dashboard-subtitle">Vue d'ensemble du système</p>
      <div className="stats-grid">
        <div className="stats-card">
          <div className="stats-icon">👥</div>
          <div className="stats-content">
            <h3>Total utilisateurs</h3>
            <div className="stats-value">{stats.total}</div>
            <p className="stats-label">comptes enregistrés</p>
          </div>
        </div>
        <div className="stats-card">
          <div className="stats-icon">🚗</div>
          <div className="stats-content">
            <h3>Chauffeurs</h3>
            <div className="stats-value">{stats.chauffeurs}</div>
            <p className="stats-label">rôle CHAUFFEUR</p>
          </div>
        </div>
        <div className="stats-card">
          <div className="stats-icon">👨‍💼</div>
          <div className="stats-content">
            <h3>Chefs de parc</h3>
            <div className="stats-value">{stats.chefs}</div>
            <p className="stats-label">rôle CHEF</p>
          </div>
        </div>
        <div className="stats-card">
          <div className="stats-icon">🔧</div>
          <div className="stats-content">
            <h3>Opérateurs maintenance</h3>
            <div className="stats-value">{stats.operateurs}</div>
            <p className="stats-label">rôle OPERATEUR</p>
          </div>
        </div>
      </div>
      <div className="dashboard-row">
        <div className="dashboard-col">
          <h3>Répartition par rôle</h3>
          <div className="pie-chart-container">
            <ResponsiveContainer width="100%" height={250}>
              <PieChart>
                <Pie
                  data={stats.repartition}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                  outerRadius={80}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {stats.repartition.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>
          <div className="legend">
            {stats.repartition.map((item, idx) => (
              <div key={idx} className="legend-item">
                <span className="legend-color" style={{ backgroundColor: COLORS[idx % COLORS.length] }}></span>
                <span>{item.name}</span>
                <span className="legend-value">{item.value}</span>
              </div>
            ))}
          </div>
        </div>
        <div className="dashboard-col">
          <h3>Derniers utilisateurs ajoutés</h3>
          <div className="recent-users">
            {stats.derniers.map(user => (
              <div key={user.id} className="recent-user-item">
                <div className="user-avatar">
                  {user.prenom ? user.prenom.charAt(0) : user.nom ? user.nom.charAt(0) : 'U'}
                </div>
                <div className="user-info">
                  <div className="user-name">{user.prenom} {user.nom}</div>
                  <div className="user-role">{getRoleLabel(user.role)}</div>
                </div>
                <div className={`user-status ${user.actif ? 'active' : 'inactive'}`}>
                  {user.actif ? 'Actif' : 'Inactif'}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
      <div className="dashboard-row">
        <div className="dashboard-col">
          <h3>Statut des comptes</h3>
          <div className="status-stats">
            <div className="status-item">
              <span className="status-label">Actifs</span>
              <span className="status-value active">{stats.actifs}</span>
            </div>
            <div className="status-item">
              <span className="status-label">Inactifs</span>
              <span className="status-value inactive">{stats.inactifs}</span>
            </div>
          </div>
        </div>
        <div className="dashboard-col">
          <h3>Résumé rapide</h3>
          <div className="summary-stats">
            <div className="summary-item">
              <span className="summary-label">Total comptes</span>
              <span className="summary-value">{stats.total}</span>
            </div>
            <div className="summary-item">
              <span className="summary-label">Taux d'activité</span>
              <span className="summary-value">{stats.tauxActivite}%</span>
            </div>
            <div className="summary-item">
              <span className="summary-label">Comptes inactifs</span>
              <span className="summary-value">{stats.inactifs}</span>
            </div>
            <div className="summary-item">
              <span className="summary-label">Rôle dominant</span>
              <span className="summary-value">
                {stats.chauffeurs >= stats.chefs && stats.chauffeurs >= stats.operateurs ? 'Chauffeur' : 
                 stats.chefs >= stats.operateurs ? 'Chef de parc' : 'Opérateur'}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;