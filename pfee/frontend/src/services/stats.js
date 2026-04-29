import api from './api';

export const getChefDashboardStats = async (parcId) => {
  try {
    // Charger les utilisateurs du parc
    const usersResponse = await api.get('/utilisateurs');
    const allUsers = usersResponse.data;
    const parcUsers = parcId ? allUsers.filter(u => u.parcId === parcId) : allUsers;

    // Charger les véhicules du parc
    const vehiculesResponse = await api.get('/vehicules');
    const allVehicules = vehiculesResponse.data;
    const parcVehicules = parcId ? allVehicules.filter(v => v.parcId === parcId) : allVehicules;

    // Charger les missions
    const missionsResponse = await api.get('/missions');
    const allMissions = missionsResponse.data;
    const parcMissions = parcId ? allMissions.filter(m => m.parcId === parcId) : allMissions;

    // Statistiques chauffeurs
    const chauffeurs = parcUsers.filter(u => u.role === 'CHAUFFEUR');
    const chauffeursDisponibles = chauffeurs.filter(c => c.disponible === 'disponible').length;
    const chauffeursOccupees = chauffeurs.filter(c => c.disponible === 'occupe').length;
    const chauffeursConges = chauffeurs.filter(c => c.disponible === 'congé').length;

    // Statistiques véhicules
    const vehiculesDisponibles = parcVehicules.filter(v => v.statut === 'DISPONIBLE').length;
    const vehiculesMaintenance = parcVehicules.filter(v => v.statut === 'MAINTENANCE').length;

    // Statistiques missions
    const missionsEnCours = parcMissions.filter(m => m.statut === 'EN_COURS').length;
    const missionsTerminees = parcMissions.filter(m => m.statut === 'TERMINEE').length;

    return {
      totalChauffeurs: chauffeurs.length,
      chauffeursDisponibles,
      chauffeursOccupees,
      chauffeursConges,
      totalVehicules: parcVehicules.length,
      vehiculesDisponibles,
      vehiculesMaintenance,
      totalMissions: parcMissions.length,
      missionsEnCours,
      missionsTerminees
    };
  } catch (error) {
    console.error('Erreur chargement stats chef', error);
    throw error;
  }
};