package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.LignePrescription;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import fr.univartois.butinfo.sae.cabinet.modele.Ordonnance;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Service de gestion des ordonnances médicales.
 * @version 1.1
 */
public class GestionOrdonnances {

	private static final Logger LOGGER = LogManager.getLogger("fr.univartois.butinfo.sae.cabinet.service");
	
    private final List<Ordonnance> ordonnances;

    /**
     * Constructeur : initialise la liste vide.
     */
    public GestionOrdonnances() {
        this.ordonnances = new ArrayList<>();
    }

    // -------------------------
    // Gestion des ordonnances
    // -------------------------

    /**
     * Crée une nouvelle ordonnance vide (sans prescriptions) et l'enregistre.
     *
     * @param medecin le médecin qui prescrit l'ordonnance.
     * @param patient le patient concerné par l'ordonnance.
     * @param dateValidite la date de validité de l'ordonnance.
     * @param remarques des remarques éventuelles sur l'ordonnance.
     * @return l'ordonnance créée.
     */
    public Ordonnance creerOrdonnance(Medecin medecin, Patient patient,
                                       LocalDate dateValidite, String remarques) {
        Ordonnance ordo = new Ordonnance(medecin, patient, dateValidite, remarques);
        ordonnances.add(ordo);
        LOGGER.info("Ordonnance créée (ID={}) pour {} par Dr {}", ordo.getId(), patient.getNomComplet(), medecin.getNomComplet());
        //System.out.println("Ordonnance créée (ID=" + ordo.getId() + ") pour " +
        //                   patient.getNomComplet() + " par Dr " + medecin.getNomComplet());
        return ordo;
    }

    /**
     * Ajoute une ligne de prescription à une ordonnance existante.
     *
     * @param ordonnanceId l'identifiant de l'ordonnance concernée.
     * @param medicament le médicament prescrit.
     * @param quantite la quantité prescrite.
     * @param posologie la posologie associée à la prescription.
     * @param dureeJours la durée du traitement en jours.
     * @param instructions des instructions complémentaires pour la prise du médicament.
     */
    public void ajouterPrescription(int ordonnanceId, Medicament medicament, int quantite,
                                        String posologie, int dureeJours, String instructions) {
        Ordonnance ordo = rechercherParId(ordonnanceId);
        if (ordo == null) {
            LOGGER.debug("ERREUR : Ordonnance ID=" + ordonnanceId + " introuvable.");
            return;
        }
        LignePrescription ligne = new LignePrescription(medicament, quantite, posologie, dureeJours, instructions);
        ordo.ajouterPrescription(ligne);
        LOGGER.info("Prescription ajoutée : " + medicament.getNom() +
                           " → ordonnance ID=" + ordonnanceId);
    }

    /**
     * Recherche une ordonnance par son ID.
     *
     * @param id l'identifiant de l'ordonnance recherchée.
     * @return l'ordonnance correspondante, ou {@code null} si introuvable.
     */
    public Ordonnance rechercherParId(int id) {
        for (Ordonnance o : ordonnances) {
            if (o.getId() == id) return o;
        }
        return null;
    }

    /**
     * Retourne toutes les ordonnances d'un patient.
     *
     * @param patient le patient concerné.
     * @return la liste des ordonnances associées à ce patient.
     */
    public ObservableList<Ordonnance> getOrdonnancesPatient(Patient patient) {
        ObservableList<Ordonnance> resultats = FXCollections.observableArrayList();
        for (Ordonnance o : ordonnances) {
            if (o.getPatient().equals(patient)) resultats.add(o);
        }
        return resultats;
    }

    /**
     * Retourne toutes les ordonnances émises par un médecin.
     *
     * @param medecin le médecin concerné.
     * @return la liste des ordonnances émises par ce médecin.
     */
    public ObservableList<Ordonnance> getOrdonnancesMedecin(Medecin medecin) {
        ObservableList<Ordonnance> resultats = FXCollections.observableArrayList();
        for (Ordonnance o : ordonnances) {
            if (o.getMedecin().equals(medecin)) resultats.add(o);
        }
        return resultats;
    }

    /**
     * Retourne uniquement les ordonnances encore valides à ce jour.
     *
     * @return la liste des ordonnances valides.
     */
    public ObservableList<Ordonnance> getOrdonnancesValides() {
        ObservableList<Ordonnance> resultats =FXCollections.observableArrayList();
        for (Ordonnance o : ordonnances) {
            if (o.estValide()) resultats.add(o);
        }
        return resultats;
    }

    /**
     * Retourne la liste complète des ordonnances.
     *
     * @return la liste de toutes les ordonnances enregistrées.
     */
    public List<Ordonnance> listerToutesLesOrdonnances() {
        return new ArrayList<>(ordonnances);
    }

    /**
     * Retourne le nombre total d'ordonnances.
     *
     * @return le nombre d'ordonnances enregistrées.
     */
    public int getNombreOrdonnances() {
        return ordonnances.size();
    }

}
