package com.parc.config;

import java.time.LocalDate;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoaderNew implements CommandLineRunner {

    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 [DataLoader] Starting initialization...");
        try {
            loadData();
            System.out.println("✅ [DataLoader] All data loaded successfully!");
        } catch (Exception e) {
            System.out.println("❌ [DataLoader] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadData() {
        // Disable FK constraints
        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
            return null;
        });
        
        // Clean database
        transactionTemplate.execute(status -> {
            System.out.println("🧹 Cleaning database...");
            // Supprimer les feuilles de route EN PREMIER (FK constraint)
            entityManager.createNativeQuery("DELETE FROM feuilles_de_route").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM missions").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM maintenances").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM conges").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM chauffeurs").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM chef_de_parc").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM vehicules").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM utilisateurs").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM parc").executeUpdate();
            entityManager.flush();
            System.out.println("✅ Database cleaned");
            return null;
        });
        
        // Reset sequences
        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery("ALTER SEQUENCE utilisateurs_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE parc_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE vehicules_id_seq RESTART WITH 1").executeUpdate();
            entityManager.flush();
            return null;
        });
        
        // Create Parc
        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery(
                "INSERT INTO parc (id, nom, adresse, capacite) VALUES (1, 'Parc Principal', '123 Rue de la Paix, Tunis', 50)"
            ).executeUpdate();
            entityManager.flush();
            System.out.println("✅ Parc created");
            return null;
        });
        
        String hash = passwordEncoder.encode("test123");
        
        // Create Admin & Chef
        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery(
                "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, telephone, role, actif) " +
                "VALUES ('Admin', 'User', 'admin@agil.com', ?, '21000000', 'ADMIN', true)"
            ).setParameter(1, hash).executeUpdate();
            
            entityManager.createNativeQuery(
                "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, telephone, role, actif) " +
                "VALUES ('Dupont', 'Jean', 'chef@agil.com', ?, '21123456', 'CHEF', true)"
            ).setParameter(1, hash).executeUpdate();
            
            entityManager.createNativeQuery(
                "INSERT INTO chef_de_parc (utilisateur_id, parc_id, date_embauche, zone_affectee) " +
                "VALUES (2, 1, ?, 'Tunis')"
            ).setParameter(1, LocalDate.now()).executeUpdate();
            
            entityManager.flush();
            System.out.println("✅ Admin & Chef created");
            return null;
        });
        
        // Create Chauffeurs
        createChauffeur("Dubois", "Pierre", "pierre@agil.com", "21345678", "P123456", hash);
        createChauffeur("Lefevre", "Marie", "marie@agil.com", "21456789", "P234567", hash);
        createChauffeur("Bernard", "Sophie", "sophie@agil.com", "21567890", "P345678", hash);
        createChauffeur("Moreau", "Paul", "paul@agil.com", "21678901", "P456789", hash);
        System.out.println("✅ All chauffeurs created");
        
        // Create Vehicles
        createVehicle("TN-123-AA", "CHASSIS001", "Peugeot", "208");
        createVehicle("TN-124-BB", "CHASSIS002", "Renault", "Clio");
        createVehicle("TN-125-CC", "CHASSIS003", "Fiat", "500");
        System.out.println("✅ All vehicles created");
        
        // Create Garages
        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery(
                "INSERT INTO garages (specialite, adresse, capacite, telephone) " +
                "VALUES ('Mécanique générale', '123 Rue de la Paix, Tunis', 10, '71123456')"
            ).executeUpdate();
            
            entityManager.createNativeQuery(
                "INSERT INTO garages (specialite, adresse, capacite, telephone) " +
                "VALUES ('Carrosserie', '456 Avenue Habib Bourguiba, Sfax', 5, '74234567')"
            ).executeUpdate();
            
            entityManager.flush();
            System.out.println("✅ All garages created");
            return null;
        });
        
        // Enable FK constraints back
        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery("SET CONSTRAINTS ALL IMMEDIATE").executeUpdate();
            return null;
        });
    }
    
    private void createChauffeur(String nom, String prenom, String email, String telephone, String numeroPermis, String hash) {
        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery(
                "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, telephone, role, actif) " +
                "VALUES (?, ?, ?, ?, ?, 'CHAUFFEUR', true)"
            )
            .setParameter(1, nom)
            .setParameter(2, prenom)
            .setParameter(3, email)
            .setParameter(4, hash)
            .setParameter(5, telephone)
            .executeUpdate();
            
            Number userId = (Number) entityManager.createNativeQuery(
                "SELECT id FROM utilisateurs WHERE email = ?"
            ).setParameter(1, email).getSingleResult();
            
            entityManager.createNativeQuery(
                "INSERT INTO chauffeurs (utilisateur_id, parc_id, numero_permis, date_expiration_permis) " +
                "VALUES (?, 1, ?, ?)"
            )
            .setParameter(1, userId)
            .setParameter(2, numeroPermis)
            .setParameter(3, LocalDate.now().plusYears(5))
            .executeUpdate();
            
            entityManager.flush();
            return null;
        });
    }
    
    private void createVehicle(String matricule, String chassis, String marque, String modele) {
        transactionTemplate.execute(status -> {
            entityManager.createNativeQuery(
                "INSERT INTO vehicules (matricule, numero_chassis, marque, modele, parc_id, statut, date_mise_en_service) " +
                "VALUES (?, ?, ?, ?, 1, 'DISPONIBLE', ?)"
            )
            .setParameter(1, matricule)
            .setParameter(2, chassis)
            .setParameter(3, marque)
            .setParameter(4, modele)
            .setParameter(5, LocalDate.now())
            .executeUpdate();
            
            entityManager.flush();
            return null;
        });
    }
}
