package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

/**
 * Service de gestion des médicaments disponibles dans le système.
 * @version 1.1
 */
public class GestionMedicaments {
    private static final Logger LOGGER = Logger.getLogger("fr.univartois.butinfo.sae.cabinet.service");
    private final List<Medicament> medicaments;

    /**
     * Constructeur : initialise la liste vide.
     */
    public GestionMedicaments() {
        this.medicaments = new ArrayList<>();
    }

    // -------------------------
    // CRUD
    // -------------------------

    /**
     * Ajoute un médicament s'il n'existe pas déjà (même nom + principe actif).
     *
     * @param medicament le médicament à ajouter.
     */
    public void ajouterMedicament(Medicament medicament) {
        if (medicaments.contains(medicament)) {
            LOGGER.info("ERREUR : Ce médicament existe déjà.");
            return;
        }
        medicaments.add(medicament);
        LOGGER.info("Médicament ajouté : " + medicament.getNom());
    }

    /**
     * Recherche un médicament par son ID.
     *
     * @param id l'identifiant du médicament recherché.
     * @return le médicament correspondant, ou {@code null} si introuvable.
     */
    public Medicament rechercherParId(int id) {
        for (Medicament m : medicaments) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    /**
     * Recherche des médicaments dont le nom contient le terme donné.
     *
     * @param terme le terme à rechercher dans le nom du médicament.
     * @return la liste des médicaments correspondants.
     */
    public List<Medicament> rechercherParNom(String terme) {
        List<Medicament> resultats = new ArrayList<>();
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
     *
     * @param id l'identifiant du médicament à supprimer.
     * @return vrai ou faux si le médicament a été supprimé, {@code false} si introuvable.
     */
    public boolean supprimerMedicament(int id) {
        Medicament m = rechercherParId(id);
        if (m == null) {
            LOGGER.info("ERREUR : Médicament ID=" + id + " introuvable.");
            return false;
        }
        medicaments.remove(m);
        LOGGER.info("Médicament supprimé : " + m.getNom());
        return true;
    }

    /**
     * Retourne la liste complète des médicaments.
     *
     * @return la liste de tous les médicaments enregistrés.
     */
    public List<Medicament> listerTousLesMedicaments() {
        return new ArrayList<>(medicaments);
    }

    /**
     * Retourne le nombre total de médicaments.
     *
     * @return le nombre de médicaments enregistrés.
     */
    public int getNombreMedicaments() {
        return medicaments.size();
    }


}
