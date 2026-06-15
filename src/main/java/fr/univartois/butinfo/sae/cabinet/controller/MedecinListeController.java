package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class MedecinListeController {

    @FXML private ListView<Medecin> listeMedecins;
    @FXML private Label labelNomComplet;
    @FXML private Label labelSpecialite;
    @FXML private Label labelTelephone;
    @FXML private Label labelEmail;
    @FXML private Label labelTarif;
    @FXML private Label labelNumeroOrdre;

    private GestionMedecins gestionMedecins;
    private ObservableList<Medecin> observableMedecins;
    private Stage stage;
    private Scene sceneAccueil;

    public void setGestionMedecins(GestionMedecins gestionMedecins) {
        this.gestionMedecins = gestionMedecins;
        observableMedecins = FXCollections.observableArrayList(gestionMedecins.listerTousLesMedecins());
        listeMedecins.setItems(observableMedecins);

        // Personnalisation de l'affichage dans la liste
        listeMedecins.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Medecin item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "Dr " + item.getNom() + " " + item.getPrenom());
            }
        });

        // Ecouteur de sélection pour mettre à jour les détails
        listeMedecins.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> afficherDetails(newVal));
    }

    private void afficherDetails(Medecin m) {
        if (m == null) {
            labelNomComplet.setText(""); labelSpecialite.setText(""); labelTelephone.setText("");
            labelEmail.setText(""); labelTarif.setText(""); labelNumeroOrdre.setText("");
        } else {
            labelNomComplet.setText("Dr " + m.getNom() + " " + m.getPrenom());
            labelSpecialite.setText(m.getSpecialite().getLibelle());
            labelTelephone.setText(m.getTelephone());
            labelEmail.setText(m.getEmail());
            labelTarif.setText(String.format("%.2f €/h", m.getTarifHoraire()));
            labelNumeroOrdre.setText(m.getNumeroOrdre());
        }
    }

    public void setStageEtScene(Stage stage, Scene scene) { this.stage = stage; }
    public void setSceneAccueil(Scene sceneAccueil) { this.sceneAccueil = sceneAccueil; }

    @FXML void addMedecin() { /* Logique d'ouverture du formulaire d'ajout */ }
    @FXML void modifMedecin() { /* Logique d'ouverture du formulaire de modif */ }

    @FXML
    void delMedecin() {
        Medecin selection = listeMedecins.getSelectionModel().getSelectedItem();
        if (selection != null) {
            gestionMedecins.supprimerMedecin(selection.getId());
            observableMedecins.remove(selection);
            afficherDetails(null);
        }
    }

    @FXML
    void retourAccueil() {
        if (sceneAccueil != null) {
            stage.setScene(sceneAccueil);
            stage.setTitle("Cabinet Médical — Accueil");
        }
    }
}