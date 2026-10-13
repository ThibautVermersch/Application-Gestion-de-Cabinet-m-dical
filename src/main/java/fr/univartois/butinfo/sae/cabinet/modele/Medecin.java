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
   *
   * @param nom le nom du médecin.
   * @param prenom le prénom du médecin.
   * @param telephone le numéro de téléphone du médecin.
   * @param email l'adresse e-mail du médecin.
   * @param numeroOrdre le numéro d'identification au Conseil de l'Ordre des Médecins.
   * @param specialite la spécialité du médecin.
   * @param tarifHoraire le tarif horaire de consultation du médecin.
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
   * Le tarif horaire est fixé par défaut à 30.00.
   *
   * @param nom le nom du médecin.
   * @param prenom le prénom du médecin.
   * @param telephone le numéro de téléphone du médecin.
   * @param email l'adresse e-mail du médecin.
   * @param numeroOrdre le numéro d'identification au Conseil de l'Ordre des Médecins.
   * @param specialite la spécialité du médecin.
   */
  public Medecin(String nom, String prenom, String telephone, String email,
      String numeroOrdre, Specialite specialite) {
    this(nom, prenom, telephone, email, numeroOrdre, specialite, 30.00);
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  /**
   * Retourne le numéro d'identification au Conseil de l'Ordre des Médecins.
   *
   * @return le numéro d'ordre.
   */
  public String getNumeroOrdre() {
    return numeroOrdre;
  }

  /**
   * Retourne la spécialité du médecin.
   *
   * @return la spécialité.
   */
  public Specialite getSpecialite() {
    return specialite;
  }

  /**
   * Définit la spécialité du médecin.
   *
   * @param specialite la nouvelle spécialité.
   */
  public void setSpecialite(Specialite specialite) {
    this.specialite = specialite;
  }


  /**
   * Retourne le tarif horaire de consultation du médecin.
   *
   * @return le tarif horaire.
   */
  public double getTarifHoraire() {
    return tarifHoraire;
  }

  /**
   * Définit le tarif horaire de consultation du médecin.
   *
   * @param tarifHoraire le nouveau tarif horaire.
   */
  public void setTarifHoraire(double tarifHoraire) {
    this.tarifHoraire = tarifHoraire;
  }

  /**
   * Deux médecins sont égaux s'ils ont le même numéro d'ordre.
   *
   * @param obj l'objet à comparer.
   * @return {@code true} si les deux médecins ont le même numéro d'ordre, {@code false} sinon.
   */
  @Override
  public boolean equals(Object obj) {
    if (!super.equals(obj)) {
      return false;
    }
    Medecin autre = (Medecin) obj;
    return this.numeroOrdre.equals(autre.numeroOrdre);
  }

  /**
   * Retourne le code de hachage basé sur le numéro d'ordre.
   *
   * @return le code de hachage du médecin.
   */
  @Override
  public int hashCode() {
    return numeroOrdre.hashCode();
  }

  /**
   * Retourne une représentation textuelle du médecin.
   *
   * @return une chaîne décrivant le médecin, sa spécialité, son numéro d'ordre et son tarif.
   */
  @Override
  public String toString() {
    return "Médecin  { " + super.toString() +
        " | Spécialité: " + specialite.getLibelle() +
        " | N° Ordre: " + numeroOrdre +
        " | tarif: " + tarifHoraire + " }";
  }
}
