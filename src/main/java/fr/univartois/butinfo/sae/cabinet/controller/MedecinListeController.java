package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import javafx.application.Application;
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
 * Contrôleur de la vue "Master-Detail" des médecins.
 *
 * Master  = la ListView à gauche (la liste de tous les médecins).
 * Detail  = les labels à droite (les infos du médecin sélectionné).
 */
public class MedecinListeController {

    // ── Composants FXML ────────────────────────────────────────────────────────

    /** La liste affichée à gauche. */
    @FXML
    private ListView<Medecin> listeMedecins;

    // Labels du panneau de détail (côté droit)
    @FXML private Label labelNom;
    @FXML private Label labelPrenom;
    @FXML private Label labelTel;
    @FXML private Label labelEmail;
    @FXML private Label labelSpecialite;
    @FXML private Label labelNumeroOrdre;
    @FXML private Label labelTarif;

    // ── Données et navigation ──────────────────────────────────────────────────

    /** Le service qui gère les médecins (le "modèle"). */
    private GestionMedecins gestionMedecins;

    /**
     * La liste observable : quand on ajoute/supprime un médecin ici,
     * la ListView se met à jour AUTOMATIQUEMENT.
     */
    private ObservableList<Medecin> observableMedecins;

    /** La fenêtre principale. */
    private Stage stage;

    /** La scène actuelle (liste médecins). */
    private Scene sceneListe;

    /** La scène de l'accueil (pour pouvoir y revenir). */
    private Scene sceneAccueil;

    // ── Méthodes appelées depuis AccueilControleur ─────────────────────────────

    /**
     * Reçoit le service médecins et initialise la liste.
     * À appeler juste après avoir chargé le FXML.
     *
     * @param gestionMedecins le service de gestion des médecins
     */
    public void setGestionMedecins(GestionMedecins gestionMedecins) {
        this.gestionMedecins = gestionMedecins;

        // Convertir la liste Java normale en ObservableList
        observableMedecins = FXCollections.observableArrayList(
                gestionMedecins.listerTousLesMedecins()
        );

        // Donner la liste à la ListView
        listeMedecins.setItems(observableMedecins);

        // Afficher seulement "Dr Nom Prénom" dans chaque cellule
        listeMedecins.setCellFactory(list -> new ListCell<>() {
            @Override
            public void updateItem(Medecin medecin, boolean empty) {
                super.updateItem(medecin, empty);
                if (empty || medecin == null) {
                    setText(null);
                } else {
                    setText("Dr " + medecin.getNom() + " " + medecin.getPrenom());
                }
            }
        });

        // Écouter les clics : afficher les détails du médecin sélectionné
        listeMedecins.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, ancienMedecin, nouveauMedecin) -> {
                    afficherDetails(nouveauMedecin);
                });
    }

    /**
     * Reçoit la Stage et la Scene courante.
     *
     * @param stage      la fenêtre principale
     * @param sceneListe la scène de la liste des médecins
     */
    public void setStageEtScene(Stage stage, Scene sceneListe) {
        this.stage = stage;
        this.sceneListe = sceneListe;
    }

    /**
     * Reçoit la scène de l'accueil pour pouvoir y revenir.
     *
     * @param sceneAccueil la scène de l'écran d'accueil
     */
    public void setSceneAccueil(Scene sceneAccueil) {
        this.sceneAccueil = sceneAccueil;
    }

    // ── Affichage des détails ──────────────────────────────────────────────────

    /**
     * Remplit les labels de droite avec les infos du médecin sélectionné.
     * Si médecin est null (aucune sélection), on vide les labels.
     *
     * @param medecin le médecin à afficher, ou null pour vider
     */
    private void afficherDetails(Medecin medecin) {
        if (medecin == null) {
            labelNom.setText("");
            labelPrenom.setText("");
            labelTel.setText("");
            labelEmail.setText("");
            labelSpecialite.setText("");
            labelNumeroOrdre.setText("");
            labelTarif.setText("");
        } else {
            labelNom.setText(medecin.getNom());
            labelPrenom.setText(medecin.getPrenom());
            labelTel.setText(medecin.getTelephone());
            labelEmail.setText(medecin.getEmail());
            labelSpecialite.setText(medecin.getSpecialite().getLibelle());
            labelNumeroOrdre.setText(medecin.getNumeroOrdre());
            labelTarif.setText(medecin.getTarifHoraire() + " €/h");
        }
    }

    // ── Boutons FXML ───────────────────────────────────────────────────────────

    /**
     * Bouton "Ajouter" : ouvre le formulaire en mode création.
     */
    @FXML
    private void ajouterMedecin() {
        ouvrirFormulaire(null); // null = mode ajout
    }

    /**
     * Bouton "Modifier" : ouvre le formulaire en mode édition
     * avec le médecin actuellement sélectionné.
     */
    @FXML
    private void modifierMedecin() {
        Medecin selectionne = listeMedecins.getSelectionModel().getSelectedItem();
        if (selectionne != null) {
            ouvrirFormulaire(selectionne);
        }
    }

    /**
     * Bouton "Supprimer" : supprime le médecin sélectionné.
     */
    @FXML
    private void supprimerMedecin() {
        Medecin selectionne = listeMedecins.getSelectionModel().getSelectedItem();
        if (selectionne != null) {
            gestionMedecins.supprimerMedecin(selectionne.getId());
            observableMedecins.remove(selectionne);
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
     * @param medecinAModifier null si on ajoute, sinon le médecin à éditer
     */
    private void ouvrirFormulaire(Medecin medecinAModifier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/view/medecin-form.fxml"));
            Scene sceneForm = new Scene(loader.load());

            MedecinFormController controleur = loader.getController();

            // Transmettre ce dont le formulaire a besoin
            controleur.setGestionMedecins(gestionMedecins);
            controleur.setObservableMedecins(observableMedecins);
            controleur.setListeView(listeMedecins);
            controleur.setStageEtSceneListe(stage, sceneListe);

            // Si on modifie : pré-remplir le formulaire
            if (medecinAModifier != null) {
                controleur.setMedecinAModifier(medecinAModifier);
            }

            // Afficher le formulaire
            stage.setScene(sceneForm);
            stage.setTitle(medecinAModifier == null ? "Ajouter un médecin" : "Modifier un médecin");

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de medecin-form.fxml : " + e.getMessage());
        }
    }
}