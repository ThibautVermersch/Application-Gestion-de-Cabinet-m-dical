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
   *
   * @param patient le patient concerné par le rendez-vous.
   * @param medecin le médecin concerné par le rendez-vous.
   * @param dateHeure la date et l'heure du rendez-vous.
   * @param dureeMinutes la durée du rendez-vous en minutes.
   * @param motif le motif de la consultation.
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
   *
   * @param patient le patient concerné par le rendez-vous.
   * @param medecin le médecin concerné par le rendez-vous.
   * @param dateHeure la date et l'heure du rendez-vous.
   * @param motif le motif de la consultation.
   */
  public RendezVous(Patient patient, Medecin medecin, LocalDateTime dateHeure, String motif) {
    this(patient, medecin, dateHeure, 30, motif);
  }

  // -------------------------
  // Actions métier
  // -------------------------

  /**
   * Confirme le rendez-vous (passe le statut à CONFIRME).
   * N'a d'effet que si le rendez-vous est actuellement PLANIFIE.
   */
  public void confirmer() {
    if (this.statut == StatutRendezVous.PLANIFIE) {
      this.statut = StatutRendezVous.CONFIRME;
    }
  }

  /**
   * Annule le rendez-vous (passe le statut à ANNULE).
   * N'a pas d'effet si le rendez-vous est déjà TERMINE.
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

  /**
   * Retourne l'identifiant du rendez-vous.
   *
   * @return l'identifiant.
   */
  public int getId() {
    return id;
  }

  /**
   * Retourne le patient concerné par le rendez-vous.
   *
   * @return le patient.
   */
  public Patient getPatient() {
    return patient;
  }

  /**
   * Retourne le médecin concerné par le rendez-vous.
   *
   * @return le médecin.
   */
  public Medecin getMedecin() {
    return medecin;
  }

  /**
   * Retourne la date et l'heure du rendez-vous.
   *
   * @return la date et l'heure.
   */
  public LocalDateTime getDateHeure() {
    return dateHeure;
  }

  /**
   * Définit la date et l'heure du rendez-vous.
   *
   * @param dateHeure la nouvelle date et heure.
   */
  public void setDateHeure(LocalDateTime dateHeure) {
    this.dateHeure = dateHeure;
  }

  /**
   * Retourne la durée du rendez-vous en minutes.
   *
   * @return la durée en minutes.
   */
  public int getDureeMinutes() {
    return dureeMinutes;
  }

  /**
   * Définit la durée du rendez-vous en minutes.
   *
   * @param dureeMinutes la nouvelle durée en minutes.
   */
  public void setDureeMinutes(int dureeMinutes) {
    this.dureeMinutes = dureeMinutes;
  }

  /**
   * Retourne le statut du rendez-vous.
   *
   * @return le statut.
   */
  public StatutRendezVous getStatut() {
    return statut;
  }

  /**
   * Définit le statut du rendez-vous.
   *
   * @param statut le nouveau statut.
   */
  public void setStatut(StatutRendezVous statut) {
    this.statut = statut;
  }

  /**
   * Retourne le motif de la consultation.
   *
   * @return le motif.
   */
  public String getMotif() {
    return motif;
  }

  /**
   * Définit le motif de la consultation.
   *
   * @param motif le nouveau motif.
   */
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


  /**
   * Retourne le code de hachage basé sur le patient, le médecin et la date/heure.
   *
   * @return le code de hachage du rendez-vous.
   */
  @Override
  public int hashCode() {
    return patient.hashCode() * 31 + medecin.hashCode() * 17 + dateHeure.hashCode();
  }

  /**
   * Deux rendez-vous sont égaux si même patient, médecin et date/heure.
   *
   * @param obj l'objet à comparer.
   * @return {@code true} si les deux rendez-vous sont égaux, {@code false} sinon.
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


  /**
   * Retourne une représentation textuelle du rendez-vous.
   *
   * @return une chaîne décrivant le patient, le médecin, la date/heure,
   *         la durée, le motif, le statut et le coût de la consultation.
   */
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
