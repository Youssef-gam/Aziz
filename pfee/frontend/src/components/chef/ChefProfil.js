import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { updateUtilisateur } from '../../services/utilisateur';

function ChefProfil() {
  const { user, login } = useAuth();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ nom: '', prenom: '', email: '', telephone: '', zoneAffectation: '', photo: '' });
  const [photoPreview, setPhotoPreview] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    if (user) { setFormData({ nom: user.nom || '', prenom: user.prenom || '', email: user.email || '', telephone: user.telephone || '', zoneAffectation: user.zoneAffectation || '', photo: user.photo || '' }); setPhotoPreview(user.photo || '/default-avatar.png'); }
  }, [user]);

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });
  const handlePhotoChange = (e) => { const file = e.target.files[0]; if (file) { const reader = new FileReader(); reader.onloadend = () => setPhotoPreview(reader.result); reader.readAsDataURL(file); } };
  const handleSubmit = async (e) => { e.preventDefault(); setLoading(true); try { await updateUtilisateur(user.id, formData); const updatedUser = { ...user, ...formData }; localStorage.setItem('user', JSON.stringify(updatedUser)); setSuccess('Profil mis à jour'); setTimeout(() => navigate('/chef/dashboard'), 1500); } catch (err) { setError('Erreur'); } finally { setLoading(false); } };

  return (
    <div className="chef-profil-container">
      <div className="page-header"><h2>👤 Mon profil</h2><button className="btn-secondary" onClick={() => navigate('/chef/dashboard')}>← Retour</button></div>
      {error && <div className="alert alert-error">{error}</div>}{success && <div className="alert alert-success">{success}</div>}
      <form onSubmit={handleSubmit} className="profil-form">
        <div className="profil-photo-section"><div className="photo-container"><img src={photoPreview} alt="Profil" className="profil-photo" /><label className="photo-upload-btn">📷 Changer la photo<input type="file" accept="image/*" onChange={handlePhotoChange} hidden /></label></div></div>
        <div className="form-row"><div className="form-group"><label>Nom *</label><input type="text" name="nom" value={formData.nom} onChange={handleChange} required /></div><div className="form-group"><label>Prénom *</label><input type="text" name="prenom" value={formData.prenom} onChange={handleChange} required /></div></div>
        <div className="form-row"><div className="form-group"><label>Email *</label><input type="email" name="email" value={formData.email} onChange={handleChange} required /></div><div className="form-group"><label>Téléphone</label><input type="tel" name="telephone" value={formData.telephone} onChange={handleChange} /></div></div>
        <div className="form-group"><label>Zone d'affectation</label><input type="text" name="zoneAffectation" value={formData.zoneAffectation} onChange={handleChange} /></div>
        <div className="form-actions"><button type="submit" disabled={loading} className="btn-primary">{loading ? 'Enregistrement...' : 'Enregistrer'}</button><button type="button" onClick={() => navigate('/chef/dashboard')} className="btn-secondary">Annuler</button></div>
      </form>
    </div>
  );
}

export default ChefProfil;