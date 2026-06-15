package fr.univartois.butinfo.sae.cabinet.enumeration;

/**
 * Représente les différents statuts possibles pour un rendez-vous médical.
 * Cette énumération permet de suivre l'état d'avancement d'un rendez-vous,
 * de sa planification jusqu'à sa finalisation ou son annulation.
 *
 * @author VotreNom (optionnel)
 * @version 1.0
 */
public enum StatutRendezVous {

    /** Le rendez-vous est planifié mais pas encore validé. */
    PLANIFIE("Planifié"),

    /** Le rendez-vous a été confirmé par le cabinet ou le patient. */
    CONFIRME("Confirmé"),

    /** Le rendez-vous a été annulé. */
    ANNULE("Annulé"),

    /** Le rendez-vous a eu lieu et est terminé. */
    TERMINE("Terminé");

    /**
     * Le libellé affichable et compréhensible du statut.
     */
    private final String libelle;

    /**
     * Constructeur privé de l'énumération.
     *
     * @param libelle Le libellé textuel associé au statut.
     */
    StatutRendezVous(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé de ce statut.
     *
     * @return Une chaîne de caractères représentant le statut de manière lisible.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Donne la représentation textuelle de la constante d'énumération.
     * Par défaut, retourne la même valeur que {@link #getLibelle()}.
     *
     * @return Le libellé du statut.
     */
    @Override
    public String toString() {
        return libelle;
    }
}