import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import { createDemandeConge } from '../../services/conge';
import '../styles/form.css';

function ChauffeurCongeForm() {
  const navigate = useNavigate();
  const { user } = useAuth();
  const { showSuccess, showError } = useNotification();
  const [loading, setLoading] = useState(false);
  
  const [formData, setFormData] = useState({
    dateDebut: '',
    dateFin: '',
    motif: '',
    type: 'CONGÉ_ANNUEL'
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.dateDebut || !formData.dateFin || !formData.motif) {
      showError('❌ Veuillez remplir tous les champs obligatoires');
      return;
    }

    if (new Date(formData.dateFin) < new Date(formData.dateDebut)) {
      showError('❌ La date de fin ne peut pas être antérieure à la date de début');
      return;
    }

    setLoading(true);

    try {
      const congeData = {
        dateDebut: formData.dateDebut,
        dateFin: formData.dateFin,
        motif: formData.motif,
        type: formData.type,
        chauffeurId: user.id,
        statut: 'EN_ATTENTE'
      };

      await createDemandeConge(congeData);
      showSuccess('✅ Demande de congé envoyée avec succès');
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
        <h2>📋 Demander un Congé</h2>
        <button className="btn-secondary" onClick={() => navigate('/chauffeur/dashboard')}>← Retour</button>
      </div>

      <form onSubmit={handleSubmit} className="user-form">
        <div className="form-group">
          <label htmlFor="type">Type de congé *</label>
          <select
            id="type"
            name="type"
            value={formData.type}
            onChange={handleChange}
            required
          >
            <option value="CONGÉ_ANNUEL">Congé annuel</option>
            <option value="MALADIE">Congé maladie</option>
            <option value="MATERNITÉ">Congé maternité</option>
            <option value="PATERNITÉ">Congé paternité</option>
            <option value="DEUIL">Congé de deuil</option>
            <option value="SANS_SOLDE">Congé sans solde</option>
          </select>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="dateDebut">Date de début *</label>
            <input
              type="date"
              id="dateDebut"
              name="dateDebut"
              value={formData.dateDebut}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="dateFin">Date de fin *</label>
            <input
              type="date"
              id="dateFin"
              name="dateFin"
              value={formData.dateFin}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="motif">Motif du congé *</label>
          <textarea
            id="motif"
            name="motif"
            value={formData.motif}
            onChange={handleChange}
            rows="4"
            placeholder="Expliquez le motif de votre demande de congé..."
            required
          />
        </div>

        <div className="form-actions">
          <button
            type="submit"
            disabled={loading}
            className="btn-primary"
          >
            {loading ? 'Envoi...' : 'Envoyer la demande'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/chauffeur/dashboard')}
            className="btn-secondary"
          >
            Annuler
          </button>
        </div>

        <div style={{ marginTop: '20px', padding: '10px', backgroundColor: '#f0f0f0', borderRadius: '5px' }}>
          <p style={{ fontSize: '12px', color: '#666' }}>
            💡 Votre demande sera transmise au chef de parc pour approbation ou refus.
            Vous recevrez une notification de réponse.
          </p>
        </div>
      </form>
    </div>
  );
}

export default ChauffeurCongeForm;
