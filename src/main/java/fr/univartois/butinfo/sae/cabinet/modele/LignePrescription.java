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
   */
  public LignePrescription(Medicament medicament, String posologie, int dureeTraitement) {
    this(medicament, 1, posologie, dureeTraitement, "Aucune instruction particulière");
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  public Medicament getMedicament() {
    return medicament;
  }

  public String getPosologie() {
    return posologie;
  }

  public void setPosologie(String posologie) {
    this.posologie = posologie;
  }

  public int getDureeTraitement() {
    return dureeTraitement;
  }

  public void setDureeTraitement(int dureeTraitement) {
    this.dureeTraitement = dureeTraitement;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public int getQuantite() {
    return quantite;
  }

  public void setQuantite(int quantite) {
    this.quantite = quantite;
  }

  @Override
  public String toString() {
    return "  → " + medicament.getNom() +
        " | Quantité: " + quantite +
        " | Posologie: " + posologie +
        " | Durée: " + dureeTraitement + " jours" +
        " | Instructions: " + instructions;
  }
}
