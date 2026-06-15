package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.enumeration.StatutRendezVous;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.modele.RendezVous;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service de gestion des rendez-vous médicaux.
 */
public class GestionRendezVous {

    private static final Logger LOGGER = Logger.getLogger("fr.univartois.butinfo.sae.cabinet.service");

    private final List<RendezVous> rendezVous;

    /**
     * Constructeur : initialise la liste vide.
     */
    public GestionRendezVous() {
        this.rendezVous = new ArrayList<>();
    }

    // -------------------------
    // Gestion des RDV
    // -------------------------

    /**
     * Prend un rendez-vous si le créneau du médecin est libre.
     * @return le rendez-vous créé, ou null si le créneau est pris
     */
    public RendezVous prendreRendezVous(Patient patient, Medecin medecin,
                                         LocalDateTime dateHeure, int dureeMinutes, String motif) {
        // Vérification de disponibilité du médecin
        if (!estDisponible(medecin, dateHeure)) {
            LOGGER.info("ERREUR : Le Dr " + medecin.getNomComplet() +
                               " n'est pas disponible à ce créneau.");
            return null;
        }
        RendezVous rdv = new RendezVous(patient, medecin, dateHeure, dureeMinutes, motif);
        rendezVous.add(rdv);
        LOGGER.info("Rendez-vous créé : " + rdv);
        return rdv;
    }

    /**
     * Annule un rendez-vous par son ID.
     */
    public void annulerRendezVous(int id) {
        RendezVous rdv = rechercherParId(id);
        if (rdv == null) {
            LOGGER.info("ERREUR : Rendez-vous ID=" + id + " introuvable.");
            return;
        }
        rdv.annuler();
        LOGGER.info("Rendez-vous annulé : ID=" + id);
    }

    /**
     * Confirme un rendez-vous par son ID.
     */
    public void confirmerRendezVous(int id) {
        RendezVous rdv = rechercherParId(id);
        if (rdv == null) {
            LOGGER.info("ERREUR : Rendez-vous ID=" + id + " introuvable.");
            return;
        }
        rdv.confirmer();
        LOGGER.info("Rendez-vous confirmé : ID=" + id);
    }

    /**
     * Marque un rendez-vous comme terminé.
     */
    public boolean terminerRendezVous(int id) {
        RendezVous rdv = rechercherParId(id);
        if (rdv == null) {
            LOGGER.info("ERREUR : Rendez-vous ID=" + id + " introuvable.");
            return false;
        }
        rdv.terminer();
        LOGGER.info("Rendez-vous terminé : ID=" + id);
        return true;
    }

    // -------------------------
    // Recherche
    // -------------------------

    /**
     * Recherche un rendez-vous par son ID.
     */
    public RendezVous rechercherParId(int id) {
        for (RendezVous rdv : rendezVous) {
            if (rdv.getId() == id) return rdv;
        }
        return null;
    }

    /**
     * Retourne tous les rendez-vous d'un patient donné.
     */
    public List<RendezVous> getRendezVousPatient(Patient patient) {
        List<RendezVous> resultats = new ArrayList<>();
        for (RendezVous rdv : rendezVous) {
            if (rdv.getPatient().equals(patient)) resultats.add(rdv);
        }
        return resultats;
    }

    /**
     * Retourne tous les rendez-vous d'un médecin donné.
     */
    public List<RendezVous> getRendezVousMedecin(Medecin medecin) {
        List<RendezVous> resultats = new ArrayList<>();
        for (RendezVous rdv : rendezVous) {
            if (rdv.getMedecin().equals(medecin)) resultats.add(rdv);
        }
        return resultats;
    }

    /**
     * Retourne les rendez-vous filtrés par statut.
     */
    public List<RendezVous> getRendezVousParStatut(StatutRendezVous statut) {
        List<RendezVous> resultats = new ArrayList<>();
        for (RendezVous rdv : rendezVous) {
            if (rdv.getStatut() == statut) resultats.add(rdv);
        }
        return resultats;
    }

    /**
     * Vérifie si un médecin est disponible à une date/heure donnée.
     * Un médecin est indisponible si un RDV non annulé existe à la même heure.
     */
    public boolean estDisponible(Medecin medecin, LocalDateTime dateHeure) {
        for (RendezVous rdv : rendezVous) {
            if (rdv.getMedecin().equals(medecin) &&
                rdv.getDateHeure().equals(dateHeure) &&
                rdv.getStatut() != StatutRendezVous.ANNULE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retourne la liste complète des rendez-vous.
     */
    public List<RendezVous> listerTousLesRendezVous() {
        return new ArrayList<>(rendezVous);
    }

    /**
     * Retourne le nombre total de rendez-vous.
     */
    public int getNombreRendezVous() {
        return rendezVous.size();
    }

}
