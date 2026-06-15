package fr.univartois.butinfo.sae.cabinet.enumeration;

/**
 * Représente les différentes spécialités médicales disponibles au sein du cabinet.
 * Cette énumération permet de classifier les médecins et d'orienter les patients
 * vers le bon praticien.
 *
 * @author VotreNom (optionnel)
 * @version 1.0
 */
public enum Specialite {

    /** Spécialité de médecine générale. */
    GENERALISTE("Médecin généraliste"),

    /** Spécialité liée aux maladies du cœur et des vaisseaux. */
    CARDIOLOGUE("Cardiologie"),

    /** Spécialité liée aux maladies de la peau. */
    DERMATOLOGUE("Dermatologie"),

    /** Spécialité liée à l'appareil génital féminin. */
    GYNECOLOGUE("Gynécologie"),

    /** Spécialité liée au système nerveux. */
    NEUROLOGUE("Neurologie"),

    /** Spécialité liée aux maladies des yeux. */
    OPHTALMOLOGUE("Ophtalmologie"),

    /** Spécialité dédiée à la santé des enfants. */
    PEDIATRE("Pédiatrie"),

    /** Spécialité dédiée aux troubles mentaux. */
    PSYCHIATRE("Psychiatrie"),

    /** Spécialité liée à l'imagerie médicale. */
    RADIOLOGUE("Radiologie"),

    /** Spécialité dédiée à la médecine d'urgence. */
    URGENTISTE("Urgences");

    /**
     * Le libellé complet et lisible de la spécialité médicale.
     */
    private final String libelle;

    /**
     * Constructeur de l'énumération.
     *
     * @param libelle Le nom complet ou libellé de la spécialité.
     */
    Specialite(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé de la spécialité médicale.
     *
     * @return Une chaîne de caractères représentant le nom de la spécialité.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Donne la représentation textuelle de la constante d'énumération.
     * Par défaut, retourne la même valeur que {@link #getLibelle()}.
     *
     * @return Le libellé de la spécialité.
     */
    @Override
    public String toString() {
        return libelle;
    }
}