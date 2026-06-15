package fr.univartois.butinfo.sae.cabinet;

import fr.univartois.butinfo.sae.cabinet.controller.AccueilControleur;
import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Classe principale de l'application.
 * Elle lance l'écran d'accueil et initialise toutes les données du modèle.
 *
 * C'est cette classe qui doit être définie comme mainClass dans build.gradle
 * pour la version finale du projet.
 */
public class AccueilApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ── 1. Initialiser les données du modèle ──────────────────────────────
        // Dans un vrai projet, ces données viendraient d'une base de données.
        // Ici on les crée manuellement pour l'exemple.

        GestionPatients gestionPatients = new GestionPatients();
        GestionMedicaments gestionMedicaments = new GestionMedicaments();
        GestionMedecins gestionMedecin = new GestionMedecins();
        gestionPatients.ajouterPatient(new Patient(
                "Dupont", "Marie",
                "06 12 34 56 78", "marie.dupont@email.com",
                "12 rue des Lilas, Lens",
                LocalDate.of(1985, 3, 15),
                "1850315620123", 0.75
        ));
        gestionPatients.ajouterPatient(new Patient(
                "Martin", "Pierre",
                "07 98 76 54 32", "pierre.martin@email.com",
                "5 avenue Victor Hugo, Arras",
                LocalDate.of(1972, 11, 28),
                "1721128620456", 0.70
        ));

        gestionMedicaments.ajouterMedicament(new Medicament("paracetamol", "test description", 5.99));
        gestionMedicaments.ajouterMedicament(new Medicament("Doliprane", "Soulage en temps difficile de SAE !", 7.99));

        gestionMedecin.ajouterMedecin(new Medecin("Dagobert", "Jean", "0606060606", "jeanDagobert@gmail.com", "1", Specialite.GYNECOLOGUE));
        // ── 2. Charger la vue FXML de l'accueil ──────────────────────────────
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/view/accueil.fxml"));
        Scene sceneAccueil = new Scene(loader.load());

        // ── 3. Récupérer le contrôleur et lui donner les données ──────────────
        AccueilControleur controleur = loader.getController();

        // Transmettre les services (le modèle)
        controleur.setGestionPatients(gestionPatients);

        // Transmettre la stage et la scène pour la navigation
        controleur.setStageEtScene(stage, sceneAccueil);

        controleur.setGestionMedicaments(gestionMedicaments);

        controleur.setGestionMedecin(gestionMedecin);
        // ── 4. Afficher la fenêtre ────────────────────────────────────────────
        stage.setScene(sceneAccueil);
        stage.setTitle("Cabinet Médical — Accueil");
        stage.setWidth(450);
        stage.setHeight(350);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
