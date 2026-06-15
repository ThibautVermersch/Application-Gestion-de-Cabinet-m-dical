package fr.univartois.butinfo.sae.cabinet.modele;

/**
 * Classe facade représentant un médicament disponible dans le système.
 */
public class Medicament {

  private final int id;
  private String nom;
  private String description;
  // Principe actif du médicament
  private double prix;

  // Compteur statique pour les IDs
  private static int compteurId = 1;

  /**
   * Constructeur complet d'un Médicament.
   *
   * @param nom le nom du médicament.
   * @param description la description ou le principe actif du médicament.
   * @param prix le prix du médicament.
   */
  public Medicament(String nom, String description, double prix) {
    this.id = compteurId++;
    this.nom = nom;
    this.description = description;
    this.prix = prix;
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  /**
   * Retourne l'identifiant du médicament.
   *
   * @return l'identifiant.
   */
  public int getId() {
    return id;
  }

  /**
   * Retourne le nom du médicament.
   *
   * @return le nom.
   */
  public String getNom() {
    return nom;
  }

  /**
   * Définit le nom du médicament.
   *
   * @param nom le nouveau nom.
   */
  public void setNom(String nom) {
    this.nom = nom;
  }

  /**
   * Retourne la description du médicament.
   *
   * @return la description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Définit la description du médicament.
   *
   * @param description la nouvelle description.
   */
  public void setDescription(String description) {
    this.description = description;
  }


  /**
   * Retourne le prix du médicament.
   *
   * @return le prix.
   */
  public double getPrix() {
    return prix;
  }

  /**
   * Définit le prix du médicament.
   *
   * @param prix le nouveau prix.
   */
  public void setPrix(double prix) {
    this.prix = prix;
  }

  /**
   * Deux médicaments sont égaux s'ils ont le même nom et le même prix.
   *
   * @param obj l'objet à comparer.
   * @return {@code true} si les deux médicaments sont égaux, {@code false} sinon.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Medicament autre = (Medicament) obj;
    return this.nom.equalsIgnoreCase(autre.nom) &&
        this.prix == autre.prix;
  }

  /**
   * Retourne le code de hachage basé sur le nom et le prix du médicament.
   *
   * @return le code de hachage du médicament.
   */
  @Override
  public int hashCode() {
    int result = nom.toLowerCase().hashCode();
    result = 31 * result + Double.hashCode(prix);
    return result;
  }

  /**
   * Retourne une représentation textuelle du médicament.
   *
   * @return une chaîne décrivant l'identifiant, le nom, le prix et la description.
   */
  @Override
  public String toString() {
    return "Médicament { [ID=" + id + "] " + nom +
        " | Prix: " + prix +
        " | " + description + " }";
  }
}
