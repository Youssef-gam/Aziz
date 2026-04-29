import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getUtilisateur, createUtilisateur, updateUtilisateur } from '../../services/utilisateur';
import { getParcs } from '../../services/parc';
import { getGarages } from '../../services/garage';

function UserForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [parcs, setParcs] = useState([]);
  const [loadingParcs, setLoadingParcs] = useState(false);
  const [garages, setGarages] = useState([]);
  const [loadingGarages, setLoadingGarages] = useState(false);
  const [user, setUser] = useState({
    nom: '',
    prenom: '',
    email: '',
    telephone: '',
    role: 'CHAUFFEUR',
    actif: true,
    motDePasse: '',
    numeroPermis: '',
    dateExpirationPermis: '',
    disponible: 'disponible',
    id_parc: '',
    dateEmbauche: '',
    zoneAffectation: '',
    specialite: '',
    niveau: 'intermédiaire',
    id_garage: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const isEditMode = !!id;

  useEffect(() => {
    const fetchParcs = async () => {
      setLoadingParcs(true);
      try {
        const response = await getParcs();
        setParcs(response.data);
      } catch (err) {
        console.error('Erreur chargement parcs', err);
      } finally {
        setLoadingParcs(false);
      }
    };

    const fetchGarages = async () => {
      setLoadingGarages(true);
      try {
        const response = await getGarages();
        setGarages(response.data);
      } catch (err) {
        console.error('Erreur chargement garages', err);
      } finally {
        setLoadingGarages(false);
      }
    };

    fetchParcs();
    fetchGarages();

    if (isEditMode) {
      const fetchUser = async () => {
        try {
          const response = await getUtilisateur(id);
          setUser(prev => ({ ...prev, ...response.data }));
        } catch (err) {
          setError('Erreur lors du chargement de l’utilisateur');
        }
      };
      fetchUser();
    }
  }, [id, isEditMode]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setUser(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');
    try {
      if (isEditMode) {
        await updateUtilisateur(id, user);
        setSuccess('Utilisateur modifié avec succès');
      } else {
        await createUtilisateur(user);
        setSuccess('Utilisateur créé avec succès');
        setUser({
          nom: '', prenom: '', email: '', telephone: '', role: 'CHAUFFEUR', actif: true, motDePasse: '',
          numeroPermis: '', dateExpirationPermis: '', disponible: 'disponible', id_parc: '',
          dateEmbauche: '', zoneAffectation: '',
          specialite: '', niveau: 'intermédiaire', id_garage: '',
        });
      }
      setTimeout(() => navigate('/admin/users'), 1500);
    } catch (err) {
      setError('Erreur lors de l’enregistrement');
    } finally {
      setLoading(false);
    }
  };

  const roles = [
    { value: 'CHAUFFEUR', label: 'Chauffeur' },
    { value: 'CHEF', label: 'Chef de parc' },
    { value: 'OPERATEUR_MAINTENANCE', label: 'Opérateur maintenance' },
  ];

  return (
    <div className="user-form-container">
      <div className="page-header">
        <h2>{isEditMode ? 'Modifier' : 'Ajouter'} un utilisateur</h2>
        <button className="btn-secondary" onClick={() => navigate('/admin/users')}>← Retour</button>
      </div>

      {error && <div className="alert alert-error">{error}</div>}
      {success && <div className="alert alert-success">{success}</div>}

      <form onSubmit={handleSubmit} className="user-form">
        {/* Champs communs */}
        <div className="form-row">
          <div className="form-group"><label>Nom *</label><input type="text" name="nom" value={user.nom} onChange={handleChange} required /></div>
          <div className="form-group"><label>Prénom *</label><input type="text" name="prenom" value={user.prenom} onChange={handleChange} required /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Email *</label><input type="email" name="email" value={user.email} onChange={handleChange} required /></div>
          <div className="form-group"><label>Téléphone</label><input type="tel" name="telephone" value={user.telephone} onChange={handleChange} /></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Rôle *</label><select name="role" value={user.role} onChange={handleChange} required>
            {roles.map(r => <option key={r.value} value={r.value}>{r.label}</option>)}
          </select></div>
          <div className="form-group checkbox-group"><label><input type="checkbox" name="actif" checked={user.actif} onChange={handleChange} /> Compte actif</label></div>
        </div>
        <div className="form-row">
          <div className="form-group"><label>Mot de passe</label><input type="password" name="motDePasse" value={user.motDePasse} onChange={handleChange} placeholder="Laissez vide pour 'default123'" /></div>
        </div>

        {/* Chauffeur */}
        {user.role === 'CHAUFFEUR' && (
          <div className="role-fields">
            <h3>Chauffeur</h3>
            <div className="form-row">
              <div className="form-group"><label>Numéro permis</label><input type="text" name="numeroPermis" value={user.numeroPermis} onChange={handleChange} /></div>
              <div className="form-group"><label>Date expiration permis</label><input type="date" name="dateExpirationPermis" value={user.dateExpirationPermis} onChange={handleChange} /></div>
            </div>
            <div className="form-row">
              <div className="form-group"><label>Disponibilité</label><select name="disponible" value={user.disponible} onChange={handleChange}>
                <option value="disponible">Disponible</option><option value="occupe">Occupé</option><option value="congé">En congé</option>
              </select></div>
              <div className="form-group"><label>Parc</label><select name="id_parc" value={user.id_parc} onChange={handleChange}>
                <option value="">-- Sélectionner --</option>
                {loadingParcs ? <option disabled>Chargement...</option> : parcs.map(p => <option key={p.id} value={p.id}>{p.nom}</option>)}
              </select></div>
            </div>
          </div>
        )}

        {/* Chef de parc */}
        {user.role === 'CHEF' && (
          <div className="role-fields">
            <h3>Chef de parc</h3>
            <div className="form-row">
              <div className="form-group"><label>Date d'embauche</label><input type="date" name="dateEmbauche" value={user.dateEmbauche} onChange={handleChange} /></div>
              <div className="form-group"><label>Zone affectation</label><input type="text" name="zoneAffectation" value={user.zoneAffectation} onChange={handleChange} /></div>
            </div>
            <div className="form-row">
              <div className="form-group"><label>Parc</label><select name="id_parc" value={user.id_parc} onChange={handleChange}>
                <option value="">-- Sélectionner --</option>
                {loadingParcs ? <option disabled>Chargement...</option> : parcs.map(p => <option key={p.id} value={p.id}>{p.nom}</option>)}
              </select></div>
            </div>
          </div>
        )}

        {/* Opérateur maintenance */}
        {user.role === 'OPERATEUR_MAINTENANCE' && (
          <div className="role-fields">
            <h3>Opérateur maintenance</h3>
            <div className="form-row">
              <div className="form-group"><label>Spécialité</label><input type="text" name="specialite" value={user.specialite} onChange={handleChange} /></div>
              <div className="form-group"><label>Date d'embauche</label><input type="date" name="dateEmbauche" value={user.dateEmbauche} onChange={handleChange} /></div>
            </div>
            <div className="form-row">
              <div className="form-group"><label>Niveau</label><select name="niveau" value={user.niveau} onChange={handleChange}>
                <option value="débutant">Débutant</option><option value="intermédiaire">Intermédiaire</option><option value="expert">Expert</option>
              </select></div>
              <div className="form-group"><label>Garage associé</label><select name="id_garage" value={user.id_garage} onChange={handleChange}>
                <option value="">-- Sélectionner un garage --</option>
                {loadingGarages ? <option disabled>Chargement des garages...</option> : garages.map(g => <option key={g.id} value={g.id}>{g.adresse || `Garage ${g.id}`}</option>)}
              </select></div>
            </div>
          </div>
        )}

        <div className="form-actions">
          <button type="submit" disabled={loading} className="btn-primary">{loading ? 'Enregistrement...' : (isEditMode ? 'Modifier' : 'Créer')}</button>
          <button type="button" onClick={() => navigate('/admin/users')} className="btn-secondary">Annuler</button>
        </div>
      </form>
    </div>
  );
}

export default UserForm;