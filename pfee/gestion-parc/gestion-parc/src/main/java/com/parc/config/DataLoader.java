package com.parc.config;

import com.parc.domain.entity.*;
import com.parc.domain.enums.Role;
import com.parc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;

// @Component  <-- DISABLED: données déjà en base
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final GarageRepository garageRepository;
    private final ParcRepository parcRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 [DataLoader] Démarrage de l'initialisation des données...");
        loadData();
        System.out.println("✅ [DataLoader] Initialisation complétée!");
    }

    @Transactional
    public void loadData() {
        System.out.println("🚀 [DataLoader] Démarrage...");
        
        // 🔄 NETTOYER COMPLÈTEMENT TOUTE LA BASE avant de charger
        try {
            System.out.println("🧹 [DataLoader] Nettoyage de la base de données...");
            // Désactiver les ForeignKeys temporarily
            entityManager.createNativeQuery("SET CONSTRAINTS ALL DEFERRED").executeUpdate();
            
            // Supprimer toutes les données dans l'ordre inverse des dépendances
            entityManager.createNativeQuery("DELETE FROM missions").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM maintenances").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM conges").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM chauffeurs").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM chef_de_parc").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM vehicules").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM utilisateurs").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM parcs").executeUpdate();
            
            // Réinitialiser toutes les séquences autocrement  
            entityManager.createNativeQuery("ALTER SEQUENCE utilisateurs_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE parcs_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE vehicules_id_seq RESTART WITH 1").executeUpdate();
            entityManager.createNativeQuery("ALTER SEQUENCE garages_id_seq RESTART WITH 1").executeUpdate();
            
            entityManager.flush();
            System.out.println("✅ Base nettoyée avec succès");
        } catch (Exception e) {
            System.out.println("⚠️ Erreur during cleanup (non-bloquante): " + e.getMessage());
            e.printStackTrace();
        }
        
        // 🏢 Créer un parc par défaut
        Parc parc = null;
        if (parcRepository.count() == 0) {
            parc = new Parc();
            parc.setNom("Parc Principal");
            parc.setAdresse("123 Rue de la Paix, Tunis");
            parc.setCapacite(50);
            parc = parcRepository.save(parc); // ✅ SIMPLE: juste save()
            entityManager.flush(); // Force persist mais garde en cache
            System.out.println("✅ Parc créé avec ID: " + parc.getId());
        } else {
            parc = parcRepository.findAll().get(0);
            System.out.println("✅ Parc found: " + parc.getNom() + " (ID: " + parc.getId() + ")");
        }

        // 👤 Créer l'utilisateur admin s'il n'existe pas
        if (utilisateurRepository.findByEmail("admin@agil.com").isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setNom("Admin");
            admin.setPrenom("User");
            admin.setEmail("admin@agil.com");
            admin.setMotDePasse(passwordEncoder.encode("test123"));
            admin.setTelephone("21000000");
            admin.setRole(Role.ADMIN);
            admin.setActif(true);
            utilisateurRepository.saveAndFlush(admin);
            entityManager.flush();
            System.out.println("✅ Admin créé : admin@agil.com / test123");
        }

        // 👤 Créer le chef de parc s'il n'existe pas
        if (utilisateurRepository.findByEmail("chef@agil.com").isEmpty()) {
            ChefDeParc chef = new ChefDeParc();
            chef.setNom("Dupont");
            chef.setPrenom("Jean");
            chef.setEmail("chef@agil.com");
            chef.setMotDePasse(passwordEncoder.encode("test123"));
            chef.setTelephone("21123456");
            chef.setRole(Role.CHEF);
            chef.setActif(true);
            chef.setDateEmbauche(LocalDate.now());
            chef.setZoneAffectee("Tunis");
            chef.setParc(parc);
            utilisateurRepository.save(chef);
            System.out.println("✅ Chef de parc créé : chef@agil.com / test123");
        }

        // Chauffeur utilisateur créé dans la section chauffeurs supplémentaires

        // 🔧 Créer des chauffeurs supplémentaires
        if (chauffeurRepository.findByEmail("pierre@agil.com").isEmpty()) {
            Chauffeur c1 = new Chauffeur();
            c1.setNom("Dubois");
            c1.setPrenom("Pierre");
            c1.setEmail("pierre@agil.com");
            c1.setMotDePasse(passwordEncoder.encode("test123"));
            c1.setTelephone("21345678");
            c1.setRole(Role.CHAUFFEUR);
            c1.setActif(true);
            c1.setNumeroPermis("P123456");
            c1.setDateExpirationPermis(LocalDate.now().plusYears(5));
            c1.setParc(parc);
            chauffeurRepository.save(c1);
            System.out.println("✅ Chauffeur créé: Pierre Dubois (parc_id=" + c1.getParc().getId() + ")");
        }

        if (chauffeurRepository.findByEmail("marie@agil.com").isEmpty()) {
            Chauffeur c2 = new Chauffeur();
            c2.setNom("Lefevre");
            c2.setPrenom("Marie");
            c2.setEmail("marie@agil.com");
            c2.setMotDePasse(passwordEncoder.encode("test123"));
            c2.setTelephone("21456789");
            c2.setRole(Role.CHAUFFEUR);
            c2.setActif(true);
            c2.setNumeroPermis("P234567");
            c2.setDateExpirationPermis(LocalDate.now().plusYears(5));
            c2.setParc(parc);
            chauffeurRepository.save(c2);
            System.out.println("✅ Chauffeur créé: Marie Lefevre (parc_id=" + c2.getParc().getId() + ")");
        }

        if (chauffeurRepository.findByEmail("sophie@agil.com").isEmpty()) {
            Chauffeur c3 = new Chauffeur();
            c3.setNom("Bernard");
            c3.setPrenom("Sophie");
            c3.setEmail("sophie@agil.com");
            c3.setMotDePasse(passwordEncoder.encode("test123"));
            c3.setTelephone("21567890");
            c3.setRole(Role.CHAUFFEUR);
            c3.setActif(true);
            c3.setNumeroPermis("P345678");
            c3.setDateExpirationPermis(LocalDate.now().plusYears(5));
            c3.setParc(parc);
            chauffeurRepository.save(c3);
            System.out.println("✅ Chauffeur créé: Sophie Bernard (parc_id=" + c3.getParc().getId() + ")");
        }

        if (chauffeurRepository.findByEmail("paul@agil.com").isEmpty()) {
            Chauffeur c4 = new Chauffeur();
            c4.setNom("Moreau");
            c4.setPrenom("Paul");
            c4.setEmail("paul@agil.com");
            c4.setMotDePasse(passwordEncoder.encode("test123"));
            c4.setTelephone("21678901");
            c4.setRole(Role.CHAUFFEUR);
            c4.setActif(true);
            c4.setNumeroPermis("P456789");
            c4.setDateExpirationPermis(LocalDate.now().plusYears(5));
            c4.setParc(parc);
            chauffeurRepository.save(c4);
            System.out.println("✅ Chauffeur créé: Paul Moreau (parc_id=" + c4.getParc().getId() + ")");
        }

        System.out.println("✅ Vérification chauffeurs complétée");

        // 🚗 Créer des véhicules
        if (vehiculeRepository.findByMatricule("TN-123-AA").isEmpty()) {
            Vehicule v1 = new Vehicule();
            v1.setMatricule("TN-123-AA");
            v1.setNumeroChassis("CHASSIS001");
            v1.setMarque("Peugeot");
            v1.setModele("208");
            v1.setDateMiseEnService(LocalDate.now().minusYears(2));
            v1.setStatut("DISPONIBLE");
            v1.setParc(parc);
            vehiculeRepository.save(v1);
            System.out.println("✅ Véhicule créé: TN-123-AA (parc_id=" + v1.getParc().getId() + ")");
        }

        if (vehiculeRepository.findByMatricule("TN-124-BB").isEmpty()) {
            Vehicule v2 = new Vehicule();
            v2.setMatricule("TN-124-BB");
            v2.setNumeroChassis("CHASSIS002");
            v2.setMarque("Renault");
            v2.setModele("Clio");
            v2.setDateMiseEnService(LocalDate.now().minusYears(3));
            v2.setStatut("DISPONIBLE");
            v2.setParc(parc);
            vehiculeRepository.save(v2);
            System.out.println("✅ Véhicule créé: TN-124-BB (parc_id=" + v2.getParc().getId() + ")");
        }

        if (vehiculeRepository.findByMatricule("TN-125-CC").isEmpty()) {
            Vehicule v3 = new Vehicule();
            v3.setMatricule("TN-125-CC");
            v3.setNumeroChassis("CHASSIS003");
            v3.setMarque("Fiat");
            v3.setModele("500");
            v3.setDateMiseEnService(LocalDate.now().minusYears(1));
            v3.setStatut("DISPONIBLE");
            v3.setParc(parc);
            vehiculeRepository.save(v3);
            System.out.println("✅ Véhicule créé: TN-125-CC (parc_id=" + v3.getParc().getId() + ")");
        }

        System.out.println("✅ Vérification véhicules complétée");

        // 🏠 Créer des garages
        if (garageRepository.findBySpecialite("Mécanique générale").isEmpty()) {
            Garage g1 = new Garage();
            g1.setSpecialite("Mécanique générale");
            g1.setAdresse("123 Rue de la Paix, Tunis");
            g1.setCapacite(10);
            g1.setTelephone("71123456");
            garageRepository.save(g1);
            System.out.println("✅ Garage créé: Mécanique générale");
        }

        if (garageRepository.findBySpecialite("Carrosserie").isEmpty()) {
            Garage g2 = new Garage();
            g2.setSpecialite("Carrosserie");
            g2.setAdresse("456 Avenue Habib Bourguiba, Sfax");
            g2.setCapacite(5);
            g2.setTelephone("74234567");
            garageRepository.save(g2);
            System.out.println("✅ Garage créé: Carrosserie");
        }

        System.out.println("✅ Vérification garages complétée");

        System.out.println("✅ Données d'initialisation chargées avec succès");
    }
}
