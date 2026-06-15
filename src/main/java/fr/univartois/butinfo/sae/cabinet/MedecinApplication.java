package fr.univartois.butinfo.sae.cabinet;

import fr.univartois.butinfo.sae.cabinet.controller.MedecinListeController;
import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale pour tester UNIQUEMENT la vue des médecins.
 *
 * Tu peux la lancer en changeant temporairement le mainClass dans build.gradle :
 *   mainClass = 'fr.univartois.butinfo.sae.cabinet.MedecinApplication'
 *
 * Ou en créant une Run Configuration dans IntelliJ.
 */
public class MedecinApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ── 1. Créer des données de test ──────────────────────────────────────
        GestionMedecins gestionMedecins = new GestionMedecins();

        gestionMedecins.ajouterMedecin(new Medecin(
                "Dupont", "Jean",
                "03 12 34 56 78", "j.dupont@cabinet.fr",
                "ORD-001", Specialite.GENERALISTE, 30.0
        ));
        gestionMedecins.ajouterMedecin(new Medecin(
                "Martin", "Sophie",
                "03 98 76 54 32", "s.martin@cabinet.fr",
                "ORD-002", Specialite.CARDIOLOGUE, 50.0
        ));
        gestionMedecins.ajouterMedecin(new Medecin(
                "Bernard", "Lucie",
                "03 67 89 12 34", "l.bernard@cabinet.fr",
                "ORD-003", Specialite.PEDIATRE, 40.0
        ));

        // ── 2. Charger la vue FXML ────────────────────────────────────────────
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fr/univartois/butinfo/sae/cabinet/medecin/medecin.fxml"));
        Scene scene = new Scene(loader.load());

        // ── 3. Récupérer le contrôleur et lui donner les données ──────────────
        MedecinListeController controleur = loader.getController();
        controleur.setGestionMedecins(gestionMedecins);
        controleur.setStageEtScene(stage, scene);
        // Pas de scène d'accueil ici car on teste la vue seule
        controleur.setSceneAccueil(scene); // on pointe vers soi-même

        // ── 4. Afficher la fenêtre ────────────────────────────────────────────
        stage.setScene(scene);
        stage.setTitle("Test — Gestion des Médecins");
        stage.setWidth(700);
        stage.setHeight(500);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}