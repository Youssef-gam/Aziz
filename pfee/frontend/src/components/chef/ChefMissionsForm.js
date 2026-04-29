import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import { getChauffeurs } from '../../services/chauffeur';
import { getVehicules } from '../../services/vehicule';
import { createMission, updateMission, getMission } from '../../services/mission';

function ChefMissionsForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const { showSuccess, showError } = useNotification();
  
  const [formData, setFormData] = useState({
    titre: '',
    destination: '',
    date: '',
    heure: '09:00',
    chauffeurId: '',
    vehiculeId: '',
    description: '',
    objetALivrer: '',
    nombreDeParticipants: 1
  });

  const [chauffeurs, setChauffeurs] = useState([]);
  const [vehicules, setVehicules] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pageLoading, setPageLoading] = useState(true);
  
  const isEditMode = !!id;

  // Charger les données
  useEffect(() => {
    loadData();
  }, []);

  // Charger mission en mode édition
  useEffect(() => {
    if (isEditMode) {
      loadMission();
    }
  }, [id, isEditMode]);

  const loadData = async () => {
    try {
      const [chRes, vRes] = await Promise.all([
        getChauffeurs(),
        getVehicules()
      ]);

      console.log('✅ Chauffeurs reçus:', chRes.data?.length || 0);
      console.log('✅ Véhicules reçus:', vRes.data?.length || 0);

      setChauffeurs(chRes.data || []);
      setVehicules(vRes.data || []);
    } catch (err) {
      console.error('❌ Erreur chargement:', err);
      showError('❌ Erreur lors du chargement');
    } finally {
      setPageLoading(false);
    }
  };

  const loadMission = async () => {
    try {
      const res = await getMission(id);
      setFormData({
        titre: res.data.description || '',
        destination: res.data.destination || '',
        date: res.data.dateDebut || '',
        heure: '09:00',
        chauffeurId: res.data.chauffeurId ? res.data.chauffeurId.toString() : '',
        vehiculeId: res.data.vehiculeId ? res.data.vehiculeId.toString() : '',
        description: '',
        objetALivrer: res.data.objetALivrer || '',
        nombreDeParticipants: res.data.nombreDeParticipants || 1
      });
    } catch (err) {
      console.error('❌ Erreur mission:', err);
      showError('❌ Erreur lors du chargement de la mission');
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!formData.titre || !formData.destination || !formData.date || !formData.chauffeurId || !formData.vehiculeId) {
      showError('❌ Veuillez remplir tous les champs obligatoires');
      return;
    }

    setLoading(true);

    try {
      const data = {
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
        await updateMission(id, data);
        showSuccess('✅ Mission modifiée');
      } else {
        await createMission(data);
        showSuccess('✅ Mission créée');
      }

      setTimeout(() => navigate('/chef/missions'), 1500);
    } catch (err) {
      console.error('❌ Erreur:', err);
      showError('❌ Erreur: ' + (err.response?.data?.message || err.message));
    } finally {
      setLoading(false);
    }
  };

  if (pageLoading) {
    return <div className="loading">Chargement...</div>;
  }

  return (
    <div className="user-form-container">
      <div className="page-header">
        <h2>{isEditMode ? 'Modifier' : 'Créer'} une mission</h2>
        <button className="btn-secondary" onClick={() => navigate('/chef/missions')}>← Retour</button>
      </div>

      <form onSubmit={handleSubmit} className="user-form">
        <div className="form-row">
          <div className="form-group">
            <label>Titre *</label>
            <input
              type="text"
              name="titre"
              value={formData.titre}
              onChange={handleChange}
              placeholder="Titre de la mission"
              required
            />
          </div>
          <div className="form-group">
            <label>Destination *</label>
            <input
              type="text"
              name="destination"
              value={formData.destination}
              onChange={handleChange}
              placeholder="Destination"
              required
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label>Date *</label>
            <input
              type="date"
              name="date"
              value={formData.date}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label>Heure *</label>
            <input
              type="time"
              name="heure"
              value={formData.heure}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label>Chauffeur * ({chauffeurs.length})</label>
            <select
              name="chauffeurId"
              value={formData.chauffeurId}
              onChange={handleChange}
              required
            >
              <option value="">-- Choisir un chauffeur --</option>
              {chauffeurs && chauffeurs.length > 0 ? (
                chauffeurs.map((c) => (
                  <option key={c.id} value={c.id}>
                    {c.prenom} {c.nom}
                  </option>
                ))
              ) : (
                <option disabled>Aucun chauffeur disponible</option>
              )}
            </select>
          </div>

          <div className="form-group">
            <label>Véhicule * ({vehicules.length})</label>
            <select
              name="vehiculeId"
              value={formData.vehiculeId}
              onChange={handleChange}
              required
            >
              <option value="">-- Choisir un véhicule --</option>
              {vehicules && vehicules.length > 0 ? (
                vehicules.map((v) => (
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

        <div className="form-group">
          <label>Description</label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows="4"
            placeholder="Description supplémentaire..."
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label>Objet à livrer</label>
            <input
              type="text"
              name="objetALivrer"
              value={formData.objetALivrer}
              onChange={handleChange}
              placeholder="Ex: Pièces détachées, documents..."
            />
          </div>
          <div className="form-group">
            <label>Nombre de participants</label>
            <input
              type="number"
              name="nombreDeParticipants"
              value={formData.nombreDeParticipants}
              onChange={handleChange}
              min="1"
              placeholder="1"
            />
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? 'Enregistrement...' : (isEditMode ? 'Modifier' : 'Créer')}
          </button>
          <button type="button" onClick={() => navigate('/chef/missions')} className="btn-secondary">
            Annuler
          </button>
        </div>

        <div style={{ marginTop: '20px', padding: '10px', backgroundColor: '#f0f0f0', borderRadius: '5px', fontSize: '12px' }}>
          <strong>🔍 Debug Info:</strong><br/>
          Chauffeurs: {chauffeurs.length} | Véhicules: {vehicules.length}
        </div>
      </form>
    </div>
  );
}

export default ChefMissionsForm;