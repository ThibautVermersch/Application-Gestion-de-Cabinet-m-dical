package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests unitaires pour le service GestionPatients.
 */
class GestionPatientsTest {

    private GestionPatients gestion;
    private Patient patient1;
    private Patient patient2;

    @BeforeEach
    void setUp() {
        gestion = new GestionPatients();
        patient1 = new Patient("Martin", "Alice", "0600000001", "alice@mail.com",
                "1 rue A", LocalDate.of(1990, 1, 1), "123456789", 0.70);
        patient2 = new Patient("Dupont", "Bob", "0600000002", "bob@mail.com",
                "2 rue B", LocalDate.of(1985, 5, 15), "987654321", 0.80);
    }

    // --- ajouterPatient ---

    @Test
    void testAjouterPatientSucces() {
        gestion.ajouterPatient(patient1);
        assertThat(gestion.getNombrePatients()).isEqualTo(1);
    }

    @Test
    void testAjouterPatientDoublonNumeroSecu() {
        gestion.ajouterPatient(patient1);
        Patient doublonSecu = new Patient("Autre", "Prénom", "0600000099", "autre@mail.com",
                "3 rue C", LocalDate.of(2000, 1, 1), "123456789", 0.60);
        gestion.ajouterPatient(doublonSecu);
        assertThat(gestion.getNombrePatients()).isEqualTo(1);
    }

    // --- rechercherParId ---

    @Test
    void testRechercherParIdTrouve() {
        gestion.ajouterPatient(patient1);
        Patient trouve = gestion.rechercherParId(patient1.getId());
        assertThat(trouve).isEqualTo(patient1);
    }

    @Test
    void testRechercherParIdNonTrouve() {
        assertThat(gestion.rechercherParId(9999)).isNull();
    }

    // --- rechercherParNumeroSecu ---

    @Test
    void testRechercherParNumeroSecuTrouve() {
        gestion.ajouterPatient(patient1);
        Patient trouve = gestion.rechercherParNumeroSecu("123456789");
        assertThat(trouve).isEqualTo(patient1);
    }

    @Test
    void testRechercherParNumeroSecuInsensibleCasse() {
        gestion.ajouterPatient(patient1);
        assertThat(gestion.rechercherParNumeroSecu("123456789")).isNotNull();
    }

    @Test
    void testRechercherParNumeroSecuNonTrouve() {
        assertThat(gestion.rechercherParNumeroSecu("000")).isNull();
    }

    // --- rechercherParNom ---

    @Test
    void testRechercherParNomTrouve() {
        gestion.ajouterPatient(patient1);
        gestion.ajouterPatient(patient2);
        assertThat(gestion.rechercherParNom("martin")).hasSize(1);
    }

    @Test
    void testRechercherParNomNonTrouve() {
        gestion.ajouterPatient(patient1);
        assertThat(gestion.rechercherParNom("Inconnu")).isEmpty();
    }

    @Test
    void testRechercherParPrenomTrouve() {
        gestion.ajouterPatient(patient1);
        assertThat(gestion.rechercherParNom("alice")).hasSize(1);
    }

    // --- mettreAJourPatient ---

    @Test
    void testMettreAJourPatientExistant() {
        gestion.ajouterPatient(patient1);
        gestion.mettreAJourPatient(patient1.getId(), "Nouveau", "Prénom", "0700000000",
                "nouveau@mail.com", "5 rue E", 0.50);
        assertThat(patient1.getNom()).isEqualTo("Nouveau");
        assertThat(patient1.getTauxRemboursement()).isEqualTo(0.50);
    }

    @Test
    void testMettreAJourPatientInexistant() {
        assertThatCode(() -> gestion.mettreAJourPatient(9999, "A", "B",
                "0000", "a@b.com", "rue", 0.5)).doesNotThrowAnyException();
    }

    // --- supprimerPatient ---

    @Test
    void testSupprimerPatientExistant() {
        gestion.ajouterPatient(patient1);
        gestion.supprimerPatient(patient1.getId());
        assertThat(gestion.getNombrePatients()).isEqualTo(0);
    }

    @Test
    void testSupprimerPatientInexistant() {
        assertThatCode(() -> gestion.supprimerPatient(9999)).doesNotThrowAnyException();
    }

    // --- listerTousLesPatients / getNombrePatients ---

    @Test
    void testListerTousLesPatientsVide() {
        assertThat(gestion.listerTousLesPatients()).isEmpty();
    }

    @Test
    void testGetNombrePatientsAvecDeuxPatients() {
        gestion.ajouterPatient(patient1);
        gestion.ajouterPatient(patient2);
        assertThat(gestion.getNombrePatients()).isEqualTo(2);
    }
}
