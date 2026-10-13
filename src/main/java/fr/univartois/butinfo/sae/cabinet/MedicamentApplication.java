package fr.univartois.butinfo.sae.cabinet;

import fr.univartois.butinfo.sae.cabinet.controller.MedicamentControleur;
import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MedicamentApplication extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		// 1. Données de test
		GestionMedicaments service = new GestionMedicaments();
		service.ajouterMedicament(new Medicament("Doliprane", "Paracétamol 500mg", 2.10));
		service.ajouterMedicament(new Medicament("Advil", "Ibuprofène 200mg", 4.50));
		service.ajouterMedicament(new Medicament("Spasfon", "Antispasmodique", 3.20));

		// 2. Chargement FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/univartois/butinfo/sae/ressources/view/vueMedicaments.fxml"));
		Scene scene = new Scene(loader.load());

		// 3. Init Contrôleur
		MedicamentControleur ctrl = loader.getController();
		ctrl.setGestionMedicaments(service);
		ctrl.setStage(stage);
		ctrl.setSceneAccueil(scene); // Test seul : retour vers soi-même

		// 4. Affichage
		stage.setScene(scene);
		stage.setTitle("Test — Gestion des Médicaments");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}