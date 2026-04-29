import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import { getVehicules } from '../../services/vehicule';
import { getGarages } from '../../services/garage';
import { createMaintenance, updateMaintenance, getMaintenanceById } from '../../services/maintenance';
import '../../styles/chef.css';

function ChefMaintenancesForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const { showSuccess, showError } = useNotification();
  const [formData, setFormData] = useState({
    type: 'REVISION',
    statut: 'PLANIFIEE',
    datePrevue: '',
    cout: '',
    operateur: '',
    rapportProbleme: '',
    dateRealisation: '',
    vehiculeId: '',
    garageId: ''
  });
  const [vehicules, setVehicules] = useState([]);
  const [garages, setGarages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [loadingForm, setLoadingForm] = useState(true);
  const isEditMode = !!id;

  // Charger les véhicules et garages
  useEffect(() => {
    const loadData = async () => {
      try {
        const vehiculesResponse = await getVehicules();
        const filteredVehicules = user?.parcId
          ? vehiculesResponse.data.filter(v => v.parcId === user.parcId)
          : vehiculesResponse.data;
        setVehicules(filteredVehicules);

        // Charger les garages depuis le backend
        const garagesResponse = await getGarages();
        setGarages(garagesResponse.data || []);
      } catch (err) {
        console.error('Erreur chargement données', err);
        showError('Erreur lors du chargement des données');
      } finally {
        setLoadingForm(false);
      }
    };
    loadData();
  }, [user?.parcId]);

  // Charger les données de maintenance en mode édition
  useEffect(() => {
    if (isEditMode) {
      const fetchMaintenance = async () => {
        try {
          const response = await getMaintenanceById(id);
          setFormData({
            type: response.data.type || 'REVISION',
            statut: response.data.statut || 'PLANIFIEE',
            datePrevue: response.data.datePrevue || '',
            cout: response.data.cout || '',
            operateur: response.data.operateur || '',
            rapportProbleme: response.data.rapportProbleme || '',
            dateRealisation: response.data.dateRealisation || '',
            vehiculeId: response.data.vehiculeId?.toString() || '',
            garageId: response.data.garageId?.toString() || ''
          });
        } catch (err) {
          console.error('Erreur chargement maintenance', err);
          showError('Erreur lors du chargement de la maintenance');
        }
      };
      fetchMaintenance();
    }
  }, [id, isEditMode]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    const dataToSend = {
      ...formData,
      cout: parseFloat(formData.cout) || 0,
      vehiculeId: parseInt(formData.vehiculeId),
      garageId: formData.garageId ? parseInt(formData.garageId) : null
    };

    try {
      if (isEditMode) {
        await updateMaintenance(id, dataToSend);
        showSuccess('✅ Maintenance mise à jour avec succès');
      } else {
        await createMaintenance(dataToSend);
        showSuccess('✅ Maintenance créée avec succès');
      }
      setTimeout(() => navigate('/chef/maintenances'), 1500);
    } catch (err) {
      console.error('Erreur formulaire', err);
      showError('❌ Erreur lors de ' + (isEditMode ? 'la mise à jour' : 'la création'));
    } finally {
      setLoading(false);
    }
  };

  if (loadingForm) return <div className="loading">Chargement du formulaire...</div>;

  return (
    <div className="form-container">
      <h2>{isEditMode ? '✏️ Modifier une maintenance' : '🔧 Ajouter une nouvelle maintenance'}</h2>
      
      <form onSubmit={handleSubmit} className="chef-form">
        <div className="form-group">
          <label>Type de maintenance *</label>
          <select name="type" value={formData.type} onChange={handleChange} required>
            <option value="REVISION">Révision</option>
            <option value="REPARATION">Réparation</option>
            <option value="INSPECTION">Inspection</option>
            <option value="ENTRETIEN">Entretien</option>
            <option value="PNEUMATIQUES">Pneumatiques</option>
            <option value="VIDANGE">Vidange</option>
            <option value="FREINS">Freins</option>
            <option value="MAINTENANCE_PREVENTIVE">Maintenance préventive</option>
          </select>
        </div>

        <div className="form-group">
          <label>Véhicule *</label>
          <select name="vehiculeId" value={formData.vehiculeId} onChange={handleChange} required>
            <option value="">Sélectionner un véhicule</option>
            {vehicules.map(v => (
              <option key={v.id} value={v.id}>
                {v.matricule} - {v.marque} {v.modele}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Statut</label>
          <select name="statut" value={formData.statut} onChange={handleChange}>
            <option value="PLANIFIEE">Planifiée</option>
            <option value="EN_COURS">En cours</option>
            <option value="TERMINEE">Terminée</option>
            <option value="ANNULEE">Annulée</option>
          </select>
        </div>

        <div className="form-group">
          <label>Garage</label>
          <select name="garageId" value={formData.garageId} onChange={handleChange}>
            <option value="">Sélectionner un garage (optionnel)</option>
            {garages.map(g => (
              <option key={g.id} value={g.id}>
                {g.specialite} - {g.adresse}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Date prévue *</label>
          <input 
            type="date" 
            name="datePrevue" 
            value={formData.datePrevue} 
            onChange={handleChange} 
            required 
          />
        </div>

        <div className="form-group">
          <label>Date de réalisation</label>
          <input 
            type="date" 
            name="dateRealisation" 
            value={formData.dateRealisation} 
            onChange={handleChange} 
          />
        </div>

        <div className="form-group">
          <label>Coût estimé (€)</label>
          <input 
            type="number" 
            name="cout" 
            value={formData.cout} 
            onChange={handleChange} 
            step="0.01"
            placeholder="0.00"
          />
        </div>

        <div className="form-group">
          <label>Opérateur/Mécanicien</label>
          <input 
            type="text" 
            name="operateur" 
            value={formData.operateur} 
            onChange={handleChange} 
            placeholder="Nom de l'opérateur"
          />
        </div>

        <div className="form-group">
          <label>Rapport du problème</label>
          <textarea 
            name="rapportProbleme" 
            value={formData.rapportProbleme} 
            onChange={handleChange}
            rows="4"
            placeholder="Description du problème détecté"
          />
        </div>

        <div className="form-actions">
          <button type="submit" disabled={loading} className="btn-primary">
            {loading ? 'Traitement...' : (isEditMode ? 'Mettre à jour' : 'Créer')}
          </button>
          <button 
            type="button" 
            onClick={() => navigate('/chef/maintenances')} 
            className="btn-secondary"
          >
            Annuler
          </button>
        </div>
      </form>
    </div>
  );
}

export default ChefMaintenancesForm;
