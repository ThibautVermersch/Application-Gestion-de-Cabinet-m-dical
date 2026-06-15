package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.Patient;
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
    private final List<Patient> patients;

    /**
     * Constructeur : initialise la liste vide.
     */
    public GestionPatients() {
        this.patients = new ArrayList<>();
    }

    // -------------------------
    // CRUD
    // -------------------------

    /**
     * Ajoute un patient si son numéro de sécu n'existe pas déjà.
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
     */
    public Patient rechercherParId(int id) {
        for (Patient p : patients) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    /**
     * Retourne un patient par son numéro de sécurité sociale.
     */
    public Patient rechercherParNumeroSecu(String numeroSecu) {
        for (Patient p : patients) {
            if (p.getNumeroSecu().equalsIgnoreCase(numeroSecu)) return p;
        }
        return null;
    }

    /**
     * Recherche des patients dont le nom ou prénom contient le terme donné (insensible à la casse).
     */
    public List<Patient> rechercherParNom(String terme) {
        List<Patient> resultats = new ArrayList<>();
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
     */
    public void mettreAJourPatient(int id, String nouveauNom, String nouveauPrenom,
                                       String nouveauTel, String nouveauEmail,
                                       String nouvelleAdresse, double nouveauTauxRemboursement) {
        Patient p = rechercherParId(id);
        if (p == null) {
            LOGGER.info("ERREUR : Patient ID=" + id + " introuvable.");
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
     */
    public void supprimerPatient(int id) {
        Patient p = rechercherParId(id);
        if (p == null) {
            System.out.println("ERREUR : Patient ID=" + id + " introuvable.");
            return;
        }
        patients.remove(p);
        LOGGER.info("Patient supprimé : " + p.getNomComplet());
    }

    /**
     * Retourne la liste complète des patients (copie défensive).
     */
    public List<Patient> listerTousLesPatients() {
        return new ArrayList<>(patients);
    }

    /**
     * Retourne le nombre total de patients.
     */
    public int getNombrePatients() {
        return patients.size();
    }


}
