package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Contrôleur du formulaire d'ajout / modification d'un médecin.
 *
 * Ce contrôleur gère deux cas :
 *  - Mode AJOUT    : medecinAModifier == null  → on crée un nouveau médecin
 *  - Mode ÉDITION  : medecinAModifier != null  → on met à jour le médecin existant
 */
public class MedecinFormController {

    // ── Composants FXML ────────────────────────────────────────────────────────

    @FXML private Label               labelTitre;
    @FXML private TextField           champNom;
    @FXML private TextField           champPrenom;
    @FXML private TextField           champTelephone;
    @FXML private TextField           champEmail;
    @FXML private TextField           champNumeroOrdre;
    @FXML private ComboBox<Specialite> champSpecialite;
    @FXML private TextField           champTarif;
    @FXML private Label               labelErreur;

    // ── Données ────────────────────────────────────────────────────────────────

    /** Le service médecins. */
    private GestionMedecins gestionMedecins;

    /**
     * La liste observable partagée avec MedecinListeControleur.
     * On l'utilise pour ajouter le nouveau médecin → la ListView se met à jour seule.
     */
    private ObservableList<Medecin> observableMedecins;

    /**
     * La ListView de la vue précédente.
     * On en a besoin pour appeler refresh() après une modification.
     */
    private ListView<Medecin> listeView;

    /**
     * Le médecin à modifier (null si on est en mode ajout).
     * C'est ce champ qui permet de savoir dans quel mode on est.
     */
    private Medecin medecinAModifier;

    /** La fenêtre principale. */
    private Stage stage;

    /** La scène de la liste des médecins (pour y revenir après validation/annulation). */
    private Scene sceneListe;

    // ── Initialisation ─────────────────────────────────────────────────────────

    /**
     * Appelé automatiquement par JavaFX après le chargement du FXML.
     * On remplit la ComboBox avec toutes les spécialités disponibles.
     */
    @FXML
    public void initialize() {
        champSpecialite.setItems(FXCollections.observableArrayList(Specialite.values()));
        champSpecialite.getSelectionModel().selectFirst();
    }

    // ── Méthodes appelées depuis MedecinListeControleur ───────────────────────

    /** Reçoit le service médecins. */
    public void setGestionMedecins(GestionMedecins gestionMedecins) {
        this.gestionMedecins = gestionMedecins;
    }

    /** Reçoit la liste observable pour pouvoir l'enrichir. */
    public void setObservableMedecins(ObservableList<Medecin> observableMedecins) {
        this.observableMedecins = observableMedecins;
    }

    /** Reçoit la ListView pour pouvoir la rafraîchir après modification. */
    public void setListeView(ListView<Medecin> listeView) {
        this.listeView = listeView;
    }

    /** Reçoit la Stage et la scène de la liste pour pouvoir y revenir. */
    public void setStageEtSceneListe(Stage stage, Scene sceneListe) {
        this.stage = stage;
        this.sceneListe = sceneListe;
    }

    /**
     * Passe en mode ÉDITION : pré-remplit les champs avec les données du médecin.
     * Cette méthode est appelée SEULEMENT en mode modification.
     *
     * @param medecin le médecin dont on veut modifier les infos
     */
    public void setMedecinAModifier(Medecin medecin) {
        this.medecinAModifier = medecin;

        // Changer le titre
        labelTitre.setText("Modifier un médecin");

        // Pré-remplir les champs avec les valeurs actuelles du médecin
        champNom.setText(medecin.getNom());
        champPrenom.setText(medecin.getPrenom());
        champTelephone.setText(medecin.getTelephone());
        champEmail.setText(medecin.getEmail());
        champNumeroOrdre.setText(medecin.getNumeroOrdre());
        champNumeroOrdre.setDisable(true); // Le numéro d'ordre ne peut pas être modifié
        champSpecialite.getSelectionModel().select(medecin.getSpecialite());
        champTarif.setText(String.valueOf(medecin.getTarifHoraire()));
    }

    // ── Boutons FXML ───────────────────────────────────────────────────────────

    /**
     * Bouton "Valider" :
     *  - Vérifie que les champs sont bien remplis
     *  - Crée un nouveau médecin (mode ajout) OU met à jour l'existant (mode édition)
     *  - Revient à la liste des médecins
     */
    @FXML
    private void valider() {
        // 1. Lire les valeurs des champs
        String nom        = champNom.getText().trim();
        String prenom     = champPrenom.getText().trim();
        String tel        = champTelephone.getText().trim();
        String email      = champEmail.getText().trim();
        String ordre      = champNumeroOrdre.getText().trim();
        Specialite specialite = champSpecialite.getValue();
        String tarifStr   = champTarif.getText().trim();

        // 2. Vérification : aucun champ ne doit être vide
        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty()
                || email.isEmpty() || ordre.isEmpty()
                || specialite == null || tarifStr.isEmpty()) {
            labelErreur.setText("Tous les champs sont obligatoires.");
            return;
        }

        // 3. Convertir le tarif horaire
        double tarif;
        try {
            tarif = Double.parseDouble(tarifStr);
            if (tarif < 0) {
                labelErreur.setText("Le tarif ne peut pas être négatif.");
                return;
            }
        } catch (NumberFormatException e) {
            labelErreur.setText("Tarif invalide. Saisissez un nombre (ex: 30.0).");
            return;
        }

        // 4a. MODE AJOUT : créer un nouveau médecin
        if (medecinAModifier == null) {
            Medecin nouveauMedecin = new Medecin(nom, prenom, tel, email,
                    ordre, specialite, tarif);

            // Ajouter au service (modèle)
            gestionMedecins.ajouterMedecin(nouveauMedecin);

            // Ajouter à la liste observable → la ListView se met à jour seule
            observableMedecins.add(nouveauMedecin);

        } else {
            // 4b. MODE ÉDITION : mettre à jour le médecin existant
            gestionMedecins.mettreAJourMedecin(
                    medecinAModifier.getId(),
                    nom, prenom, tarif, tel, email, specialite
            );

            // Forcer le rafraîchissement de la ListView
            listeView.refresh();
        }

        // 5. Revenir à la liste des médecins
        retourListe();
    }

    /**
     * Bouton "Annuler" : revenir à la liste sans rien faire.
     */
    @FXML
    private void annuler() {
        retourListe();
    }

    // ── Navigation ─────────────────────────────────────────────────────────────

    /** Affiche la scène de la liste des médecins dans la fenêtre. */
    private void retourListe() {
        stage.setScene(sceneListe);
        stage.setTitle("Gestion des Médecins");
    }
}