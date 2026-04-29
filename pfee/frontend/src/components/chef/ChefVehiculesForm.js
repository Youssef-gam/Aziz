import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { getVehicule, createVehicule, updateVehicule } from '../../services/vehicule';

function ChefVehiculesForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    matricule: '', numeroChassis: '', marque: '', modele: '', typeCarburant: '',
    couleur: '', kilometre: '', capaciteReservoir: '', dateMiseEnService: '',
    dateExpirationVisiteTechnique: '', dateExpirationCarteGrise: '', puissanceFiscale: '',
    statut: 'disponible', nomSocieteAssurance: '', dateExpirationAssurance: '', montantAssurance: '',
    parcId: user?.parcId || null
  });
  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(true);
  const isEditMode = !!id;

  useEffect(() => {
    if (isEditMode) {
      const fetchVehicule = async () => {
        try {
          const response = await getVehicule(id);
          setFormData(response.data);
        } catch (err) {
          console.error(err);
        } finally {
          setLoadingData(false);
        }
      };
      fetchVehicule();
    } else {
      setLoadingData(false);
    }
  }, [id, isEditMode]); // ✅ Ajout de isEditMode dans les dépendances

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    // Adapter les noms de champs pour correspondre au backend
    const dataToSend = {
      ...formData,
      parcId: user?.parcId,
      kilometre: parseFloat(formData.kilometre) || 0,
      capaciteReservoir: parseFloat(formData.capaciteReservoir) || 0,
      puissanceFiscale: parseInt(formData.puissanceFiscale, 10) || 0,
      statut: formData.statut?.toUpperCase?.() || 'DISPONIBLE',
      montantAssurance: parseFloat(formData.montantAssurance) || 0
    };

    // Supprimer les champs qui ne sont pas attendus par le backend
    delete dataToSend.kilometrage;
    delete dataToSend.poursuivanteFiscale;
    delete dataToSend.id_parc;

    try {
      if (isEditMode) {
        await updateVehicule(id, dataToSend);
      } else {
        await createVehicule(dataToSend);
      }
      navigate('/chef/vehicules');
    } catch (err) {
      console.error('Erreur sauvegarde véhicule', err);
      const responseData = err?.response?.data;
      const message = responseData?.message || responseData || err.message || 'Erreur inconnue';
      const messageText = typeof message === 'object' ? JSON.stringify(message) : message;
      alert(`Erreur lors de l'enregistrement du véhicule : ${messageText}`);
    } finally {
      setLoading(false);
    }
  };

  if (loadingData) return <div className="loading">Chargement...</div>;

  return (
    <div className="user-form-container">
      <div className="page-header">
        <h2>{isEditMode ? 'Modifier' : 'Ajouter'} un véhicule</h2>
        <button className="btn-secondary" onClick={() => navigate('/chef/vehicules')}>← Retour</button>
      </div>
      <form onSubmit={handleSubmit} className="user-form">
        <div className="form-row">
          <div className="form-group"><label>Matricule *</label><input type="text" name="matricule" value={formData.matricule} onChange={handleChange} required /></div>
          <div className="form-group"><label>Numéro Châssis</label><input type="text" name="numeroChassis" value={formData.numeroChassis} onChange={handleChange} /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Marque *</label><input type="text" name="marque" value={formData.marque} onChange={handleChange} required /></div>
          <div className="form-group"><label>Modèle *</label><input type="text" name="modele" value={formData.modele} onChange={handleChange} required /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Type carburant</label><select name="typeCarburant" value={formData.typeCarburant} onChange={handleChange}><option value="">Sélectionner</option><option value="Essence">Essence</option><option value="Diesel">Diesel</option><option value="Electrique">Electrique</option><option value="Hybride">Hybride</option></select></div>
          <div className="form-group"><label>Couleur</label><input type="text" name="couleur" value={formData.couleur} onChange={handleChange} /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Kilométrage (km)</label><input type="number" name="kilometre" value={formData.kilometre} onChange={handleChange} /></div>
          <div className="form-group"><label>Capacité réservoir (L)</label><input type="number" step="0.1" name="capaciteReservoir" value={formData.capaciteReservoir} onChange={handleChange} /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Date mise en service</label><input type="date" name="dateMiseEnService" value={formData.dateMiseEnService} onChange={handleChange} /></div>
          <div className="form-group"><label>Expiration visite technique</label><input type="date" name="dateExpirationVisiteTechnique" value={formData.dateExpirationVisiteTechnique} onChange={handleChange} /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Expiration carte grise</label><input type="date" name="dateExpirationCarteGrise" value={formData.dateExpirationCarteGrise} onChange={handleChange} /></div>
          <div className="form-group"><label>Puissance fiscale</label><input type="number" name="puissanceFiscale" value={formData.puissanceFiscale} onChange={handleChange} /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Statut</label><select name="statut" value={formData.statut} onChange={handleChange}><option value="disponible">Disponible</option><option value="occupe">En mission</option><option value="maintenance">En maintenance</option></select></div>
          <div className="form-group"><label>Société assurance</label><input type="text" name="nomSocieteAssurance" value={formData.nomSocieteAssurance} onChange={handleChange} /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Expiration assurance</label><input type="date" name="dateExpirationAssurance" value={formData.dateExpirationAssurance} onChange={handleChange} /></div>
          <div className="form-group"><label>Montant assurance (DT)</label><input type="number" step="0.01" name="montantAssurance" value={formData.montantAssurance} onChange={handleChange} /></div>
        </div>
        <div className="form-actions">
          <button type="submit" disabled={loading} className="btn-primary">{loading ? 'Enregistrement...' : (isEditMode ? 'Modifier' : 'Ajouter')}</button>
          <button type="button" onClick={() => navigate('/chef/vehicules')} className="btn-secondary">Annuler</button>
        </div>
      </form>
    </div>
  );
}

export default ChefVehiculesForm;