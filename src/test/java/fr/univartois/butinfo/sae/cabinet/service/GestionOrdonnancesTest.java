package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour le service GestionOrdonnances.
 */
@ExtendWith(MockitoExtension.class)
class GestionOrdonnancesTest {

    @Mock
    private Medecin medecin;

    @Mock
    private Patient patient;

    @Mock
    private Medicament medicament;

    private GestionOrdonnances gestion;
    private LocalDate dateValidite;

    @BeforeEach
    void setUp() {
        gestion = new GestionOrdonnances();
        dateValidite = LocalDate.now().plusMonths(3);
        when(medecin.getNomComplet()).thenReturn("Jean Dupont");
        when(patient.getNomComplet()).thenReturn("Alice Martin");
        when(medicament.getNom()).thenReturn("Paracetamol");
    }

    // --- creerOrdonnance ---

    @Test
    void testCreerOrdonnanceSucces() {
        Ordonnance ordo = gestion.creerOrdonnance(medecin, patient, dateValidite, "RAS");
        assertThat(ordo).isNotNull();
        assertThat(ordo.getMedecin()).isEqualTo(medecin);
        assertThat(ordo.getPatient()).isEqualTo(patient);
        assertThat(gestion.getNombreOrdonnances()).isEqualTo(1);
    }

    // --- ajouterPrescription ---

    @Test
    void testAjouterPrescriptionSurOrdonnanceExistante() {
        Ordonnance ordo = gestion.creerOrdonnance(medecin, patient, dateValidite, "");
        gestion.ajouterPrescription(ordo.getId(), medicament, 2, "Matin", 7, "Avec eau");
        assertThat(ordo.getPrescriptions()).hasSize(1);
    }

    @Test
    void testAjouterPrescriptionSurOrdonnanceInexistante() {
        // Ne doit pas lever d'exception
        assertThatCode(() -> gestion.ajouterPrescription(9999, medicament, 1, "Soir", 5, ""))
                .doesNotThrowAnyException();
    }

    // --- rechercherParId ---

    @Test
    void testRechercherParIdTrouve() {
        Ordonnance ordo = gestion.creerOrdonnance(medecin, patient, dateValidite, "");
        assertThat(gestion.rechercherParId(ordo.getId())).isEqualTo(ordo);
    }

    @Test
    void testRechercherParIdNonTrouve() {
        assertThat(gestion.rechercherParId(9999)).isNull();
    }

    // --- getOrdonnancesPatient ---

    @Test
    void testGetOrdonnancesPatient() {
        gestion.creerOrdonnance(medecin, patient, dateValidite, "");
        when(patient.equals(patient)).thenReturn(true);
        assertThat(gestion.getOrdonnancesPatient(patient)).hasSize(1);
    }

    // --- getOrdonnancesMedecin ---

    @Test
    void testGetOrdonnancesMedecin() {
        gestion.creerOrdonnance(medecin, patient, dateValidite, "");
        when(medecin.equals(medecin)).thenReturn(true);
        assertThat(gestion.getOrdonnancesMedecin(medecin)).hasSize(1);
    }

    // --- getOrdonnancesValides ---

    @Test
    void testGetOrdonnancesValidesAvecDateFuture() {
        gestion.creerOrdonnance(medecin, patient, LocalDate.now().plusDays(10), "");
        assertThat(gestion.getOrdonnancesValides()).hasSize(1);
    }

    @Test
    void testGetOrdonnancesValidesAvecDatePassee() {
        gestion.creerOrdonnance(medecin, patient, LocalDate.now().minusDays(1), "");
        assertThat(gestion.getOrdonnancesValides()).isEmpty();
    }

    // --- listerToutesLesOrdonnances / getNombreOrdonnances ---

    @Test
    void testListerToutesLesOrdonnancesVide() {
        assertThat(gestion.listerToutesLesOrdonnances()).isEmpty();
    }

    @Test
    void testGetNombreOrdonnances() {
        gestion.creerOrdonnance(medecin, patient, dateValidite, "");
        gestion.creerOrdonnance(medecin, patient, dateValidite, "");
        assertThat(gestion.getNombreOrdonnances()).isEqualTo(2);
    }
}
