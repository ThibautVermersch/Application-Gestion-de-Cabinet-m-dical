package fr.univartois.butinfo.sae.cabinet.utils;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.enumeration.StatutRendezVous;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedicaments;
import fr.univartois.butinfo.sae.cabinet.service.GestionOrdonnances;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import fr.univartois.butinfo.sae.cabinet.service.GestionRendezVous;

/**
 * Module de statistiques du cabinet médical.
 * Agrège des informations issues des différents services pour produire un tableau de bord.
 */
public class Statistiques {

    private final GestionPatients gestionPatients;
    private final GestionMedecins gestionMedecins;
    private final GestionMedicaments gestionMedicaments;
    private final GestionRendezVous gestionRendezVous;
    private final GestionOrdonnances gestionOrdonnances;

    /**
     * Constructeur : injecte tous les services nécessaires.
     *
     * @param gestionPatients le service de gestion des patients.
     * @param gestionMedecins le service de gestion des médecins.
     * @param gestionMedicaments le service de gestion des médicaments.
     * @param gestionRendezVous le service de gestion des rendez-vous.
     * @param gestionOrdonnances le service de gestion des ordonnances.
     */
    public Statistiques(GestionPatients gestionPatients,
                        GestionMedecins gestionMedecins,
                        GestionMedicaments gestionMedicaments,
                        GestionRendezVous gestionRendezVous,
                        GestionOrdonnances gestionOrdonnances) {
        this.gestionPatients = gestionPatients;
        this.gestionMedecins = gestionMedecins;
        this.gestionMedicaments = gestionMedicaments;
        this.gestionRendezVous = gestionRendezVous;
        this.gestionOrdonnances = gestionOrdonnances;
    }

    /**
     * Construit un résumé complet des statistiques du cabinet : chiffres globaux,
     * rendez-vous par statut, médecins par spécialité, médecins par nombre de
     * rendez-vous et ordonnances valides.
     *
     * @return une chaîne formatée représentant le tableau de bord du cabinet.
     */
    public String getStatistique() {
        StringBuilder str = new StringBuilder();

        str.append("\n╔══════════════════════════════════════════╗");
        str.append("║        TABLEAU DE BORD - CABINET MÉDICAL  ║");
        str.append("╚══════════════════════════════════════════╝");

        // -- Chiffres globaux --
        str.append("\n📋 CHIFFRES GLOBAUX :");
        str.append("  • Patients enregistrés   : " + gestionPatients.getNombrePatients());
        str.append("  • Médecins enregistrés   : " + gestionMedecins.getNombreMedecins());
        str.append("  • Médicaments référencés : " + gestionMedicaments.getNombreMedicaments());
        str.append("  • Rendez-vous total      : " + gestionRendezVous.getNombreRendezVous());
        str.append("  • Ordonnances émises     : " + gestionOrdonnances.getNombreOrdonnances());

        // -- Détail des rendez-vous par statut --
        str.append("\n📅 RENDEZ-VOUS PAR STATUT :");
        for (StatutRendezVous statut : StatutRendezVous.values()) {
            int nb = gestionRendezVous.getRendezVousParStatut(statut).size();
            str.append("  • " + statut.getLibelle() + " : " + nb);
        }

        // -- Médecins par spécialité --
        str.append("\n🩺 MÉDECINS PAR SPÉCIALITÉ :");
        for (Specialite sp : Specialite.values()) {
            int nb = gestionMedecins.rechercherParSpecialite(sp).size();
            if (nb > 0) {
                str.append("  • " + sp.getLibelle() + " : " + nb);
            }
        }

        // -- Top médecins (par nombre de RDV) --
        str.append("\n🏆 MÉDECINS PAR NOMBRE DE RENDEZ-VOUS :");
        for (Medecin m : gestionMedecins.listerTousLesMedecins()) {
            int nbRdv = gestionRendezVous.getRendezVousMedecin(m).size();
            int nbOrdo = gestionOrdonnances.getOrdonnancesMedecin(m).size();
            str.append("  • Dr " + m.getNomComplet() +
                               " (" + m.getSpecialite().getLibelle() + ")" +
                               " → " + nbRdv + " RDV, " + nbOrdo + " ordonnances");
        }

        // -- Ordonnances valides --
        str.append("\n💊 ORDONNANCES VALIDES (en cours) : " +
                           gestionOrdonnances.getOrdonnancesValides().size());

        str.append("\n══════════════════════════════════════════════\n");

        String res = str.toString();
        return res;
    }

    // -------------------------
    // Méthodes utilitaires
    // -------------------------

    /**
     * Retourne le nombre de rendez-vous pour un statut donné.
     *
     * @param statut le statut recherché.
     * @return le nombre de rendez-vous ayant ce statut.
     */
    public int getNombreRendezVousParStatut(StatutRendezVous statut) {
        return gestionRendezVous.getRendezVousParStatut(statut).size();
    }

    /**
     * Retourne le médecin ayant le plus de rendez-vous.
     *
     * @return le médecin le plus actif, ou {@code null} si aucun médecin n'est enregistré.
     */
    public Medecin getMedecinLePlusActif() {
        Medecin medecinMax = null;
        int max = 0;
        for (Medecin m : gestionMedecins.listerTousLesMedecins()) {
            int nb = gestionRendezVous.getRendezVousMedecin(m).size();
            if (nb > max) {
                max = nb;
                medecinMax = m;
            }
        }
        return medecinMax;
    }

    /**
     * Retourne une représentation textuelle du module de statistiques.
     *
     * @return une chaîne indiquant le nombre de patients, médecins, médicaments,
     *         rendez-vous et ordonnances.
     */
    @Override
    public String toString() {
        return "Statistiques { " +
               "patients=" + gestionPatients.getNombrePatients() +
               ", médecins=" + gestionMedecins.getNombreMedecins() +
               ", médicaments=" + gestionMedicaments.getNombreMedicaments() +
               ", RDV=" + gestionRendezVous.getNombreRendezVous() +
               ", ordonnances=" + gestionOrdonnances.getNombreOrdonnances() + " }";
    }
}
