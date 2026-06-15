package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class MedicamentControleur {

	@FXML private ListView<Medicament> listeMedicaments;
	@FXML private Label labelNom;
	@FXML private Label labelPrix;
	@FXML private Label labelDescription;
	@FXML private Label labelId;

	private GestionMedicaments gestionMedicaments;
	private ObservableList<Medicament> observableMedicaments;
	private Stage stage;
	private Scene sceneAccueil;

	public void setGestionMedicaments(GestionMedicaments gestionMedicaments) {
		this.gestionMedicaments = gestionMedicaments;
		observableMedicaments = FXCollections.observableArrayList(gestionMedicaments.listerTousLesMedicaments());
		listeMedicaments.setItems(observableMedicaments);

		// Affichage personnalisé dans la liste
		listeMedicaments.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
			@Override
			protected void updateItem(Medicament item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty || item == null ? null : item.getNom());
			}
		});

		// Ecouteur de sélection
		listeMedicaments.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> afficherDetails(newVal));
	}

	private void afficherDetails(Medicament m) {
		if (m == null) {
			labelNom.setText(""); labelPrix.setText(""); labelDescription.setText(""); labelId.setText("");
		} else {
			labelNom.setText(m.getNom());
			labelPrix.setText(String.format("%.2f €", m.getPrix()));
			labelDescription.setText(m.getDescription());
			labelId.setText(String.valueOf(m.getId()));
		}
	}

	public void setStage(Stage stage) { this.stage = stage; }
	public void setSceneAccueil(Scene scene) { this.sceneAccueil = scene; }

	@FXML void ajouterMedicament() { System.out.println("Ouvrir formulaire ajout"); }
	@FXML void modifierMedicament() { System.out.println("Ouvrir formulaire modification"); }

	@FXML
	void supprimerMedicament() {
		Medicament selection = listeMedicaments.getSelectionModel().getSelectedItem();
		if (selection != null) {
			gestionMedicaments.supprimerMedicament(selection.getId());
			observableMedicaments.remove(selection);
			afficherDetails(null);
		}
	}

	@FXML
	void retourAccueil() {
		if (sceneAccueil != null) {
			stage.setScene(sceneAccueil);
			stage.setTitle("Accueil");
		}
	}
}