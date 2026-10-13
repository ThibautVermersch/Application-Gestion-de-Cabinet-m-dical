package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.modele.Ordonnance;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.modele.LignePrescription; // Importation de votre classe modèle
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;

import java.time.LocalDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OrdonnanceFormController {

    private GestionOrdonnances gestionOrdonnances;
    private GestionMedecins gestionMedecins;
    private GestionPatients gestionPatients;
    private GestionMedicaments gestionMedicaments;

    private Ordonnance ordonnanceEnCours;
    private boolean modeModification = false;

    private final ObservableList<LignePrescription> listeLignesFormulaire = FXCollections.observableArrayList();

    @FXML
    private Label lblTitreFormulaire;

    @FXML
    private ComboBox<Medecin> cmbMedecin;
    @FXML
    private ComboBox<Patient> cmbPatient;
    @FXML
    private DatePicker dpDateEmission;
    @FXML
    private DatePicker dpDateValidite;

    @FXML
    private ComboBox<Medicament> cmbMedicament;
    @FXML
    private Spinner<Integer> spnQuantite;
    @FXML
    private Spinner<Integer> spnDuree; // Représente la durée graphique
    @FXML
    private TextField txtPosologie;
    @FXML
    private TextField txtInstructions;
    @FXML
    private Button btnAjouterLigne;

    @FXML
    private TableView<LignePrescription> tableViewPrescriptionsForm;
    @FXML
    private TableColumn<LignePrescription, Medicament> colFormMedicament;
    @FXML
    private TableColumn<LignePrescription, Integer> colFormQuantite;
    @FXML
    private TableColumn<LignePrescription, Integer> colFormDuree;
    @FXML
    private TableColumn<LignePrescription, String> colFormPosologie;
    @FXML
    private TableColumn<LignePrescription, String> colFormInstructions;

    @FXML
    private Button btnSupprimerLigne;
    @FXML
    private TextArea txtFormRemarques;
    @FXML
    private Button btnAnnuler;
    @FXML
    private Button btnEnregistrer;

    /**
     * Initialisation automatique par JavaFX au chargement de la vue ordonnance-form.fxml.
     */
    @FXML
    public void initialize() {
        colFormMedicament.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMedicament()));
        colFormQuantite.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantite()).asObject());

        colFormDuree.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDureeTraitement()).asObject());
        colFormPosologie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosologie()));
        colFormInstructions.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstructions()));

        tableViewPrescriptionsForm.setItems(listeLignesFormulaire);

        spnQuantite.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        spnDuree.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 7));

        lblErreur.setText("");
    }

    /**
     * Injecte les services indispensables à l'application et peuple les sélecteurs.
     */
    public void setServices(GestionOrdonnances go, GestionMedecins gm, GestionPatients gp, GestionMedicaments gmed) {
        this.gestionOrdonnances = go;
        this.gestionMedecins = gm;
        this.gestionPatients = gp;
        this.gestionMedicaments = gmed;

        // Remplissage à partir de vos classes d'ObservableList
        cmbMedecin.setItems(gestionMedecins.listerTousLesMedecins());
        cmbPatient.setItems(gestionPatients.listerTousLesPatients());
        cmbMedicament.setItems(gestionMedicaments.listerTousLesMedicaments());
    }

    /**
     * Permet de charger les données d'une ordonnance existante à modifier.
     */
    public void chargerOrdonnancePourModification(Ordonnance ordonnance) {
        this.ordonnanceEnCours = ordonnance;
        this.modeModification = true;

        lblTitreFormulaire.setText("Modifier l'Ordonnance n°" + ordonnance.getId());

        cmbMedecin.setValue(ordonnance.getMedecin());
        cmbPatient.setValue(ordonnance.getPatient());
        dpDateEmission.setValue(ordonnance.getDateEmission());
        dpDateValidite.setValue(ordonnance.getDateValidite());
        txtFormRemarques.setText(ordonnance.getRemarques());

        cmbMedecin.setDisable(true);
        cmbPatient.setDisable(true);

        listeLignesFormulaire.clear();
        listeLignesFormulaire.addAll(ordonnance.getPrescriptions());
    }

    /**
     * Déclenché lors du clic sur le bouton "Ajouter" de la zone médicament.
     */
    @FXML
    void ajouterLignePrescription(ActionEvent event) {
        Medicament medicament = cmbMedicament.getValue();
        int quantite = spnQuantite.getValue();
        String posologie = txtPosologie.getText().trim();
        int dureeTraitement = spnDuree.getValue(); // Récupère la valeur du Spinner duree
        String instructions = txtInstructions.getText().trim();

        if (medicament == null) {
            afficherAlerte("Saisie incorrecte", "Veuillez choisir un médicament.", AlertType.WARNING);
            return;
        }
        if (posologie.isEmpty()) {
            afficherAlerte("Saisie incorrecte", "Veuillez écrire une posologie.", AlertType.WARNING);
            return;
        }

        LignePrescription nouvelleLigne = new LignePrescription(medicament, quantite, posologie, dureeTraitement, instructions);

        listeLignesFormulaire.add(nouvelleLigne);

        cmbMedicament.setValue(null);
        txtPosologie.clear();
        txtInstructions.clear();
        spnQuantite.getValueFactory().setValue(1);
    }

    /**
     * Déclenché lors du clic sur "Retirer le médicament sélectionné".
     */
    @FXML
    void supprimerLignePrescription(ActionEvent event) {
        LignePrescription selection = tableViewPrescriptionsForm.getSelectionModel().getSelectedItem();
        if (selection != null) {
            listeLignesFormulaire.remove(selection);
        } else {
            afficherAlerte("Sélection manquante", "Sélectionnez un médicament à retirer du tableau.", AlertType.WARNING);
        }
    }

    private Parent rootListe;

    public void setRootListe(Parent rootListe) {
        this.rootListe = rootListe;
    }

    private void fermerFenetre() {
        Stage stage = (Stage) btnAnnuler.getScene().getWindow();
        stage.getScene().setRoot(rootListe);
        stage.setTitle("Cabinet Médical — Gestion des Ordonnances");
        stage.setWidth(900);
        stage.setHeight(600);
    }

    /**
     * Déclenché lors de la validation finale du formulaire ("Enregistrer").
     */
    @FXML
    void enregistrerOrdonnance(ActionEvent event) {
        Medecin medecin = cmbMedecin.getValue();
        Patient patient = cmbPatient.getValue();
        LocalDate dateValidite = dpDateValidite.getValue();
        String remarques = txtFormRemarques.getText().trim();

        if (medecin == null || patient == null || dateValidite == null) {
            afficherAlerte("Erreur", "Veuillez renseigner les champs obligatoires (médecin, patient, date de validité).", AlertType.ERROR);
            return;
        }
        if (listeLignesFormulaire.isEmpty()) {
            afficherAlerte("Erreur", "L'ordonnance doit contenir au moins un médicament.", AlertType.ERROR);
            return;
        }

        if (modeModification) {
            ordonnanceEnCours.setDateValidite(dateValidite);
            ordonnanceEnCours.setRemarques(remarques);

            ordonnanceEnCours.getPrescriptions().clear();
            ordonnanceEnCours.getPrescriptions().addAll(listeLignesFormulaire);

            afficherAlerte("Succès", "Ordonnance modifiée avec succès.", AlertType.INFORMATION);
        } else {
            Ordonnance nouvelle = gestionOrdonnances.creerOrdonnance(medecin, patient, dateValidite, remarques);

            nouvelle.getPrescriptions().addAll(listeLignesFormulaire);

            afficherAlerte("Succès", "Nouvelle ordonnance enregistrée.", AlertType.INFORMATION);
        }

        fermerFenetre();
    }

    @FXML
    void annulerSaisie(ActionEvent event) {
        fermerFenetre();
    }



    @FXML
    private Label lblErreur;

    private void afficherAlerte(String titre, String message, AlertType type) {
        if (type == AlertType.INFORMATION) {
            lblErreur.setStyle("-fx-text-fill: green; -fx-font-size: 13px;");
        } else {
            lblErreur.setStyle("-fx-text-fill: red; -fx-font-size: 13px;");
        }
        lblErreur.setText(message);
    }
}