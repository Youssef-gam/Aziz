-- Réinitialiser le mot de passe de tarek@agil.com à null
UPDATE utilisateurs SET mot_de_passe = NULL WHERE email = 'tarek@agil.com';

-- Vérifier
SELECT id, email, mot_de_passe, actif FROM utilisateurs WHERE email = 'tarek@agil.com';
