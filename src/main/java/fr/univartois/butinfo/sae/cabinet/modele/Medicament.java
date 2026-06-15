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

  public int getId() {
    return id;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public double getPrix() {
    return prix;
  }

  public void setPrix(double prix) {
    this.prix = prix;
  }

  /**
   * Deux médicaments sont égaux s'ils ont le même nom et principe actif.
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

  @Override
  public int hashCode() {
    int result = nom.toLowerCase().hashCode();
    result = 31 * result + Double.hashCode(prix);
    return result;
  }

  @Override
  public String toString() {
    return "Médicament { [ID=" + id + "] " + nom +
        " | Prix: " + prix +
        " | " + description + " }";
  }
}
