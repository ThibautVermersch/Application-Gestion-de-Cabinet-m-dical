package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Contrôleur du formulaire d'ajout / modification d'un patient.
 *
 * Ce contrôleur gère deux cas :
 *  - Mode AJOUT    : patientAModifier == null  → on crée un nouveau patient
 *  - Mode ÉDITION  : patientAModifier != null  → on met à jour le patient existant
 */
public class PatientFormControleur {

    // ── Composants FXML ────────────────────────────────────────────────────────
    // Chaque @FXML correspond à un fx:id dans patient-form.fxml

    @FXML private Label labelTitre;
    @FXML private TextField champNom;
    @FXML private TextField champPrenom;
    @FXML private TextField champTelephone;
    @FXML private TextField champEmail;
    @FXML private TextField champAdresse;
    @FXML private TextField champDateNaissance;
    @FXML private TextField champSecu;
    @FXML private TextField champTaux;
    @FXML private Label labelErreur;

    // ── Données ────────────────────────────────────────────────────────────────

    /** Le service patients. */
    private GestionPatients gestionPatients;

    /**
     * La liste observable partagée avec PatientListeControleur.
     * On l'utilise pour ajouter le nouveau patient → la ListView se met à jour seule.
     */
    private ObservableList<Patient> observablePatients;

    /**
     * La ListView de la vue précédente.
     * On en a besoin pour appeler refresh() après une modification.
     */
    private ListView<Patient> listeView;

    /**
     * Le patient à modifier (null si on est en mode ajout).
     * C'est ce champ qui permet de savoir dans quel mode on est.
     */
    private Patient patientAModifier;

    /** La fenêtre principale. */
    private Stage stage;

    /** La scène de la liste des patients (pour y revenir après validation/annulation). */
    private Scene sceneListe;

    // ── Méthodes appelées depuis PatientListeControleur ───────────────────────

    /** Reçoit le service patients. */
    public void setGestionPatients(GestionPatients gestionPatients) {
        this.gestionPatients = gestionPatients;
    }

    /** Reçoit la liste observable pour pouvoir l'enrichir. */
    public void setObservablePatients(ObservableList<Patient> observablePatients) {
        this.observablePatients = observablePatients;
    }

    /** Reçoit la ListView pour pouvoir la rafraîchir après modification. */
    public void setListeView(ListView<Patient> listeView) {
        this.listeView = listeView;
    }

    /** Reçoit la Stage et la scène de la liste pour pouvoir y revenir. */
    public void setStageEtSceneListe(Stage stage, Scene sceneListe) {
        this.stage = stage;
        this.sceneListe = sceneListe;
    }

    /**
     * Passe en mode ÉDITION : pré-remplit les champs avec les données du patient.
     * Cette méthode est appelée SEULEMENT en mode modification.
     *
     * @param patient le patient dont on veut modifier les infos
     */
    public void setPatientAModifier(Patient patient) {
        this.patientAModifier = patient;

        // Changer le titre
        labelTitre.setText("Modifier un patient");

        // Pré-remplir les champs avec les valeurs actuelles du patient
        champNom.setText(patient.getNom());
        champPrenom.setText(patient.getPrenom());
        champTelephone.setText(patient.getTelephone());
        champEmail.setText(patient.getEmail());
        champAdresse.setText(patient.getAdresse());
        champDateNaissance.setText(patient.getDateNaissance().toString());
        champSecu.setText(patient.getNumeroSecu());
        champSecu.setDisable(true); // Le numéro de sécu ne peut pas être modifié
        champTaux.setText(String.valueOf(patient.getTauxRemboursement()));
    }

    // ── Boutons FXML ───────────────────────────────────────────────────────────

    /**
     * Bouton "Valider" :
     *  - Vérifie que les champs sont bien remplis
     *  - Crée un nouveau patient (mode ajout) OU met à jour l'existant (mode édition)
     *  - Revient à la liste des patients
     */
    @FXML
    private void valider() {
        // 1. Lire les valeurs des champs
        String nom       = champNom.getText().trim();
        String prenom    = champPrenom.getText().trim();
        String tel       = champTelephone.getText().trim();
        String email     = champEmail.getText().trim();
        String adresse   = champAdresse.getText().trim();
        String dateStr   = champDateNaissance.getText().trim();
        String secu      = champSecu.getText().trim();
        String tauxStr   = champTaux.getText().trim();

        // 2. Vérification basique : aucun champ ne doit être vide
        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty()
                || email.isEmpty() || adresse.isEmpty()
                || dateStr.isEmpty() || secu.isEmpty() || tauxStr.isEmpty()) {
            labelErreur.setText("Tous les champs sont obligatoires.");
            return; // on arrête ici, on ne ferme pas le formulaire
        }

        // 3. Convertir la date (format AAAA-MM-JJ)
        LocalDate dateNaissance;
        try {
            dateNaissance = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            labelErreur.setText("Format de date invalide. Utilisez AAAA-MM-JJ (ex: 1990-05-21)");
            return;
        }

        // 4. Convertir le taux de remboursement
        double taux;
        try {
            taux = Double.parseDouble(tauxStr);
            if (taux < 0 || taux > 1) {
                labelErreur.setText("Le taux doit être entre 0 et 1 (ex: 0.75 pour 75%).");
                return;
            }
        } catch (NumberFormatException e) {
            labelErreur.setText("Taux invalide. Saisissez un nombre entre 0 et 1 (ex: 0.75).");
            return;
        }

        // 5a. MODE AJOUT : créer un nouveau patient
        if (patientAModifier == null) {
            Patient nouveauPatient = new Patient(nom, prenom, tel, email,
                    adresse, dateNaissance, secu, taux);

            // Ajouter au service (modèle)
            gestionPatients.ajouterPatient(nouveauPatient);

            // Ajouter à la liste observable → la ListView se met à jour seule
            observablePatients.add(nouveauPatient);

        } else {
            // 5b. MODE ÉDITION : mettre à jour le patient existant
            gestionPatients.mettreAJourPatient(
                    patientAModifier.getId(),
                    nom, prenom, tel, email, adresse, taux
            );

            // Mettre à jour aussi la date de naissance (non couverte par mettreAJourPatient)
            patientAModifier.setDateNaissance(dateNaissance);
            patientAModifier.setAdresse(adresse);

            // Forcer le rafraîchissement de la ListView
            // (car on a modifié l'objet directement, pas la liste)
            listeView.refresh();
        }

        // 6. Revenir à la liste des patients
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

    /** Affiche la scène de la liste des patients dans la fenêtre. */
    private void retourListe() {
        stage.setScene(sceneListe);
        stage.setTitle("Gestion des Patients");
    }
}
