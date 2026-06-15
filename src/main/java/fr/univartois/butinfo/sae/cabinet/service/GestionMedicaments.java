package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service de gestion des médicaments disponibles dans le système.
 */
public class GestionMedicaments {
    private static final Logger LOGGER = LoggerFactory.getLogger("gestion-cabinet-medical-groupe-13");
    private final ObservableList<Medicament> medicaments;

    /**
     * Constructeur : initialise la liste vide.
     */
    public GestionMedicaments() {
        this.medicaments = FXCollections.observableArrayList();
    }

    // -------------------------
    // CRUD
    // -------------------------

    /**
     * Ajoute un médicament s'il n'existe pas déjà (même nom + principe actif).
     */
    public void ajouterMedicament(Medicament medicament) {
        if (medicaments.contains(medicament)) {
            LOGGER.debug("ERREUR : Ce médicament existe déjà.");
            return;
        }
        medicaments.add(medicament);
        LOGGER.info("Médicament ajouté : " + medicament.getNom());
    }

    /**
     * Recherche un médicament par son ID.
     */
    public Medicament rechercherParId(int id) {
        for (Medicament m : medicaments) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    /**
     * Recherche des médicaments dont le nom contient le terme donné.
     */
    public ObservableList<Medicament> rechercherParNom(String terme) {
        ObservableList<Medicament> resultats = FXCollections.observableArrayList();
        String termeLower = terme.toLowerCase();
        for (Medicament m : medicaments) {
            if (m.getNom().toLowerCase().contains(termeLower)) {
                resultats.add(m);
            }
        }
        return resultats;
    }


    /**
     * Supprime un médicament par son ID.
     */
    public boolean supprimerMedicament(int id) {
        Medicament m = rechercherParId(id);
        if (m == null) {
            LOGGER.debug("ERREUR : Médicament ID=" + id + " introuvable.");
            return false;
        }
        medicaments.remove(m);
        LOGGER.info("Médicament supprimé : " + m.getNom());
        return true;
    }

    /**
     * Retourne la liste complète des médicaments.
     */
    public ObservableList<Medicament> listerTousLesMedicaments() {
        return this.medicaments;
    }

    /**
     * Retourne le nombre total de médicaments.
     */
    public int getNombreMedicaments() {
        return medicaments.size();
    }


}
