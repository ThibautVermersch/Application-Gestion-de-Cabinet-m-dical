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
   */
  public Ordonnance(Medecin medecin, Patient patient) {
    this(medecin, patient, LocalDate.now().plusMonths(3), "");
  }

  // -------------------------
  // Gestion des prescriptions
  // -------------------------

  /**
   * Ajoute une ligne de prescription à l'ordonnance.
   */
  public void ajouterPrescription(LignePrescription ligne) {
    prescriptions.add(ligne);
  }

  /**
   * Supprime une ligne de prescription par son médicament.
   */
  public boolean supprimerPrescription(Medicament medicament) {
    return prescriptions.removeIf(l -> l.getMedicament().equals(medicament));
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  public int getId() {
    return id;
  }

  public Medecin getMedecin() {
    return medecin;
  }

  public Patient getPatient() {
    return patient;
  }

  public LocalDate getDateEmission() {
    return dateEmission;
  }

  public LocalDate getDateValidite() {
    return dateValidite;
  }

  public void setDateValidite(LocalDate dateValidite) {
    this.dateValidite = dateValidite;
  }

  public List<LignePrescription> getPrescriptions() {
    // Retourne une copie défensive pour protéger la liste interne
    return new ArrayList<>(prescriptions);
  }

  public String getRemarques() {
    return remarques;
  }

  public void setRemarques(String remarques) {
    this.remarques = remarques;
  }

  /**
   * Vérifie si l'ordonnance est encore valide à ce jour.
   */
  public boolean estValide() {
    return !LocalDate.now().isAfter(dateValidite);
  }

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
}
