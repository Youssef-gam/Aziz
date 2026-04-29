-- =============================== UTILISATEURS DE TEST ===============================
-- Les mots de passe doivent être hashés avec BCrypt
-- admin@agil.com / Admin@123 → $2a$10$N9gM5PqOgXJE3lJmQ...
-- chef@agil.com / Chef@123 → $2a$10$N9gM5PqOgXJE3lJmQ...

-- Insérer les utilisateurs de test (les hashs sont pré-générés avec BCrypt)
-- Hash pour "Admin@123":
-- Hash pour "Chef@123":

-- Pour générer des hashs, utilise: https://bcrypt-generator.com/
-- Exemple : mot de passe "test123" = $2a$10$EXqZQM5i8sAQhLCZxGK3Hu5v5xA3O7nLG3qC.o.eF.xZQZ4z2lHhS

-- ADMIN
INSERT INTO utilisateurs (id, nom, prenom, email, moto_de_passe, telephone, role, photo, actif, derniere_connexion)
VALUES (1, 'Admin', 'User', 'admin@agil.com', '$2a$10$h3LU1PvOq.xo3ElsF5EXXupVfXuMfLWYvXPVhJNrQvKZpuJTHmVrm', '21000000', 'ADMIN', NULL, true, NULL)
ON CONFLICT DO NOTHING;

-- CHEF DE PARC
INSERT INTO utilisateurs (id, nom, prenom, email, moto_de_passe, telephone, role, photo, actif, derniere_connexion)
VALUES (2, 'Dupont', 'Jean', 'chef@agil.com', '$2a$10$h3LU1PvOq.xo3ElsF5EXXupVfXuMfLWYvXPVhJNrQvKZpuJTHmVrm', '21123456', 'CHEF', NULL, true, NULL)
ON CONFLICT DO NOTHING;

-- CHAUFFEUR
INSERT INTO utilisateurs (id, nom, prenom, email, moto_de_passe, telephone, role, photo, actif, derniere_connexion)
VALUES (3, 'Martin', 'Luc', 'chauffeur@agil.com', '$2a$10$h3LU1PvOq.xo3ElsF5EXXupVfXuMfLWYvXPVhJNrQvKZpuJTHmVrm', '21234567', 'CHAUFFEUR', NULL, true, NULL)
ON CONFLICT DO NOTHING;
-- Commit
COMMIT;
