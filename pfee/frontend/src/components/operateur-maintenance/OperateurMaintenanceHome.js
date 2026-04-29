import React, { useState, useEffect } from 'react';
import { getMaintenances } from '../../services/maintenance';
import { useNotification } from '../../context/NotificationContext';
import '../../styles/chef.css';

function OperateurMaintenanceHome({ user }) {
  const { showError } = useNotification();
  const [stats, setStats] = useState({
    totalMaintenances: 0,
    planifiees: 0,
    enCours: 0,
    terminees: 0,
    delai: 0
  });
  const [maintenances, setMaintenances] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const response = await getMaintenances();
      const data = response.data || [];

      // Calculer les statistiques
      const totalMaintenances = data.length;
      const planifiees = data.filter(m => m.statut === 'PLANIFIEE').length;
      const enCours = data.filter(m => m.statut === 'EN_COURS').length;
      const terminees = data.filter(m => m.statut === 'TERMINEE').length;
      const delai = data.filter(m => {
        const datePrevue = new Date(m.datePrevue);
        const aujourd = new Date();
        return datePrevue < aujourd && m.statut !== 'TERMINEE';
      }).length;

      setStats({
        totalMaintenances,
        planifiees,
        enCours,
        terminees,
        delai
      });

      // Prendre les 5 premières maintenances
      setMaintenances(data.slice(0, 5));
    } catch (err) {
      console.error('Erreur chargement données:', err);
      showError('Erreur lors du chargement des données');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Chargement...</div>;

  return (
    <div className="chef-dashboard-container">
      
      {/* Statistiques */}
      <div className="stats-grid">
        <div className="stats-card">
          <div className="stats-icon">🔧</div>
          <div className="stats-content">
            <h3>Total Maintenances</h3>
            <div className="stats-value">{stats.totalMaintenances}</div>
            <p className="stats-label">À gérer</p>
          </div>
        </div>

        <div className="stats-card">
          <div className="stats-icon">📅</div>
          <div className="stats-content">
            <h3>Planifiées</h3>
            <div className="stats-value">{stats.planifiees}</div>
            <p className="stats-label">À commencer</p>
          </div>
        </div>

        <div className="stats-card">
          <div className="stats-icon">⚙️</div>
          <div className="stats-content">
            <h3>En Cours</h3>
            <div className="stats-value">{stats.enCours}</div>
            <p className="stats-label">En cours de traitement</p>
          </div>
        </div>

        <div className="stats-card">
          <div className="stats-icon">✅</div>
          <div className="stats-content">
            <h3>Terminées</h3>
            <div className="stats-value">{stats.terminees}</div>
            <p className="stats-label">Complétées</p>
          </div>
        </div>

        <div className="stats-card warning">
          <div className="stats-icon">⚠️</div>
          <div className="stats-content">
            <h3>En Retard</h3>
            <div className="stats-value">{stats.delai}</div>
            <p className="stats-label">À traiter en priorité</p>
          </div>
        </div>
      </div>

      {/* Maintenances Récentes */}
      <div className="section">
        <h2>Maintenances Récentes</h2>
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Véhicule</th>
                <th>Type</th>
                <th>Date Prévue</th>
                <th>Statut</th>
                <th>Priorité</th>
              </tr>
            </thead>
            <tbody>
              {maintenances.length > 0 ? (
                maintenances.map((m) => (
                  <tr key={m.id}>
                    <td><strong>{m.vehicule?.immatriculation || 'N/A'}</strong></td>
                    <td>{m.type}</td>
                    <td>{new Date(m.datePrevue).toLocaleDateString('fr-FR')}</td>
                    <td>
                      <span className={`badge badge-${m.statut.toLowerCase()}`}>
                        {m.statut === 'PLANIFIEE' && '📅 Planifiée'}
                        {m.statut === 'EN_COURS' && '⚙️ En cours'}
                        {m.statut === 'TERMINEE' && '✅ Terminée'}
                      </span>
                    </td>
                    <td>
                      {m.cout > 1000 && '🔴 Haute'}
                      {m.cout > 500 && m.cout <= 1000 && '🟠 Moyenne'}
                      {m.cout <= 500 && '🟢 Basse'}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="text-center">Aucune maintenance</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Alertes */}
      {stats.delai > 0 && (
        <div className="alert alert-warning">
          <div className="alert-icon">⚠️</div>
          <div className="alert-content">
            <h3>Maintenances en Retard</h3>
            <p>{stats.delai} maintenance(s) dépassent la date prévue. Veuillez les traiter en priorité.</p>
          </div>
        </div>
      )}

      {stats.enCours > 0 && (
        <div className="alert alert-info">
          <div className="alert-icon">ℹ️</div>
          <div className="alert-content">
            <h3>Maintenances en Cours</h3>
            <p>Vous avez {stats.enCours} maintenance(s) actuellement en cours de traitement.</p>
          </div>
        </div>
      )}
    </div>
  );
}

export default OperateurMaintenanceHome;
