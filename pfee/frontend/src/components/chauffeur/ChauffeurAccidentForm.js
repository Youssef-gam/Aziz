import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import { createAccident } from '../../services/accident';
import { getMissions } from '../../services/mission';
import '../styles/form.css';

function ChauffeurAccidentForm() {
  const navigate = useNavigate();
  const { user } = useAuth();
  const { showSuccess, showError } = useNotification();
  const [loading, setLoading] = useState(false);
  const [missions, setMissions] = useState([]);
  
  const [formData, setFormData] = useState({
    missionId: '',
    date: new Date().toISOString().split('T')[0],
    lieu: '',
    description: '',
    temoins: '',
    degats: ''
  });

  React.useEffect(() => {
    loadMissions();
  }, []);

  const loadMissions = async () => {
    try {
      const res = await getMissions();
      const mesMissions = res.data.filter(m => m.chauffeurId === user.id);
      setMissions(mesMissions);
    } catch (err) {
      console.error('❌ Erreur chargement missions:', err);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.date || !formData.lieu || !formData.description) {
      showError('❌ Veuillez remplir tous les champs obligatoires');
      return;
    }

    setLoading(true);

    try {
      const accidentData = {
        dateAccident: formData.date,
        lieu: formData.lieu,
        description: formData.description,
        temoins: formData.temoins,
        degats: formData.degats,
        chauffeurId: user.id,
        missionId: formData.missionId ? parseInt(formData.missionId) : null,
        statut: 'DÉCLARÉ'
      };

      await createAccident(accidentData);
      showSuccess('✅ Accident déclaré avec succès');
      setTimeout(() => navigate('/chauffeur/dashboard'), 1500);
    } catch (err) {
      console.error('❌ Erreur:', err);
      showError('❌ Erreur: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-container">
      <div className="page-header">
        <h2>🚨 Déclarer un Accident</h2>
        <button className="btn-secondary" onClick={() => navigate('/chauffeur/dashboard')}>← Retour</button>
      </div>

      <form onSubmit={handleSubmit} className="user-form">
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="date">Date de l'accident *</label>
            <input
              type="date"
              id="date"
              name="date"
              value={formData.date}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="missionId">Mission concernée (facultatif)</label>
            <select
              id="missionId"
              name="missionId"
              value={formData.missionId}
              onChange={handleChange}
            >
              <option value="">-- Aucune mission --</option>
              {missions.map(m => (
                <option key={m.id} value={m.id}>
                  {m.description} ({m.destination})
                </option>
              ))}
            </select>
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="lieu">Lieu de l'accident *</label>
          <input
            type="text"
            id="lieu"
            name="lieu"
            value={formData.lieu}
            onChange={handleChange}
            placeholder="Ex: Rue de la Paix, Tunis"
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="description">Description de l'accident *</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows="4"
            placeholder="Décrivez les circonstances de l'accident..."
            required
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="temoins">Témoins</label>
            <input
              type="text"
              id="temoins"
              name="temoins"
              value={formData.temoins}
              onChange={handleChange}
              placeholder="Noms des témoins (facultatif)"
            />
          </div>
          <div className="form-group">
            <label htmlFor="degats">Description des dégâts</label>
            <input
              type="text"
              id="degats"
              name="degats"
              value={formData.degats}
              onChange={handleChange}
              placeholder="Ex: Rayures sur aile droite"
            />
          </div>
        </div>

        <div className="form-actions">
          <button
            type="submit"
            disabled={loading}
            className="btn-primary"
          >
            {loading ? 'Déclaration...' : 'Déclarer l\'accident'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/chauffeur/dashboard')}
            className="btn-secondary"
          >
            Annuler
          </button>
        </div>
      </form>
    </div>
  );
}

export default ChauffeurAccidentForm;
