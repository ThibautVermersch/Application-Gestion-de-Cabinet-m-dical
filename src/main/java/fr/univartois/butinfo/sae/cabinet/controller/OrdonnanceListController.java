package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.LignePrescription;
import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.modele.Ordonnance;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OrdonnanceListController {

    private GestionOrdonnances gestionOrdonnances;
    private GestionMedecins gestionMedecins;
    private GestionPatients gestionPatients;
    private GestionMedicaments gestionMedicaments;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private Button btnRetourAccueil;

    @FXML
    private ListView<Ordonnance> listViewOrdonnances;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;

    @FXML
    private Label lblId;
    @FXML
    private Label lblMedecin;
    @FXML
    private Label lblPatient;
    @FXML
    private Label lblDateEmission;
    @FXML
    private Label lblDateValidite;
    @FXML
    private Label lblPrixTotal;

    @FXML
    private TableView<LignePrescription> tableViewPrescriptions;
    @FXML
    private TableColumn<LignePrescription, Medicament> colMedicament;
    @FXML
    private TableColumn<LignePrescription, Integer> colQuantite;
    @FXML
    private TableColumn<LignePrescription, Integer> colDuree;
    @FXML
    private TableColumn<LignePrescription, String> colPosologie;
    @FXML
    private TableColumn<LignePrescription, String> colInstructions;

    @FXML
    private TextArea txtRemarques;

    /**
     * Initialisation automatique par JavaFX.
     */
    @FXML
    public void initialize() {
        colMedicament.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getMedicament()));
        colQuantite.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantite()).asObject());
        colDuree.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDureeTraitement()).asObject());
        colPosologie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosologie()));
        colInstructions.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstructions()));

        listViewOrdonnances.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            lblErreur.setText("");
            afficherDetailsOrdonnance(newValue);
        });

        afficherDetailsOrdonnance(null);
    }

    /**
     * Injection des services de gestion.
     */
    public void setServices(GestionOrdonnances go, GestionMedecins gm, GestionPatients gp, GestionMedicaments gmed) {
        this.gestionOrdonnances = go;
        this.gestionMedecins = gm;
        this.gestionPatients = gp;
        this.gestionMedicaments = gmed;

        listViewOrdonnances.setItems(gestionOrdonnances.listerToutesLesOrdonnances());
    }

    /**
     * Met à jour l'affichage détaillé de droite.
     */
    private void afficherDetailsOrdonnance(Ordonnance ordonnance) {
        if (ordonnance != null) {
            lblId.setText(String.valueOf(ordonnance.getId()));
            lblMedecin.setText("Dr " + ordonnance.getMedecin().getNomComplet());
            lblPatient.setText(ordonnance.getPatient().getNomComplet());
            lblDateEmission.setText(ordonnance.getDateEmission().format(dateFormatter));
            lblDateValidite.setText(ordonnance.getDateValidite().format(dateFormatter));

            lblPrixTotal.setText(String.format("%.2f €", ordonnance.calculerPrix()));
            txtRemarques.setText(ordonnance.getRemarques());

            tableViewPrescriptions.setItems(FXCollections.observableArrayList(ordonnance.getPrescriptions()));
        } else {
            lblId.setText("-");
            lblMedecin.setText("-");
            lblPatient.setText("-");
            lblDateEmission.setText("-");
            lblDateValidite.setText("-");
            lblPrixTotal.setText("0.00 €");
            txtRemarques.clear();
            tableViewPrescriptions.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    void ouvrirFormulaireAjout(ActionEvent event) {
        ouvrirPopupFormulaire(null);
    }

    @FXML
    void ouvrirFormulaireModification(ActionEvent event) {
        Ordonnance selection = listViewOrdonnances.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une ordonnance à modifier.", AlertType.WARNING);
            return;
        }
        ouvrirPopupFormulaire(selection);
    }

    @FXML
    void supprimerOrdonnance(ActionEvent event) {
        Ordonnance selection = listViewOrdonnances.getSelectionModel().getSelectedItem();
        if (selection == null) {
            afficherAlerte("Sélection requise", "Veuillez sélectionner une ordonnance à supprimer.", AlertType.WARNING);
            return;
        }
        gestionOrdonnances.listerToutesLesOrdonnances().remove(selection);
        afficherDetailsOrdonnance(null);
    }

    /**
     * Gère l'ouverture du formulaire sans dépendre d'une méthode de modification interne à Ordonnance.java
     */
    private void ouvrirPopupFormulaire(Ordonnance ordonnanceAModifier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ordonnance-form.fxml"));
            Parent root = loader.load();

            OrdonnanceFormController formController = loader.getController();
            formController.setServices(gestionOrdonnances, gestionMedecins, gestionPatients, gestionMedicaments);

            Scene sceneCourante = btnAjouter.getScene();
            Parent rootListe = sceneCourante.getRoot();

            formController.setRootListe(rootListe);

            if (ordonnanceAModifier != null) {
                formController.chargerOrdonnancePourModification(ordonnanceAModifier);
            }

            Stage stage = (Stage) sceneCourante.getWindow();
            sceneCourante.setRoot(root);
            stage.setTitle(ordonnanceAModifier == null ? "Créer une ordonnance" : "Modifier l'ordonnance");

        } catch (IOException e) {
            afficherAlerte("Erreur système", "Impossible de charger le formulaire : " + e.getMessage(), AlertType.ERROR);
        }
    }

    private Parent rootAccueil;

    public void setRootAccueil(Parent rootAccueil) {
        this.rootAccueil = rootAccueil;
    }
    @FXML
    void retourAccueil(ActionEvent event) {
        Stage stage = (Stage) btnRetourAccueil.getScene().getWindow();
        stage.getScene().setRoot(rootAccueil);
        stage.setTitle("Cabinet Médical — Accueil");
        stage.setWidth(450);
        stage.setHeight(350);
    }




    @FXML
    private Label lblErreur;

    private void afficherAlerte(String titre, String message, AlertType type) {
        if (type == AlertType.INFORMATION) {
            lblErreur.setStyle("-fx-text-fill: green; -fx-font-size: 13px;");
        } else if (type == AlertType.WARNING) {
            lblErreur.setStyle("-fx-text-fill: #e80000; -fx-font-size: 13px;");
        } else {
            lblErreur.setStyle("-fx-text-fill: red; -fx-font-size: 13px;");
        }
        lblErreur.setText(message);
    }


    private Scene sceneAccueil;

    public void setSceneAccueil(Scene sceneAccueil) {
        this.sceneAccueil = sceneAccueil;
    }
}