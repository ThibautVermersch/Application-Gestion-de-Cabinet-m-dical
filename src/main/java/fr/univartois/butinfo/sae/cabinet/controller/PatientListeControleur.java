package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur de la vue "Master-Detail" des patients.
 *
 * Master  = la ListView à gauche (la liste de tous les patients).
 * Detail  = les labels à droite (les infos du patient sélectionné).
 */
public class PatientListeControleur {

    // ── Composants FXML ────────────────────────────────────────────────────────
    // Ces attributs sont automatiquement remplis par JavaFX
    // grâce aux fx:id dans le fichier FXML.

    /** La liste affichée à gauche. */
    @FXML
    private ListView<Patient> listePatients;

    // Labels du panneau de détail (côté droit)
    @FXML private Label labelNom;
    @FXML private Label labelPrenom;
    @FXML private Label labelTel;
    @FXML private Label labelEmail;
    @FXML private Label labelAdresse;
    @FXML private Label labelNaissance;
    @FXML private Label labelSecu;
    @FXML private Label labelTaux;

    // ── Données et navigation ──────────────────────────────────────────────────

    /** Le service qui gère les patients (le "modèle"). */
    private GestionPatients gestionPatients;

    /**
     * La liste observable : quand on ajoute/supprime un patient ici,
     * la ListView se met à jour AUTOMATIQUEMENT. C'est la magie de JavaFX !
     */
    private ObservableList<Patient> observablePatients;

    /** La fenêtre principale. */
    private Stage stage;

    /** La scène actuelle (liste patients). */
    private Scene sceneListe;

    /** La scène de l'accueil (pour pouvoir y revenir). */
    private Scene sceneAccueil;

    // ── Méthodes appelées depuis AccueilControleur ─────────────────────────────

    /**
     * Reçoit le service patients et initialise la liste.
     * À appeler juste après avoir chargé le FXML.
     */
    public void setGestionPatients(GestionPatients gestionPatients) {
        this.gestionPatients = gestionPatients;

        // Convertir la liste Java normale en ObservableList
        // pour que la ListView se rafraîchisse automatiquement
        observablePatients = FXCollections.observableArrayList(
                gestionPatients.listerTousLesPatients()
        );

        // Donner la liste à la ListView
        listePatients.setItems(observablePatients);

        // Personnaliser ce qui est affiché dans chaque cellule :
        // on affiche seulement "Nom Prénom" au lieu du toString() complet
        listePatients.setCellFactory(list -> new ListCell<>() {
            @Override
            public void updateItem(Patient patient, boolean empty) {
                super.updateItem(patient, empty);
                if (empty || patient == null) {
                    setText(null);
                } else {
                    // Affiche "NOM Prénom" dans la liste
                    setText(patient.getNom() + " " + patient.getPrenom());
                }
            }
        });

        // Écouter les clics dans la liste :
        // quand l'utilisateur clique sur un patient, afficher ses détails
        listePatients.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, ancienPatient, nouveauPatient) -> {
                    afficherDetails(nouveauPatient);
                });
    }

    /** Reçoit la Stage et la Scene courante. */
    public void setStageEtScene(Stage stage, Scene sceneListe) {
        this.stage = stage;
        this.sceneListe = sceneListe;
    }

    /** Reçoit la scène de l'accueil pour pouvoir y revenir. */
    public void setSceneAccueil(Scene sceneAccueil) {
        this.sceneAccueil = sceneAccueil;
    }

    // ── Affichage des détails ──────────────────────────────────────────────────

    /**
     * Remplit les labels de droite avec les infos du patient sélectionné.
     * Si patient est null (aucune sélection), on vide les labels.
     */
    private void afficherDetails(Patient patient) {
        if (patient == null) {
            // Vider tous les labels
            labelNom.setText("");
            labelPrenom.setText("");
            labelTel.setText("");
            labelEmail.setText("");
            labelAdresse.setText("");
            labelNaissance.setText("");
            labelSecu.setText("");
            labelTaux.setText("");
        } else {
            // Remplir les labels avec les données du patient
            labelNom.setText(patient.getNom());
            labelPrenom.setText(patient.getPrenom());
            labelTel.setText(patient.getTelephone());
            labelEmail.setText(patient.getEmail());
            labelAdresse.setText(patient.getAdresse());
            labelNaissance.setText(patient.getDateNaissance().toString()
                    + " (" + patient.getAge() + " ans)");
            labelSecu.setText(patient.getNumeroSecu());
            labelTaux.setText((int)(patient.getTauxRemboursement() * 100) + " %");
        }
    }

    // ── Boutons FXML ───────────────────────────────────────────────────────────

    /**
     * Bouton "Ajouter" : ouvre le formulaire en mode création.
     */
    @FXML
    private void ajouterPatient() {
        ouvrirFormulaire(null); // null = pas de patient existant = mode ajout
    }

    /**
     * Bouton "Modifier" : ouvre le formulaire en mode édition
     * avec le patient actuellement sélectionné.
     */
    @FXML
    private void modifierPatient() {
        Patient selectionne = listePatients.getSelectionModel().getSelectedItem();
        if (selectionne != null) {
            ouvrirFormulaire(selectionne); // on passe le patient à modifier
        }
    }

    /**
     * Bouton "Supprimer" : supprime le patient sélectionné.
     */
    @FXML
    private void supprimerPatient() {
        Patient selectionne = listePatients.getSelectionModel().getSelectedItem();
        if (selectionne != null) {
            // Supprimer du service (modèle)
            gestionPatients.supprimerPatient(selectionne.getId());
            // Supprimer de la liste observable (la ListView se met à jour seule)
            observablePatients.remove(selectionne);
            // Vider les détails
            afficherDetails(null);
        }
    }

    /**
     * Bouton "Retour accueil" : revenir à l'écran d'accueil.
     */
    @FXML
    private void retourAccueil() {
        stage.setScene(sceneAccueil);
        stage.setTitle("Cabinet Médical — Accueil");
    }

    // ── Navigation vers le formulaire ─────────────────────────────────────────

    /**
     * Charge la vue du formulaire et lui transmet les données nécessaires.
     *
     * @param patientAModifier null si on ajoute, sinon le patient à éditer
     */
    private void ouvrirFormulaire(Patient patientAModifier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/view/patient-form.fxml"));
            Scene sceneForm = new Scene(loader.load());

            PatientFormControleur controleur = loader.getController();

            // Transmettre ce dont le formulaire a besoin
            controleur.setGestionPatients(gestionPatients);
            controleur.setObservablePatients(observablePatients);
            controleur.setListeView(listePatients);
            // Donner la scène de la liste pour revenir après validation/annulation
            controleur.setStageEtSceneListe(stage, sceneListe);

            // Si on modifie : pré-remplir le formulaire avec les données du patient
            if (patientAModifier != null) {
                controleur.setPatientAModifier(patientAModifier);
            }

            // Afficher le formulaire
            stage.setScene(sceneForm);
            stage.setTitle(patientAModifier == null ? "Ajouter un patient" : "Modifier un patient");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de patient-form.fxml : " + e.getMessage());
        }
    }
}
