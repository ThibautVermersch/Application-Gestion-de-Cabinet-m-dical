package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.enumeration.StatutRendezVous;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.modele.RendezVous;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour le service GestionRendezVous.
 */
@ExtendWith(MockitoExtension.class)
class GestionRendezVousTest {

    @Mock
    private Medecin medecin;

    @Mock
    private Patient patient;

    private GestionRendezVous gestion;
    private LocalDateTime dateRdv;

    @BeforeEach
    void setUp() {
        gestion = new GestionRendezVous();
        dateRdv = LocalDateTime.of(2025, 6, 10, 14, 0);
        when(medecin.getNomComplet()).thenReturn("Jean Dupont");
        when(patient.getNomComplet()).thenReturn("Alice Martin");
    }

    // --- prendreRendezVous ---

    @Test
    void testPrendreRendezVousSucces() {
        RendezVous rdv = gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        assertThat(rdv).isNotNull();
        assertThat(rdv.getMedecin()).isEqualTo(medecin);
        assertThat(rdv.getPatient()).isEqualTo(patient);
        assertThat(gestion.getNombreRendezVous()).isEqualTo(1);
    }

    @Test
    void testPrendreRendezVousEchecCreneauOccupe() {
        gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "RDV 1");

        RendezVous rdv2 = gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "RDV 2");
        assertThat(rdv2).isNull();
        assertThat(gestion.getNombreRendezVous()).isEqualTo(1);
    }

    @Test
    void testPrendreRendezVousApresAnnulation() {
        RendezVous rdv1 = gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "RDV 1");
        gestion.annulerRendezVous(rdv1.getId());

        // Le créneau est libre à nouveau
        RendezVous rdv2 = gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "RDV 2");
        assertThat(rdv2).isNotNull();
    }

    // --- annulerRendezVous ---

    @Test
    void testAnnulerRendezVousExistant() {
        RendezVous rdv = gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        gestion.annulerRendezVous(rdv.getId());
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.ANNULE);
    }

    @Test
    void testAnnulerRendezVousInexistant() {
        // Ne doit pas lever d'exception
        assertThatCode(() -> gestion.annulerRendezVous(9999)).doesNotThrowAnyException();
    }

    // --- confirmerRendezVous ---

    @Test
    void testConfirmerRendezVousExistant() {
        RendezVous rdv = gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        gestion.confirmerRendezVous(rdv.getId());
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.CONFIRME);
    }

    @Test
    void testConfirmerRendezVousInexistant() {
        assertThatCode(() -> gestion.confirmerRendezVous(9999)).doesNotThrowAnyException();
    }

    // --- terminerRendezVous ---

    @Test
    void testTerminerRendezVousExistant() {
        RendezVous rdv = gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        boolean resultat = gestion.terminerRendezVous(rdv.getId());
        assertThat(resultat).isTrue();
        assertThat(rdv.getStatut()).isEqualTo(StatutRendezVous.TERMINE);
    }

    @Test
    void testTerminerRendezVousInexistant() {
        boolean resultat = gestion.terminerRendezVous(9999);
        assertThat(resultat).isFalse();
    }

    // --- rechercherParId ---

    @Test
    void testRechercherParIdTrouve() {
        RendezVous rdv = gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        assertThat(gestion.rechercherParId(rdv.getId())).isEqualTo(rdv);
    }

    @Test
    void testRechercherParIdNonTrouve() {
        assertThat(gestion.rechercherParId(9999)).isNull();
    }

    // --- getRendezVousPatient ---

    @Test
    void testGetRendezVousPatient() {
        gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        when(patient.equals(patient)).thenReturn(true);
        assertThat(gestion.getRendezVousPatient(patient)).hasSize(1);
    }

    // --- getRendezVousMedecin ---

    @Test
    void testGetRendezVousMedecin() {
        gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        when(medecin.equals(medecin)).thenReturn(true);
        assertThat(gestion.getRendezVousMedecin(medecin)).hasSize(1);
    }

    // --- getRendezVousParStatut ---

    @Test
    void testGetRendezVousParStatutPlanifie() {
        gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        assertThat(gestion.getRendezVousParStatut(StatutRendezVous.PLANIFIE)).hasSize(1);
    }

    @Test
    void testGetRendezVousParStatutAnnule() {
        RendezVous rdv = gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        gestion.annulerRendezVous(rdv.getId());
        assertThat(gestion.getRendezVousParStatut(StatutRendezVous.ANNULE)).hasSize(1);
        assertThat(gestion.getRendezVousParStatut(StatutRendezVous.PLANIFIE)).isEmpty();
    }

    // --- estDisponible ---

    @Test
    void testEstDisponibleSansRdv() {
        assertThat(gestion.estDisponible(medecin, dateRdv)).isTrue();
    }

    @Test
    void testEstDisponibleCreneauPris() {
        gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        assertThat(gestion.estDisponible(medecin, dateRdv)).isFalse();
    }

    // --- listerTousLesRendezVous / getNombreRendezVous ---

    @Test
    void testListerTousLesRendezVousVide() {
        assertThat(gestion.listerTousLesRendezVous()).isEmpty();
    }

    @Test
    void testGetNombreRendezVous() {
        gestion.prendreRendezVous(patient, medecin, dateRdv, 30, "Suivi");
        gestion.prendreRendezVous(patient, medecin, dateRdv.plusHours(1), 30, "Bilan");
        assertThat(gestion.getNombreRendezVous()).isEqualTo(2);
    }
}
