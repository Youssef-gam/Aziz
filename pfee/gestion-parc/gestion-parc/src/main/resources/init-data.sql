-- Utilisateurs de test
-- Mot de passe pour tous: "test123"
-- Hash BCrypt: $2a$10$X7AK0wMKVNR0E7S1qDvKte6h9dPM1QLaHYLzR6YPnKVZ2kGzhqpnq

INSERT INTO utilisateurs (nom, prenom, email, moto_de_passe, telephone, role, photo, actif)
SELECT 'Admin', 'User', 'admin@agil.com', '$2a$10$X7AK0wMKVNR0E7S1qDvKte6h9dPM1QLaHYLzR6YPnKVZ2kGzhqpnq', '21000000', 'ADMIN', NULL, true
WHERE NOT EXISTS (SELECT 1 FROM utilisateurs WHERE email = 'admin@agil.com');

INSERT INTO utilisateurs (nom, prenom, email, moto_de_passe, telephone, role, photo, actif)
SELECT 'Dupont', 'Jean', 'chef@agil.com', '$2a$10$X7AK0wMKVNR0E7S1qDvKte6h9dPM1QLaHYLzR6YPnKVZ2kGzhqpnq', '21123456', 'CHEF', NULL, true
WHERE NOT EXISTS (SELECT 1 FROM utilisateurs WHERE email = 'chef@agil.com');

INSERT INTO utilisateurs (nom, prenom, email, moto_de_passe, telephone, role, photo, actif)
SELECT 'Martin', 'Luc', 'chauffeur@agil.com', '$2a$10$X7AK0wMKVNR0E7S1qDvKte6h9dPM1QLaHYLzR6YPnKVZ2kGzhqpnq', '21234567', 'CHAUFFEUR', NULL, true
WHERE NOT EXISTS (SELECT 1 FROM utilisateurs WHERE email = 'chauffeur@agil.com');