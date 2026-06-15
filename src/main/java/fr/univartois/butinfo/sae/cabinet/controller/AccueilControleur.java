package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import fr.univartois.butinfo.sae.cabinet.controller.PatientFormControleur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur de l'écran d'accueil.
 *
 * Son rôle : quand on clique sur un bouton de l'accueil,
 * charger la bonne vue et lui transmettre les données du modèle.
 */
public class AccueilControleur {

    // ── Données du modèle ──────────────────────────────────────────────────────
    // Ce contrôleur reçoit les services depuis AccueilApplication.
    // Il les redistribue ensuite aux contrôleurs des vues enfants.

    /** Service de gestion des patients (le "modèle" côté patients). */
    private GestionPatients gestionPatients;

    /** Service de gestion des patients (le "modèle" côté patients). */
    private GestionMedicaments gestionMedicaments;

    // ── Fenêtre et scène actuelles ─────────────────────────────────────────────

    /** La fenêtre principale de l'application (la "Stage"). */
    private Stage stage;

    /**
     * La scène de l'accueil.
     * On la garde pour pouvoir y revenir depuis les autres vues.
     */
    private Scene sceneAccueil;

    // ── Méthodes appelées depuis AccueilApplication ────────────────────────────

    /**
     * Reçoit le service patients depuis la classe principale.
     *
     * @param gestionPatients le service à utiliser
     */
    public void setGestionPatients(GestionPatients gestionPatients) {
        this.gestionPatients = gestionPatients;
    }

    /**
     * Reçoit la Stage et la Scene depuis la classe principale.
     * On en a besoin pour changer de vue.
     *
     * @param stage        la fenêtre JavaFX
     * @param sceneAccueil la scène de l'accueil (pour y revenir)
     */
    public void setStageEtScene(Stage stage, Scene sceneAccueil) {
        this.stage = stage;
        this.sceneAccueil = sceneAccueil;
    }

    // ── Méthodes déclenchées par les boutons FXML ──────────────────────────────

    /**
     * Appelée quand on clique sur "Gérer les Patients".
     * Charge la vue liste des patients et lui donne les données.
     */
    @FXML
    private void ouvrirPatients() {
        try {
            // 1. Charger le fichier FXML de la liste des patients
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/view/patient-liste.fxml"));
            Scene scenePatients = new Scene(loader.load());

            // 2. Récupérer le contrôleur de cette nouvelle vue
            PatientListeControleur controleur = loader.getController();

            // 3. Lui transmettre les données dont il a besoin
            controleur.setGestionPatients(gestionPatients);
            controleur.setStageEtScene(stage, scenePatients);

            // 4. Lui donner la scène de l'accueil pour qu'il puisse y revenir
            controleur.setSceneAccueil(sceneAccueil);

            // 5. Afficher la nouvelle scène dans la fenêtre
            stage.setScene(scenePatients);
            stage.setTitle("Gestion des Patients");

        } catch (IOException e) {
            // Si le fichier FXML est introuvable, on affiche l'erreur
            System.err.println("Erreur lors du chargement de patient-liste.fxml : " + e.getMessage());
        }
    }


    @FXML
    private void ouvrirMedicaments() {
        try {
            // 1. Charger le fichier FXML de la liste des patients
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/view/vueMedicaments.fxml"));
            Scene sceneMedicament = new Scene(loader.load());

            // 2. Récupérer le contrôleur de cette nouvelle vue
            MedicamentControleur controleur = loader.getController();

            // 3. Lui transmettre les données dont il a besoin
            controleur.setGestionMedicaments(gestionMedicaments);
            controleur.setStage(stage);

            // 4. Lui donner la scène de l'accueil pour qu'il puisse y revenir
            controleur.setSceneAccueil(sceneAccueil);

            // 5. Afficher la nouvelle scène dans la fenêtre
            stage.setScene(sceneMedicament);
            stage.setTitle("Gestion des Medicaments");

        } catch (IOException e) {
            // Si le fichier FXML est introuvable, on affiche l'erreur
            System.err.println("Erreur lors du chargement de patient-liste.fxml : " + e.getMessage());
        }
    }
    /**
     * Permet d'injecter le service de gestion des médicaments depuis l'application principale.
     * @param gestionMedicaments Le service des médicaments.
     */
    public void setGestionMedicaments(GestionMedicaments gestionMedicaments) {
        this.gestionMedicaments = gestionMedicaments;
    }

    // Tu pourras ajouter ici les méthodes pour les autres vues :
    //ouvrirMedicaments(), etc.
}
