package fr.univartois.butinfo.sae.cabinet.modele;

import java.time.LocalDate;
import java.time.Period;

/**
 * Classe facade représentant un patient du cabinet médical.
 * Hérite de la classe abstraite Personne.
 */
public class Patient extends Personne {

    private String adresse;
    private LocalDate dateNaissance;
    // Numéro de sécurité sociale (identifiant médical)
    private final String numeroSecu;
    private double tauxRemboursement; // Taux de remboursement par la sécurité sociale

    /**
     * Constructeur complet d'un Patient.
     *
     * @param nom le nom du patient.
     * @param prenom le prénom du patient.
     * @param telephone le numéro de téléphone du patient.
     * @param email l'adresse e-mail du patient.
     * @param adresse l'adresse postale du patient.
     * @param dateNaissance la date de naissance du patient.
     * @param numeroSecu le numéro de sécurité sociale du patient.
     * @param tauxRemboursement le taux de remboursement par la sécurité sociale.
     */
    public Patient(String nom, String prenom, String telephone, String email,
                   String adresse, LocalDate dateNaissance, String numeroSecu, double tauxRemboursement) {
        super(nom, prenom, telephone, email);
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.numeroSecu = numeroSecu;
        this.tauxRemboursement = tauxRemboursement;
    }

    /**
     * Constructeur simplifié d'un Patient (sans groupe sanguin).
     * Le taux de remboursement est fixé par défaut à 0.75.
     *
     * @param nom le nom du patient.
     * @param prenom le prénom du patient.
     * @param telephone le numéro de téléphone du patient.
     * @param email l'adresse e-mail du patient.
     * @param adresse l'adresse postale du patient.
     * @param dateNaissance la date de naissance du patient.
     * @param numeroSecu le numéro de sécurité sociale du patient.
     */
    public Patient(String nom, String prenom, String telephone, String email,
                   String adresse, LocalDate dateNaissance, String numeroSecu) {
        this(nom, prenom, telephone, email, adresse, dateNaissance, numeroSecu, .75);
    }

    // -------------------------
    // Getters et Setters
    // -------------------------

    /**
     * Retourne l'adresse postale du patient.
     *
     * @return l'adresse.
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Définit l'adresse postale du patient.
     *
     * @param adresse la nouvelle adresse.
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Retourne la date de naissance du patient.
     *
     * @return la date de naissance.
     */
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    /**
     * Définit la date de naissance du patient.
     *
     * @param dateNaissance la nouvelle date de naissance.
     */
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * Retourne le numéro de sécurité sociale du patient.
     *
     * @return le numéro de sécurité sociale.
     */
    public String getNumeroSecu() {
        return numeroSecu;
    }

    // Pas de setter pour numéroSecu : immuable une fois défini


    /**
     * Retourne le taux de remboursement du patient par la sécurité sociale.
     *
     * @return le taux de remboursement.
     */
    public double getTauxRemboursement() {
        return tauxRemboursement;
    }

    /**
     * Définit le taux de remboursement du patient par la sécurité sociale.
     *
     * @param tauxRemboursement le nouveau taux de remboursement.
     */
    public void setTauxRemboursement(double tauxRemboursement) {
        this.tauxRemboursement = tauxRemboursement;
    }

    /**
     * Calcule et retourne l'âge actuel du patient.
     *
     * @return l'âge du patient en années.
     */
    public int getAge() {
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    /**
     * Deux patients sont égaux s'ils ont le même ID et le même numéro de sécurité sociale.
     *
     * @param obj l'objet à comparer.
     * @return {@code true} si les deux patients sont égaux, {@code false} sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        Patient autre = (Patient) obj;
        return this.numeroSecu.equals(autre.numeroSecu);
    }

    /**
     * Retourne le code de hachage basé sur l'ID et le numéro de sécurité sociale.
     *
     * @return le code de hachage du patient.
     */
    @Override
    public int hashCode() {
        return super.hashCode() * 31 + numeroSecu.hashCode();
    }

    /**
     * Retourne une représentation textuelle du patient.
     *
     * @return une chaîne décrivant le patient, sa date de naissance, son âge,
     *         son adresse, son numéro de sécurité sociale et son taux de remboursement.
     */
    @Override
    public String toString() {
        return "Patient  { " + super.toString() +
               " | Naissance: " + dateNaissance +
               " (" + getAge() + " ans)" +
               " | Adresse: " + adresse +
               " | Sécu: " + numeroSecu +
               " | Taux: " + tauxRemboursement + " }";
    }
}
