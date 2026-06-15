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
     */
    public Patient(String nom, String prenom, String telephone, String email,
                   String adresse, LocalDate dateNaissance, String numeroSecu) {
        this(nom, prenom, telephone, email, adresse, dateNaissance, numeroSecu, .75);
    }

    // -------------------------
    // Getters et Setters
    // -------------------------

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNumeroSecu() {
        return numeroSecu;
    }

    // Pas de setter pour numéroSecu : immuable une fois défini


    public double getTauxRemboursement() {
        return tauxRemboursement;
    }

    public void setTauxRemboursement(double tauxRemboursement) {
        this.tauxRemboursement = tauxRemboursement;
    }

    /**
     * Calcule et retourne l'âge actuel du patient.
     */
    public int getAge() {
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        Patient autre = (Patient) obj;
        return this.numeroSecu.equals(autre.numeroSecu);
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + numeroSecu.hashCode();
    }

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
