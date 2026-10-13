package fr.univartois.butinfo.sae.cabinet;

import fr.univartois.butinfo.sae.cabinet.controller.OrdonnanceListController;
import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Classe principale pour tester UNIQUEMENT la vue des ordonnances.
 *
 * Basée sur la structure rigoureuse de PatientApplication.java.
 */
public class OrdonnanceApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        GestionMedecins gestionMedecins = new GestionMedecins();
        GestionPatients gestionPatients = new GestionPatients();
        GestionMedicaments gestionMedicaments = new GestionMedicaments();
        GestionOrdonnances gestionOrdonnances = new GestionOrdonnances();

        gestionMedecins.ajouterMedecin(new Medecin("Dupont", "Jean", "0312345678", "j.dupont@cabinet.fr",
                "ORD-001", Specialite.GENERALISTE, 30.0));
        gestionMedecins.ajouterMedecin(new Medecin("Martin", "Sophie", "0398765432", "s.martin@cabinet.fr",
                "ORD-002", Specialite.CARDIOLOGUE, 50.0));
        gestionMedecins.ajouterMedecin(new Medecin("Bernard", "Lucie", "0367891234", "l.bernard@cabinet.fr",
                "ORD-003", Specialite.PEDIATRE, 40.0));

        gestionPatients.ajouterPatient(new Patient("Leblanc", "Paul", "0612345678", "p.leblanc@mail.fr",
                "12 rue des Lilas, Arras", LocalDate.of(1985, 3, 22), "185036562012345", .50));
        gestionPatients.ajouterPatient(new Patient("Moreau", "Claire", "0687654321", "c.moreau@mail.fr",
                "8 avenue Foch, Lens", LocalDate.of(1995, 7, 15), "295076210054321"));
        gestionPatients.ajouterPatient(new Patient("Petit", "Antoine", "0655443322", "a.petit@mail.fr",
                "3 impasse du Moulin, Douai", LocalDate.of(2010, 11, 5), "210116210011122", .6));

        gestionMedicaments.ajouterMedicament(new Medicament("Doliprane 1000mg", "Antalgique et antipyrétique", 5.5));
        gestionMedicaments.ajouterMedicament(new Medicament("Amoxicilline 500mg", "Antibiotique à large spectre", 13.25));
        gestionMedicaments.ajouterMedicament(new Medicament("Ibuprofène 400mg", "Anti-inflammatoire non stéroïdien", 43.12));
        gestionMedicaments.ajouterMedicament(new Medicament("Lisinopril 10mg", "Inhibiteur de l'enzyme de conversion", 24.56));



        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/fr/univartois/butinfo/sae/cabinet/view/ordonnance-liste.fxml"));
        Scene scene = new Scene(loader.load());

        OrdonnanceListController controleur = loader.getController();

        controleur.setServices(gestionOrdonnances, gestionMedecins, gestionPatients, gestionMedicaments);

        stage.setScene(scene);
        stage.setTitle("Test — Gestion des Ordonnances");
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}