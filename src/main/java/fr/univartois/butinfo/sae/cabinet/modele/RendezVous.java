package fr.univartois.butinfo.sae.cabinet.modele;

import fr.univartois.butinfo.sae.cabinet.enumeration.StatutRendezVous;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe facade représentant un rendez-vous entre un patient et un médecin.
 */
public class RendezVous {

  private final int id;
  private final Patient patient;
  private final Medecin medecin;
  private LocalDateTime dateHeure;
  // Durée du rendez-vous en minutes
  private int dureeMinutes;
  private StatutRendezVous statut;
  // Motif de la consultation
  private String motif;

  // Compteur statique pour les IDs
  private static int compteurId = 1;

  // Format d'affichage de la date et heure
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

  /**
   * Constructeur complet d'un RendezVous. Le statut est initialisé à PLANIFIE par défaut.
   */
  public RendezVous(Patient patient, Medecin medecin, LocalDateTime dateHeure,
      int dureeMinutes, String motif) {
    this.id = compteurId++;
    this.patient = patient;
    this.medecin = medecin;
    this.dateHeure = dateHeure;
    this.dureeMinutes = dureeMinutes;
    this.statut = StatutRendezVous.PLANIFIE;
    this.motif = motif;
  }

  /**
   * Constructeur simplifié avec durée par défaut de 30 minutes.
   */
  public RendezVous(Patient patient, Medecin medecin, LocalDateTime dateHeure, String motif) {
    this(patient, medecin, dateHeure, 30, motif);
  }

  // -------------------------
  // Actions métier
  // -------------------------

  /**
   * Confirme le rendez-vous (passe le statut à CONFIRME).
   */
  public void confirmer() {
    if (this.statut == StatutRendezVous.PLANIFIE) {
      this.statut = StatutRendezVous.CONFIRME;
    }
  }

  /**
   * Annule le rendez-vous (passe le statut à ANNULE).
   */
  public void annuler() {
    if (this.statut != StatutRendezVous.TERMINE) {
      this.statut = StatutRendezVous.ANNULE;
    }
  }

  /**
   * Termine le rendez-vous (passe le statut à TERMINE).
   */
  public void terminer() {
    this.statut = StatutRendezVous.TERMINE;
  }

  // -------------------------
  // Getters et Setters
  // -------------------------

  public int getId() {
    return id;
  }

  public Patient getPatient() {
    return patient;
  }

  public Medecin getMedecin() {
    return medecin;
  }

  public LocalDateTime getDateHeure() {
    return dateHeure;
  }

  public void setDateHeure(LocalDateTime dateHeure) {
    this.dateHeure = dateHeure;
  }

  public int getDureeMinutes() {
    return dureeMinutes;
  }

  public void setDureeMinutes(int dureeMinutes) {
    this.dureeMinutes = dureeMinutes;
  }

  public StatutRendezVous getStatut() {
    return statut;
  }

  public void setStatut(StatutRendezVous statut) {
    this.statut = statut;
  }

  public String getMotif() {
    return motif;
  }

  public void setMotif(String motif) {
    this.motif = motif;
  }

  /**
   * Calcule le prix de la consultation.
   * <p>
   * Le prix de la consultation est fonction
   * <ul>
   *   <li>le tarif horaire du médecin</li>
   *   <li>le nombre de minutes de consultation</li>
   *   <li>le taux de remboursement du patient</li>
   * </ul>
   *
   * @return le prix de la consultation
   */
  public double prixConsultation() {
    // TODO : calculer le prix de la consultation
    return 0.0;
  }


  @Override
  public int hashCode() {
    return patient.hashCode() * 31 + medecin.hashCode() * 17 + dateHeure.hashCode();
  }

  /**
   * Deux rendez-vous sont égaux si même patient, médecin et date/heure.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    RendezVous autre = (RendezVous) obj;
    return this.patient.equals(autre.patient) &&
        this.medecin.equals(autre.medecin) &&
        this.dateHeure.equals(autre.dateHeure);
  }


  @Override
  public String toString() {
    return "RDV      { [ID=" + id + "] " +
        patient.getNomComplet() + " avec Dr " + medecin.getNomComplet() +
        " | " + dateHeure.format(FORMATTER) +
        " (" + dureeMinutes + " min)" +
        " | Motif: " + motif +
        " | Statut: " + statut.getLibelle() +
        " | Coût : " + prixConsultation() + " }";
  }
}
