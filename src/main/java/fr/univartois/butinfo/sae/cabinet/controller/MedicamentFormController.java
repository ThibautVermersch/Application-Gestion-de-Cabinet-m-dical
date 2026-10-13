package fr.univartois.butinfo.sae.cabinet.controller;

import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MedicamentFormController {

    @FXML private Label labelTitre;
    @FXML private TextField champNom;
    @FXML private TextField champPrix;
    @FXML private TextField champDescription;
    @FXML private Label labelErreur;

    private GestionMedicaments gestionMedicaments;
    private ObservableList<Medicament> observableMedicaments;
    private ListView<Medicament> listeView;
    private Medicament medicamentAModifier;
    private Stage stage;
    private Scene sceneListe;

    public void setGestionMedicaments(GestionMedicaments gestionMedicaments) {
        this.gestionMedicaments = gestionMedicaments;
    }

    public void setObservableMedicaments(ObservableList<Medicament> observableMedicaments) {
        this.observableMedicaments = observableMedicaments;
    }

    public void setListeView(ListView<Medicament> listeView) {
        this.listeView = listeView;
    }

    public void setStageEtSceneListe(Stage stage, Scene sceneListe) {
        this.stage = stage;
        this.sceneListe = sceneListe;
    }

    public void setMedicamentAModifier(Medicament medicament) {
        this.medicamentAModifier = medicament;
        labelTitre.setText("Modifier un médicament");
        champNom.setText(medicament.getNom());
        champPrix.setText(String.valueOf(medicament.getPrix()));
        champDescription.setText(medicament.getDescription());
    }

    @FXML
    private void valider() {
        labelErreur.setText("");

        String nom = champNom.getText().trim();
        String prixStr = champPrix.getText().trim();
        String description = champDescription.getText().trim();

        if (nom.isEmpty() || prixStr.isEmpty() || description.isEmpty()) {
            labelErreur.setText("Tous les champs sont obligatoires.");
            return;
        }

        double prix;
        try {
            prix = Double.parseDouble(prixStr);
            if (prix < 0) {
                labelErreur.setText("Le prix ne peut pas être négatif.");
                return;
            }
        } catch (NumberFormatException e) {
            labelErreur.setText("Prix invalide. Saisissez un nombre (ex: 5.99).");
            return;
        }

        if (medicamentAModifier == null) {
            
            Medicament nouveau = new Medicament(nom, description, prix);
            gestionMedicaments.ajouterMedicament(nouveau);
            observableMedicaments.add(nouveau);
        } else {
            medicamentAModifier.setNom(nom);
            medicamentAModifier.setPrix(prix);
            medicamentAModifier.setDescription(description);
            listeView.refresh();
        }

        retourListe();
    }

    @FXML
    private void annuler() {
        retourListe();
    }

    private void retourListe() {
        stage.setScene(sceneListe);
        stage.setTitle("Gestion des Médicaments");
    }
}