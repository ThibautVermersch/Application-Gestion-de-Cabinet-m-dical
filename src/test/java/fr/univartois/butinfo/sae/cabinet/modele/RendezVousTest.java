package fr.univartois.butinfo.sae.cabinet.modele;

import fr.univartois.butinfo.sae.cabinet.enumeration.StatutRendezVous;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour la classe RendezVous.
 */
@ExtendWith(MockitoExtension.class)
class RendezVousTest {

    @Mock
    private Medecin medecin;

    @Mock
    private Patient patient;

    private RendezVous rdv;
    private LocalDateTime dateRdv;

    @BeforeEach
    void setUp() {
        when(medecin.getNomComplet()).thenReturn("Jean Dupont");
        when(patient.getNomComplet()).thenReturn("Alice Martin");
        dateRdv = LocalDateTime.of(2025, 6, 10, 14, 0);
        rdv = new RendezVous(patient, medecin, dateRdv, 30, "Consultation");
    }

    // --- Constructeurs ---

    @Test
    void testConstructeurCompletInitialiseBien() {
        assertThat(rdv.getPatient()).isEqualTo(patient);
        assertThat(rdv.getMedecin()).isEqualTo(medecin);
        assertThat(rdv.getDateHeure()).isEqualTo(dateRdv);
        assertThat(rdv.getDureeMinutes()).isEqualTo(30);
        assertThat(rdv.getMotif()).isEqualTo("Consultation");
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.PLANIFIE);
    }

    @Test
    void testConstructeurSimplifieDefaut30Minutes() {
        RendezVous rdvSimple = new RendezVous(patient, medecin, dateRdv, "Suivi");
        assertThat(rdvSimple.getDureeMinutes()).isEqualTo(30);
        assertThat(rdvSimple.getStatut()).isEqualTo(StatutRendezVous.PLANIFIE);
    }

    // --- confirmer ---

    @Test
    void testConfirmerDepuisPlanifie() {
        rdv.confirmer();
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.CONFIRME);
    }

    @Test
    void testConfirmerDepuisConfirmeNeChangePas() {
        rdv.confirmer();
        rdv.confirmer(); // deuxième appel sans effet
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.CONFIRME);
    }

    @Test
    void testConfirmerDepuisAnnuleNeChangePas() {
        rdv.annuler();
        rdv.confirmer();
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.ANNULE);
    }

    // --- annuler ---

    @Test
    void testAnnulerDepuisPlanifie() {
        rdv.annuler();
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.ANNULE);
    }

    @Test
    void testAnnulerDepuisConfirme() {
        rdv.confirmer();
        rdv.annuler();
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.ANNULE);
    }

    @Test
    void testAnnulerDepuisTermineNeChangePas() {
        rdv.terminer();
        rdv.annuler();
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.TERMINE);
    }

    // --- terminer ---

    @Test
    void testTerminerDepuisPlanifie() {
        rdv.terminer();
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.TERMINE);
    }

    @Test
    void testTerminerDepuisConfirme() {
        rdv.confirmer();
        rdv.terminer();
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.TERMINE);
    }

    // --- prixConsultation ---

    @Test
    void testPrixConsultationAvecMocks() {
        // Tarif 60€/h = 1€/min, durée 30 min, remboursement 70% → patient paie 30% → 9.0
        when(medecin.getTarifHoraire()).thenReturn(60.0);
        when(patient.getTauxRemboursement()).thenReturn(0.70);

        RendezVous rdvCalc = new RendezVous(patient, medecin, dateRdv, 30, "Test");
        assertThat(rdvCalc.prixConsultation()).isCloseTo(9.0, within(0.01));
    }

    @Test
    void testPrixConsultationTauxRemboursementZero() {
        // Aucun remboursement : patient paie 100%
        when(medecin.getTarifHoraire()).thenReturn(60.0);
        when(patient.getTauxRemboursement()).thenReturn(0.0);

        RendezVous rdvCalc = new RendezVous(patient, medecin, dateRdv, 60, "Test");
        // 60/60 * 60 * 1.0 = 60.0
        assertThat(rdvCalc.prixConsultation()).isCloseTo(60.0, within(0.01));
    }

    @Test
    void testPrixConsultationTauxRemboursementTotal() {
        // Remboursement 100% : patient ne paie rien
        when(medecin.getTarifHoraire()).thenReturn(60.0);
        when(patient.getTauxRemboursement()).thenReturn(1.0);

        RendezVous rdvCalc = new RendezVous(patient, medecin, dateRdv, 30, "Test");
        assertThat(rdvCalc.prixConsultation()).isCloseTo(0.0, within(0.01));
    }

    // --- Setters ---

    @Test
    void testSetDateHeure() {
        LocalDateTime nouvelle = LocalDateTime.of(2025, 7, 1, 9, 0);
        rdv.setDateHeure(nouvelle);
        assertThat(rdv.getDateHeure()).isEqualTo(nouvelle);
    }

    @Test
    void testSetDureeMinutes() {
        rdv.setDureeMinutes(45);
        assertThat(rdv.getDureeMinutes()).isEqualTo(45);
    }

    @Test
    void testSetMotif() {
        rdv.setMotif("Bilan annuel");
        assertThat(rdv.getMotif()).isEqualTo("Bilan annuel");
    }

    @Test
    void testSetStatut() {
        rdv.setStatut(StatutRendezVous.CONFIRME);
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.CONFIRME);
    }

    // --- equals / hashCode ---

    @Test
    void testEqualsMemeInstance() {
        assertThat(rdv).isEqualTo(rdv);
    }

    @Test
    void testEqualsMemesChamps() {
        RendezVous rdv2 = new RendezVous(patient, medecin, dateRdv, 60, "Autre motif");
        assertThat(rdv).isEqualTo(rdv2);
    }

    @Test
    void testEqualsDateDifferente() {
        RendezVous rdv2 = new RendezVous(patient, medecin, dateRdv.plusHours(1), 30, "Consultation");
        assertThat(rdv).isNotEqualTo(rdv2);
    }

    @Test
    void testEqualsNull() {
        assertThat(rdv.equals(null)).isFalse();
    }

    @Test
    void testHashCodeCoherent() {
        RendezVous rdv2 = new RendezVous(patient, medecin, dateRdv, 20, "Autre");
        assertThat(rdv.hashCode()).isEqualTo(rdv2.hashCode());
    }

    // --- toString ---

    @Test
    void testToStringContientNomPatientEtMedecin() {
        String str = rdv.toString();
        assertThat(str).contains("Alice Martin").contains("Jean Dupont");
    }
}
