package fr.univartois.butinfo.sae.cabinet.modele;

/**
 * Classe facade représentant une ligne de prescription dans une ordonnance. Associe un médicament à sa
 * posologie et durée de traitement.
 */
public class LignePrescription {

  private final Medicament medicament;
  private int quantite;
  // Ex : "1 comprimé matin et soir"
  private String posologie;
  // Durée en jours
  private int dureeTraitement;
  private String instructions; // Consignes particulières

  /**
   * Constructeur complet d'une ligne de prescription.
   *
   * @param medicament le médicament prescrit.
   * @param quantite la quantité prescrite.
   * @param posologie la posologie associée à la prescription.
   * @param dureeTraitement la durée du traitement en jours.
   * @param instructions des instructions complémentaires pour la prise du médicament.
   */
  public LignePrescription(Medicament medicament, int quantite, String posologie,
      int dureeTraitement, String instructions) {
    this.medicament = medicament;
    this.quantite = quantite;
    this.posologie = posologie;
    this.dureeTraitement = dureeTraitement;
    this.instructions = instructions;
  }

  /**
   * Constructeur sans instructions spéciales.
   *
   * @param medicament le médicament prescrit.
   * @param posologie la posologie associée à la prescription.
   * @param dureeTraitement la durée du traitement en jours.
   */
  public LignePrescription(Medicament medicament, String posologie, int dureeTraitement) {
    this(medicament, 1, posologie, dureeTraitement, "Aucune instruction particulière");
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

/**
 * Retourne le médicament associé à cette prescription.
 *
 * @return le médicament prescrit.
 */
  public Medicament getMedicament() {
    return medicament;
  }

  /**
   * Retourne la posologie de cette prescription.
   *
   * @return la posologie.
   */
  public String getPosologie() {
    return posologie;
  }

  /**
   * Définit la posologie de cette prescription.
   *
   * @param posologie la nouvelle posologie.
   */
  public void setPosologie(String posologie) {
    this.posologie = posologie;
  }

  /**
   * Retourne la durée du traitement en jours.
   *
   * @return la durée du traitement.
   */
  public int getDureeTraitement() {
    return dureeTraitement;
  }

  /**
   * Définit la durée du traitement en jours.
   *
   * @param dureeTraitement la nouvelle durée du traitement.
   */
  public void setDureeTraitement(int dureeTraitement) {
    this.dureeTraitement = dureeTraitement;
  }

  /**
   * Retourne les instructions particulières de cette prescription.
   *
   * @return les instructions.
   */
  public String getInstructions() {
    return instructions;
  }

  /**
   * Définit les instructions particulières de cette prescription.
   *
   * @param instructions les nouvelles instructions.
   */
  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  /**
   * Retourne la quantité prescrite.
   *
   * @return la quantité.
   */
  public int getQuantite() {
    return quantite;
  }

  /**
   * Définit la quantité prescrite.
   *
   * @param quantite la nouvelle quantité.
   */
  public void setQuantite(int quantite) {
    this.quantite = quantite;
  }

  /**
   * Retourne une représentation textuelle de la ligne de prescription.
   *
   * @return une chaîne décrivant le médicament, la quantité, la posologie,
   *         la durée et les instructions.
   */
  @Override
  public String toString() {
    return "  → " + medicament.getNom() +
        " | Quantité: " + quantite +
        " | Posologie: " + posologie +
        " | Durée: " + dureeTraitement + " jours" +
        " | Instructions: " + instructions;
  }
}
