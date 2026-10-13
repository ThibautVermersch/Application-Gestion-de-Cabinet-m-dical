package fr.univartois.butinfo.sae.cabinet.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour la classe Ordonnance.
 */
@ExtendWith(MockitoExtension.class)
class OrdonnanceTest {

    @Mock
    private Medecin medecin;

    @Mock
    private Patient patient;

    private Ordonnance ordonnance;

    @BeforeEach
    void setUp() {
        // Stubs nécessaires pour le constructeur et toString
        when(medecin.getNomComplet()).thenReturn("Jean Dupont");
        when(medecin.getSpecialite()).thenReturn(mock(fr.univartois.butinfo.sae.cabinet.enumeration.Specialite.class));
        when(medecin.getSpecialite().getLibelle()).thenReturn("Généraliste");
        when(patient.getNomComplet()).thenReturn("Alice Martin");
        when(patient.getTauxRemboursement()).thenReturn(0.70);

        ordonnance = new Ordonnance(medecin, patient, LocalDate.now().plusMonths(3), "Aucune remarque");
    }

    // --- Constructeur complet ---

    @Test
    void testConstructeurCompletInitialiseBien() {
        assertThat(ordonnance.getMedecin()).isEqualTo(medecin);
        assertThat(ordonnance.getPatient()).isEqualTo(patient);
        assertThat(ordonnance.getRemarques()).isEqualTo("Aucune remarque");
        assertThat(ordonnance.getDateEmission()).isEqualTo(LocalDate.now());
        assertThat(ordonnance.getPrescriptions()).isEmpty();
    }

    @Test
    void testConstructeurSimplifieDateValiditeTroisMois() {
        Ordonnance ordo = new Ordonnance(medecin, patient);
        assertThat(ordo.getDateValidite()).isEqualTo(LocalDate.now().plusMonths(3));
        assertThat(ordo.getRemarques()).isEmpty();
    }

    // --- getId ---

    @Test
    void testGetIdPositif() {
        assertThat(ordonnance.getId()).isPositive();
    }

    // --- estValide ---

    @Test
    void testEstValideQuandDateFuture() {
        ordonnance.setDateValidite(LocalDate.now().plusDays(10));
        assertThat(ordonnance.estValide()).isTrue();
    }

    @Test
    void testEstValideQuandDatePassee() {
        ordonnance.setDateValidite(LocalDate.now().minusDays(1));
        assertThat(ordonnance.estValide()).isFalse();
    }

    @Test
    void testEstValideQuandDateAujourdhui() {
        ordonnance.setDateValidite(LocalDate.now());
        assertThat(ordonnance.estValide()).isTrue();
    }

    // --- ajouterPrescription / getPrescriptions ---

    @Test
    void testAjouterPrescriptionAugmenteLaListe() {
        Medicament medicament = mock(Medicament.class);
        LignePrescription ligne = new LignePrescription(medicament, 2, "Matin", 7, "Avec eau");
        ordonnance.ajouterPrescription(ligne);
        assertThat(ordonnance.getPrescriptions()).hasSize(1);
    }

    @Test
    void testGetPrescriptionsRetourneCopieDéfensive() {
        Medicament medicament = mock(Medicament.class);
        LignePrescription ligne = new LignePrescription(medicament, 1, "Soir", 5, "");
        ordonnance.ajouterPrescription(ligne);

        // Modifier la copie ne doit pas affecter l'ordonnance
        ordonnance.getPrescriptions().clear();
        assertThat(ordonnance.getPrescriptions()).hasSize(1);
    }

    // --- supprimerPrescription ---

    @Test
    void testSupprimerPrescriptionExistante() {
        Medicament medicament = mock(Medicament.class);
        LignePrescription ligne = new LignePrescription(medicament, 1, "Matin", 5, "");
        ordonnance.ajouterPrescription(ligne);

        boolean supprime = ordonnance.supprimerPrescription(medicament);
        assertThat(supprime).isTrue();
        assertThat(ordonnance.getPrescriptions()).isEmpty();
    }

    @Test
    void testSupprimerPrescriptionInexistante() {
        Medicament medicament = mock(Medicament.class);
        boolean supprime = ordonnance.supprimerPrescription(medicament);
        assertThat(supprime).isFalse();
    }

    // --- getPrixTotal ---

    @Test
    void testGetPrixTotalSansPrescriptions() {
        assertThat(ordonnance.getPrixTotal()).isEqualTo(0.0);
    }

    @Test
    void testGetPrixTotalAvecUneMedicament() {
        Medicament medicament = mock(Medicament.class);
        when(medicament.getPrix()).thenReturn(10.0);
        LignePrescription ligne = new LignePrescription(medicament, 2, "Matin", 7, "");
        ordonnance.ajouterPrescription(ligne);

        // prix brut = 10.0 * 2 = 20.0, remboursement 70% → patient paie 30% → 6.0
        assertThat(ordonnance.getPrixTotal()).isCloseTo(6.0, within(0.01));
    }

    @Test
    void testGetPrixTotalAvecPlusieursLignes() {
        Medicament m1 = mock(Medicament.class);
        Medicament m2 = mock(Medicament.class);
        when(m1.getPrix()).thenReturn(5.0);
        when(m2.getPrix()).thenReturn(20.0);

        ordonnance.ajouterPrescription(new LignePrescription(m1, 1, "Matin", 5, ""));
        ordonnance.ajouterPrescription(new LignePrescription(m2, 3, "Soir", 10, ""));

        // prix brut = (5*1) + (20*3) = 5 + 60 = 65, patient paie 30% = 19.5
        assertThat(ordonnance.getPrixTotal()).isCloseTo(19.5, within(0.01));
    }

    // --- calculerPrix (même logique que getPrixTotal) ---

    @Test
    void testCalculerPrixEgalGetPrixTotal() {
        Medicament medicament = mock(Medicament.class);
        when(medicament.getPrix()).thenReturn(8.0);
        ordonnance.ajouterPrescription(new LignePrescription(medicament, 3, "Midi", 5, ""));

        assertThat(ordonnance.calculerPrix()).isCloseTo(ordonnance.getPrixTotal(), within(0.001));
    }

    // --- setters ---

    @Test
    void testSetRemarques() {
        ordonnance.setRemarques("Nouvelle remarque");
        assertThat(ordonnance.getRemarques()).isEqualTo("Nouvelle remarque");
    }

    @Test
    void testSetDateValidite() {
        LocalDate nouvelle = LocalDate.now().plusYears(1);
        ordonnance.setDateValidite(nouvelle);
        assertThat(ordonnance.getDateValidite()).isEqualTo(nouvelle);
    }

    // --- equals / hashCode ---

    @Test
    void testEqualsMemInstance() {
        assertThat(ordonnance).isEqualTo(ordonnance);
    }

    @Test
    void testEqualsDeuxOrdonnancesDifferentesIds() {
        Ordonnance autre = new Ordonnance(medecin, patient);
        assertThat(ordonnance).isNotEqualTo(autre);
    }

    @Test
    void testEqualsNull() {
        assertThat(ordonnance.equals(null)).isFalse();
    }

    @Test
    void testHashCodeBaseSurId() {
        assertThat(ordonnance.hashCode()).isEqualTo(Integer.hashCode(ordonnance.getId()));
    }

    // --- toString ---

    @Test
    void testToStringContientNumeroOrdonnance() {
        String str = ordonnance.toString();
        assertThat(str).contains("ORDONNANCE");
    }
}
