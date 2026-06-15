package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des patients.
 * Fournit les opérations CRUD (Créer, Lire, Mettre à jour, Supprimer).
 */
public class GestionPatients {
    private static final Logger LOGGER = LoggerFactory.getLogger("gestion-cabinet-medical-groupe-13");
    // Liste interne des patients enregistrés
    private final ObservableList<Patient> patients;

    /**
     * Constructeur : initialise la liste vide.
     */
    public GestionPatients() {
        this.patients = FXCollections.observableArrayList();
    }

    // -------------------------
    // CRUD
    // -------------------------

    /**
     * Ajoute un patient si son numéro de sécu n'existe pas déjà.
     *
     * @param patient le patient à ajouter.
     */
    public void ajouterPatient(Patient patient) {
        if (rechercherParNumeroSecu(patient.getNumeroSecu()) != null) {
            LOGGER.info("ERREUR : Un patient avec ce numéro de sécu existe déjà.");
            return;
        }
        patients.add(patient);
        LOGGER.info("Patient ajouté : " + patient.getNomComplet());
    }

    /**
     * Retourne un patient par son ID, ou null si introuvable.
     *
     * @param id l'identifiant du patient recherché.
     * @return le patient correspondant, ou {@code null} si introuvable.
     */
    public Patient rechercherParId(int id) {
        for (Patient p : patients) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * Retourne un patient par son numéro de sécurité sociale.
     *
     * @param numeroSecu le numéro de sécurité sociale recherché.
     * @return le patient correspondant, ou {@code null} si introuvable.
     */
    public Patient rechercherParNumeroSecu(String numeroSecu) {
        for (Patient p : patients) {
            if (p.getNumeroSecu().equalsIgnoreCase(numeroSecu)) return p;
        }
        return null;
    }

    /**
     * Recherche des patients dont le nom ou prénom contient le terme donné (insensible à la casse).
     *
     * @param terme le terme à rechercher dans le nom ou le prénom.
     * @return la liste des patients correspondants.
     */
    public ObservableList<Patient> rechercherParNom(String terme) {
        ObservableList<Patient> resultats = FXCollections.observableArrayList();
        String termeLower = terme.toLowerCase();
        for (Patient p : patients) {
            if (p.getNom().toLowerCase().contains(termeLower) ||
                p.getPrenom().toLowerCase().contains(termeLower)) {
                resultats.add(p);
            }
        }
        return resultats;
    }

    /**
     * Met à jour les informations d'un patient (sauf ID et numéro de sécu).
     *
     * @param id l'identifiant du patient à mettre à jour.
     * @param nouveauNom le nouveau nom du patient.
     * @param nouveauPrenom le nouveau prénom du patient.
     * @param nouveauTel le nouveau numéro de téléphone du patient.
     * @param nouveauEmail la nouvelle adresse e-mail du patient.
     * @param nouvelleAdresse la nouvelle adresse postale du patient.
     * @param nouveauTauxRemboursement le nouveau taux de remboursement du patient.
     */
    public void mettreAJourPatient(int id, String nouveauNom, String nouveauPrenom,
                                       String nouveauTel, String nouveauEmail,
                                       String nouvelleAdresse, double nouveauTauxRemboursement) {
        Patient p = rechercherParId(id);
        if (p == null) {
            LOGGER.debug("ERREUR : Patient ID=" + id + " introuvable.");
            return;
        }
        p.setNom(nouveauNom);
        p.setPrenom(nouveauPrenom);
        p.setTelephone(nouveauTel);
        p.setEmail(nouveauEmail);
        p.setAdresse(nouvelleAdresse);
        p.setTauxRemboursement(nouveauTauxRemboursement);
        LOGGER.info("Patient mis à jour : " + p.getNomComplet());
    }

    /**
     * Supprime un patient par son ID.
     * @param id L'id du patient à supprimer
     */
    public void supprimerPatient(int id) {
        Patient p = rechercherParId(id);
        if (p == null) {
            LOGGER.debug("ERREUR : Patient ID=" + id + " introuvable.");
            return;
        }
        patients.remove(p);
        LOGGER.info("Patient supprimé : " + p.getNomComplet());
    }

    /**
     * Retourne la liste complète des patients (copie défensive).
     * @return new ArrayList<>(patients) Une liste de patients.
     */
    public ObservableList<Patient> listerTousLesPatients() {
        return this.patients;
    }

    /**
     * Retourne le nombre total de patients.
     * @return patients.size()
     */
    public int getNombrePatients() {
        return patients.size();
    }


}
