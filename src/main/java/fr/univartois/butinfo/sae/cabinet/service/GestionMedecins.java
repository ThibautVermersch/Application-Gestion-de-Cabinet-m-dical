package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Service de gestion des médecins du cabinet.
 * @version 1.1
 */
public class GestionMedecins {
    private static final Logger LOGGER = LoggerFactory.getLogger("gestion-cabinet-medical-groupe-13");

    private final List<Medecin> medecins;

    /**
     * Constructeur : initialise la liste vide.
     */
    public GestionMedecins() {
        this.medecins = new ArrayList<>();
    }

    // -------------------------
    // CRUD
    // -------------------------

    /**
     * Enregistre un nouveau médecin (vérifie l'unicité du numéro d'ordre).
     *  @param medecin     Un medecin de la classe medecin
     * 	@since           1.0
     */
    public void ajouterMedecin(Medecin medecin) {
        if (rechercherParNumeroOrdre(medecin.getNumeroOrdre()) != null) {
            LOGGER.info("ERREUR : Un médecin avec ce numéro d'ordre existe déjà.");
            return;
        }
        medecins.add(medecin);
        LOGGER.info("Médecin ajouté : Dr " + medecin.getNomComplet());
    }

    /**
     * Recherche un médecin par son ID.
     * @param id     L'id d'un médecin pour le rechercher
     * @since           1.0
     * @return un medecin s'il est trouvé sinon null
     */
    public Medecin rechercherParId(int id) {
        for (Medecin m : medecins) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    /**
     * Recherche un médecin par son numéro d'ordre.
     * @param numeroOrdre     Le numéro d'ordre d'un médecin pour le rechercher.
     * @since           1.0
     * @return un medecin s'il est trouvé sinon null
     */
    public Medecin rechercherParNumeroOrdre(String numeroOrdre) {
        for (Medecin m : medecins) {
            if (m.getNumeroOrdre().equalsIgnoreCase(numeroOrdre)) return m;
        }
        return null;
    }

    /**
     * Recherche des médecins par nom ou prénom.
     * @param terme     Le terme pour trouver des médecins.
     * @since           1.0
     * @return une liste de médecin.
     */
    public List<Medecin> rechercherParNom(String terme) {
        List<Medecin> resultats = new ArrayList<>();
        String termeLower = terme.toLowerCase();
        for (Medecin m : medecins) {
            if (m.getNom().toLowerCase().contains(termeLower) ||
                m.getPrenom().toLowerCase().contains(termeLower)) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    /**
     * Retourne tous les médecins d'une spécialité donnée.
     */
    public List<Medecin> rechercherParSpecialite(Specialite specialite) {
        List<Medecin> resultats = new ArrayList<>();
        for (Medecin m : medecins) {
            if (m.getSpecialite() == specialite) {
                resultats.add(m);
            }
        }
        return resultats;
    }

    /**
     * Met à jour les informations d'un médecin.
     */
    public void mettreAJourMedecin(int id, String nouveauNom, String nouveauPrenom, double nouveauTarifConsultation,
                                       String nouveauTel, String nouveauEmail,
                                       Specialite nouvelleSpecialite) {
        Medecin m = rechercherParId(id);
        if (m == null) {
            LOGGER.info("ERREUR : Médecin ID=" + id + " introuvable.");
            return;
        }
        m.setNom(nouveauNom);
        m.setPrenom(nouveauPrenom);
        m.setTarifHoraire(nouveauTarifConsultation);
        m.setTelephone(nouveauTel);
        m.setEmail(nouveauEmail);
        m.setSpecialite(nouvelleSpecialite);
        LOGGER.info("Médecin mis à jour : Dr " + m.getNomComplet());
    }

    /**
     * Supprime un médecin par son ID.
     */
    public boolean supprimerMedecin(int id) {
        Medecin m = rechercherParId(id);
        if (m == null) {
            LOGGER.info("ERREUR : Médecin ID=" + id + " introuvable.");
            return false;
        }
        medecins.remove(m);
        LOGGER.info("Médecin supprimé : Dr " + m.getNomComplet());
        return true;
    }

    /**
     * Retourne la liste complète des médecins.
     */
    public List<Medecin> listerTousLesMedecins() {
        return new ArrayList<>(medecins);
    }

    /**
     * Retourne le nombre total de médecins.
     */
    public int getNombreMedecins() {
        return medecins.size();
    }

}
