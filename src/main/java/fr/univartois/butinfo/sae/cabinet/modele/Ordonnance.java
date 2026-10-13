package fr.univartois.butinfo.sae.cabinet.modele;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe facade représentant une ordonnance médicale. Une ordonnance est émise par un médecin pour un
 * patient et contient une liste de lignes de prescription.
 */
public class Ordonnance {

  private final int id;
  private final Medecin medecin;
  private final Patient patient;
  private final LocalDate dateEmission;
  // Date de validité de l'ordonnance
  private LocalDate dateValidite;
  private final List<LignePrescription> prescriptions;
  private String remarques;

  // Compteur statique pour les IDs
  private static int compteurId = 1;

  /**
   * Constructeur complet d'une Ordonnance. La liste des prescriptions est initialisée vide.
   *
   * @param medecin le médecin qui émet l'ordonnance.
   * @param patient le patient concerné par l'ordonnance.
   * @param dateValidite la date de validité de l'ordonnance.
   * @param remarques des remarques éventuelles sur l'ordonnance.
   */
  public Ordonnance(Medecin medecin, Patient patient, LocalDate dateValidite, String remarques) {
    this.id = compteurId++;
    this.medecin = medecin;
    this.patient = patient;
    this.dateEmission = LocalDate.now();
    this.dateValidite = dateValidite;
    this.prescriptions = new ArrayList<>();
    this.remarques = remarques;
  }

  /**
   * Constructeur simplifié (validité 3 mois par défaut, aucune remarque).
   *
   * @param medecin le médecin qui émet l'ordonnance.
   * @param patient le patient concerné par l'ordonnance.
   */
  public Ordonnance(Medecin medecin, Patient patient) {
    this(medecin, patient, LocalDate.now().plusMonths(3), "");
  }

  // -------------------------
  // Gestion des prescriptions
  // -------------------------

  /**
   * Ajoute une ligne de prescription à l'ordonnance.
   *
   * @param ligne la ligne de prescription à ajouter.
   */
  public void ajouterPrescription(LignePrescription ligne) {
    prescriptions.add(ligne);
  }

  /**
   * Supprime une ligne de prescription par son médicament.
   *
   * @param medicament le médicament dont la prescription doit être supprimée.
   * @return {@code true} si une ligne a été supprimée, {@code false} sinon.
   */
  public boolean supprimerPrescription(Medicament medicament) {
    return prescriptions.removeIf(l -> l.getMedicament().equals(medicament));
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  /**
   * Retourne l'identifiant de l'ordonnance.
   *
   * @return l'identifiant.
   */
  public int getId() {
    return id;
  }

  /**
   * Retourne le médecin ayant émis l'ordonnance.
   *
   * @return le médecin.
   */
  public Medecin getMedecin() {
    return medecin;
  }

  /**
   * Retourne le patient concerné par l'ordonnance.
   *
   * @return le patient.
   */
  public Patient getPatient() {
    return patient;
  }

  /**
   * Retourne la date d'émission de l'ordonnance.
   *
   * @return la date d'émission.
   */
  public LocalDate getDateEmission() {
    return dateEmission;
  }

  /**
   * Retourne la date de validité de l'ordonnance.
   *
   * @return la date de validité.
   */
  public LocalDate getDateValidite() {
    return dateValidite;
  }

  /**
   * Définit la date de validité de l'ordonnance.
   *
   * @param dateValidite la nouvelle date de validité.
   */
  public void setDateValidite(LocalDate dateValidite) {
    this.dateValidite = dateValidite;
  }

  /**
   * Retourne la liste des prescriptions de l'ordonnance (copie défensive).
   *
   * @return la liste des lignes de prescription.
   */
  public List<LignePrescription> getPrescriptions() {
    // Retourne une copie défensive pour protéger la liste interne
    return new ArrayList<>(prescriptions);
  }

  /**
   * Retourne les remarques associées à l'ordonnance.
   *
   * @return les remarques.
   */
  public String getRemarques() {
    return remarques;
  }

  /**
   * Définit les remarques associées à l'ordonnance.
   *
   * @param remarques les nouvelles remarques.
   */
  public void setRemarques(String remarques) {
    this.remarques = remarques;
  }

  /**
   * Vérifie si l'ordonnance est encore valide à ce jour.
   *
   * @return {@code true} si l'ordonnance est valide, {@code false} sinon.
   */
  public boolean estValide() {
    return !LocalDate.now().isAfter(dateValidite);
  }

  /**
   * Deux ordonnances sont égales si elles ont le même ID.
   *
   * @param obj l'objet à comparer.
   * @return {@code true} si les deux ordonnances ont le même ID, {@code false} sinon.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Ordonnance o = (Ordonnance) obj;
    return id == o.id; // Deux ordonnances sont égales si même ID
  }

  /**
   * Retourne le code de hachage basé sur l'identifiant de l'ordonnance.
   *
   * @return le code de hachage de l'ordonnance.
   */
  @Override
  public int hashCode() {
    return Integer.hashCode(id);
  }

  /**
   * Calcul du prix total de l'ordonnance en fonction des prescriptions.
   * <p>
   * Le prix total de l'ordonnance est fonction
   * <ul>
   *   <li>du prix de chaque prestation</li>
   *   <li>du taux de remboursement du patient.</li>
   * </ul>
   *
   * @return le prix total de l'ordonnance
   */
  public double getPrixTotal() {
    double prix = 0.0;

    for (int i = 0; i < prescriptions.size(); i++) {
      LignePrescription lp = prescriptions.get(i);
      Medicament medicament = lp.getMedicament();
      double prixMedicament = medicament.getPrix();
      int quantite = lp.getQuantite();
      double prixLigne = prixMedicament * quantite;
      prix = prix + prixLigne;
    }

    double tauxRemboursement = patient.getTauxRemboursement();

    return prix * (1 - tauxRemboursement);
  }

  /**
   * Retourne une représentation textuelle de l'ordonnance, incluant le médecin,
   * le patient, les dates, les prescriptions et le prix total estimé.
   *
   * @return une chaîne décrivant l'ordonnance.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("=== ORDONNANCE N°").append(id).append(" ===\n");
    sb.append("  Médecin  : Dr ").append(medecin.getNomComplet())
        .append(" (").append(medecin.getSpecialite().getLibelle()).append(")\n");
    sb.append("  Patient  : ").append(patient.getNomComplet()).append("\n");
    sb.append("  Émise le : ").append(dateEmission).append("\n");
    sb.append("  Valide jusqu'au : ").append(dateValidite).append("\n");
    sb.append("  Médicaments prescrits :\n");
    for (LignePrescription lp : prescriptions) {
      sb.append(lp.toString()).append("\n");
    }
    sb.append("  Prix total estimé : ").append(String.format("%.2f €", getPrixTotal())).append("\n");
    if (!remarques.isEmpty()) {
      sb.append("  Remarques : ").append(remarques).append("\n");
    }
    return sb.toString();
  }

  public double calculerPrix() {
    return this.getPrixTotal();
  }
}
