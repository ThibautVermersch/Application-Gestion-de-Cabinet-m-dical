package fr.univartois.butinfo.sae.cabinet.modele;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;

/**
 * Classe facade représentant un médecin du cabinet. Hérite de la classe abstraite Personne.
 */
public class Medecin extends Personne {

  // Numéro d'identification au Conseil de l'Ordre des Médecins
  private final String numeroOrdre;
  private Specialite specialite;
  private double tarifHoraire; // Tarif de base pour une consultation

  /**
   * Constructeur complet d'un Médecin.
   */
  public Medecin(String nom, String prenom, String telephone, String email,
      String numeroOrdre, Specialite specialite, double tarifHoraire) {
    super(nom, prenom, telephone, email);
    this.numeroOrdre = numeroOrdre;
    this.specialite = specialite;
    this.tarifHoraire = tarifHoraire;
  }

  /**
   * Constructeur simplifié (sans précision du cabinet).
   */
  public Medecin(String nom, String prenom, String telephone, String email,
      String numeroOrdre, Specialite specialite) {
    this(nom, prenom, telephone, email, numeroOrdre, specialite, 30.00);
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  public String getNumeroOrdre() {
    return numeroOrdre;
  }

  public Specialite getSpecialite() {
    return specialite;
  }

  public void setSpecialite(Specialite specialite) {
    this.specialite = specialite;
  }


  public double getTarifHoraire() {
    return tarifHoraire;
  }

  public void setTarifHoraire(double tarifHoraire) {
    this.tarifHoraire = tarifHoraire;
  }

  /**
   * Deux médecins sont égaux s'ils ont le même numéro d'ordre.
   */
  @Override
  public boolean equals(Object obj) {
    if (!super.equals(obj)) {
      return false;
    }
    Medecin autre = (Medecin) obj;
    return this.numeroOrdre.equals(autre.numeroOrdre);
  }

  @Override
  public int hashCode() {
    return numeroOrdre.hashCode();
  }

  @Override
  public String toString() {
    return "Médecin  { " + super.toString() +
        " | Spécialité: " + specialite.getLibelle() +
        " | N° Ordre: " + numeroOrdre +
        " | tarif: " + tarifHoraire + " }";
  }
}
