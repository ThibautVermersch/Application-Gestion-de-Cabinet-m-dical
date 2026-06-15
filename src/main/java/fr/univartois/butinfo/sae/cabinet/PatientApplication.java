package fr.univartois.butinfo.sae.cabinet;

import fr.univartois.butinfo.sae.cabinet.controller.PatientListeControleur;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Classe principale pour tester UNIQUEMENT la vue des patients.
 *
 * Tu peux la lancer en changeant temporairement le mainClass dans build.gradle :
 *   mainClass = 'fr.univartois.butinfo.sae.cabinet.vue.patient.PatientApplication'
 *
 * Ou en créant une Run Configuration dans IntelliJ.
 */
public class PatientApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ── 1. Créer des données de test ──────────────────────────────────────
        GestionPatients gestionPatients = new GestionPatients();

        // On ajoute quelques patients pour avoir quelque chose à afficher
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
        gestionPatients.ajouterPatient(new Patient(
                "Bernard", "Sophie",
                "06 55 44 33 22", "sophie.bernard@email.com",
                "3 place de la République, Béthune",
                LocalDate.of(1990, 7, 4),
                "2900704620789", 0.80
        ));

        // ── 2. Charger la vue FXML ────────────────────────────────────────────
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fr/univartois/butinfo/sae/cabinet/patient/patient-liste.fxml"));
        Scene scene = new Scene(loader.load());

        // ── 3. Récupérer le contrôleur et lui donner les données ──────────────
        PatientListeControleur controleur = loader.getController();
        controleur.setGestionPatients(gestionPatients);
        controleur.setStageEtScene(stage, scene);
        // Pas de scène d'accueil ici car on teste la vue seule
        controleur.setSceneAccueil(scene); // on pointe vers soi-même

        // ── 4. Afficher la fenêtre ────────────────────────────────────────────
        stage.setScene(scene);
        stage.setTitle("Test — Gestion des Patients");
        stage.setWidth(700);
        stage.setHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
