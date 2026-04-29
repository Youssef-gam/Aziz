import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import { getChauffeurs } from '../../services/chauffeur';
import { getVehicules } from '../../services/vehicule';
import { createMission, updateMission, getMission } from '../../services/mission';
import '../styles/form.css';

function ChefMissionsForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const { showSuccess, showError } = useNotification();
  
  // État du formulaire
  const [formData, setFormData] = useState({
    titre: '',
    destination: '',
    date: '',
    heure: '',
    chauffeurId: '',
    vehiculeId: '',
    description: '',
    objetALivrer: '',
    nombreDeParticipants: 1
  });

  // Listes de données
  const [chauffeurs, setChauffeurs] = useState([]);
  const [vehicules, setVehicules] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);
  
  const isEditMode = !!id;

  // Charger les données au montage
  useEffect(() => {
    loadInitialData();
  }, []);

  const loadInitialData = async () => {
    setPageLoading(true);
    try {
      // Charger chauffeurs et véhicules en parallèle
      const [chauffeursRes, vehiculesRes] = await Promise.all([
        getChauffeurs(),
        getVehicules()
      ]);

      console.log('📋 Chauffeurs:', chauffeursRes.data?.length || 0);
      console.log('📋 Véhicules:', vehiculesRes.data?.length || 0);

      setChauffeurs(chauffeursRes.data || []);
      setVehicules(vehiculesRes.data || []);

      // Si en mode édition, charger la mission
      if (isEditMode) {
        const missionRes = await getMission(id);
        setFormData({
          titre: missionRes.data.description || '',
          destination: missionRes.data.destination || '',
          date: missionRes.data.dateDebut || '',
          heure: '09:00',
          chauffeurId: missionRes.data.chauffeurId?.toString() || '',
          vehiculeId: missionRes.data.vehiculeId?.toString() || '',
          description: '',
          objetALivrer: missionRes.data.objetALivrer || '',
          nombreDeParticipants: missionRes.data.nombreDeParticipants || 1
        });
      }
    } catch (err) {
      console.error('❌ Erreur chargement:', err);
      showError('❌ Erreur lors du chargement des données');
    } finally {
      setPageLoading(false);
    }
  };

  // Gérer les changements du formulaire
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // Soumettre le formulaire
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validation
    if (!formData.titre || !formData.destination || !formData.date || !formData.chauffeurId || !formData.vehiculeId) {
      showError('❌ Tous les champs obligatoires doivent être remplis');
      return;
    }

    setLoading(true);

    try {
      const missionData = {
        description: formData.titre,
        destination: formData.destination,
        dateDebut: formData.date,
        dateFin: formData.date,
        chauffeurId: parseInt(formData.chauffeurId),
        vehiculeId: parseInt(formData.vehiculeId),
        objetALivrer: formData.objetALivrer,
        nombreDeParticipants: parseInt(formData.nombreDeParticipants) || 1,
        statut: 'PLANIFIEE'
      };

      if (isEditMode) {
        await updateMission(id, missionData);
        showSuccess('✅ Mission modifiée avec succès');
      } else {
        await createMission(missionData);
        showSuccess('✅ Mission créée avec succès');
      }

      setTimeout(() => navigate('/chef/missions'), 1500);
    } catch (err) {
      console.error('❌ Erreur création mission:', err);
      showError('❌ Erreur: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  if (pageLoading) {
    return <div className="loading">Chargement...</div>;
  }

  return (
    <div className="form-container">
      <div className="page-header">
        <h2>{isEditMode ? 'Modifier' : 'Créer'} une mission</h2>
        <button className="btn-secondary" onClick={() => navigate('/chef/missions')}>← Retour</button>
      </div>

      <form onSubmit={handleSubmit} className="user-form">
        {/* Titre et Destination */}
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="titre">Titre *</label>
            <input
              type="text"
              id="titre"
              name="titre"
              value={formData.titre}
              onChange={handleChange}
              placeholder="Ex: Transport de marchandises"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="destination">Destination *</label>
            <input
              type="text"
              id="destination"
              name="destination"
              value={formData.destination}
              onChange={handleChange}
              placeholder="Ex: Sfax"
              required
            />
          </div>
        </div>

        {/* Date et Heure */}
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="date">Date *</label>
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
            <label htmlFor="heure">Heure *</label>
            <input
              type="time"
              id="heure"
              name="heure"
              value={formData.heure}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        {/* Chauffeur et Véhicule */}
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="chauffeurId">Chauffeur *</label>
            <select
              id="chauffeurId"
              name="chauffeurId"
              value={formData.chauffeurId}
              onChange={handleChange}
              required
            >
              <option value="">-- Sélectionner un chauffeur --</option>
              {chauffeurs.length > 0 ? (
                chauffeurs.map(c => (
                  <option key={c.id} value={c.id}>
                    {c.prenom} {c.nom} ({c.email})
                  </option>
                ))
              ) : (
                <option disabled>Aucun chauffeur disponible</option>
              )}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="vehiculeId">Véhicule *</label>
            <select
              id="vehiculeId"
              name="vehiculeId"
              value={formData.vehiculeId}
              onChange={handleChange}
              required
            >
              <option value="">-- Sélectionner un véhicule --</option>
              {vehicules.length > 0 ? (
                vehicules.map(v => (
                  <option key={v.id} value={v.id}>
                    {v.matricule} - {v.marque} {v.modele}
                  </option>
                ))
              ) : (
                <option disabled>Aucun véhicule disponible</option>
              )}
            </select>
          </div>
        </div>

        {/* Description */}
        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows="4"
            placeholder="Détails supplémentaires de la mission..."
          />
        </div>

        {/* Objet à livrer et Nombre de participants */}
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="objetALivrer">Objet à livrer</label>
            <input
              type="text"
              id="objetALivrer"
              name="objetALivrer"
              value={formData.objetALivrer}
              onChange={handleChange}
              placeholder="Ex: Pièces détachées, documents..."
            />
          </div>
          <div className="form-group">
            <label htmlFor="nombreDeParticipants">Nombre de participants</label>
            <input
              type="number"
              id="nombreDeParticipants"
              name="nombreDeParticipants"
              value={formData.nombreDeParticipants}
              onChange={handleChange}
              min="1"
              placeholder="1"
            />
          </div>
        </div>

        {/* Boutons */}
        <div className="form-actions">
          <button
            type="submit"
            disabled={loading}
            className="btn-primary"
          >
            {loading ? 'Enregistrement...' : (isEditMode ? 'Modifier' : 'Créer')}
          </button>
          <button
            type="button"
            onClick={() => navigate('/chef/missions')}
            className="btn-secondary"
          >
            Annuler
          </button>
        </div>
      </form>
    </div>
  );
}

export default ChefMissionsForm;
