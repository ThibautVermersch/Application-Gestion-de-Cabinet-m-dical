package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.modele.Medicament;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests unitaires pour le service GestionMedicaments.
 */
class GestionMedicamentsTest {

    private GestionMedicaments gestion;
    private Medicament medicament1;
    private Medicament medicament2;

    @BeforeEach
    void setUp() {
        gestion = new GestionMedicaments();
        medicament1 = new Medicament("Paracetamol", "Antidouleur", 2.50);
        medicament2 = new Medicament("Ibuprofene", "Anti-inflammatoire", 3.80);
    }


    @Test
    void testAjouterMedicamentSucces() {
        gestion.ajouterMedicament(medicament1);
        assertThat(gestion.getNombreMedicaments()).isEqualTo(1);
    }

    @Test
    void testAjouterMedicamentDoublon() {
        gestion.ajouterMedicament(medicament1);
        // Même nom et même prix → doublon
        Medicament doublon = new Medicament("Paracetamol", "Générique", 2.50);
        gestion.ajouterMedicament(doublon);
        assertThat(gestion.getNombreMedicaments()).isEqualTo(1);
    }


    @Test
    void testRechercherParIdTrouve() {
        gestion.ajouterMedicament(medicament1);
        assertThat(gestion.rechercherParId(medicament1.getId())).isEqualTo(medicament1);
    }

    @Test
    void testRechercherParIdNonTrouve() {
        assertThat(gestion.rechercherParId(9999)).isNull();
    }


    @Test
    void testRechercherParNomTrouve() {
        gestion.ajouterMedicament(medicament1);
        gestion.ajouterMedicament(medicament2);
        assertThat(gestion.rechercherParNom("para")).hasSize(1);
    }

    @Test
    void testRechercherParNomNonTrouve() {
        gestion.ajouterMedicament(medicament1);
        assertThat(gestion.rechercherParNom("Aspirine")).isEmpty();
    }

    @Test
    void testRechercherParNomInsensibleCasse() {
        gestion.ajouterMedicament(medicament1);
        assertThat(gestion.rechercherParNom("PARACETAMOL")).hasSize(1);
    }


    @Test
    void testSupprimerMedicamentExistant() {
        gestion.ajouterMedicament(medicament1);
        boolean resultat = gestion.supprimerMedicament(medicament1.getId());
        assertThat(resultat).isTrue();
        assertThat(gestion.getNombreMedicaments()).isEqualTo(0);
    }

    @Test
    void testSupprimerMedicamentInexistant() {
        boolean resultat = gestion.supprimerMedicament(9999);
        assertThat(resultat).isFalse();
    }


    @Test
    void testListerTousLesMedicamentsVide() {
        assertThat(gestion.listerTousLesMedicaments()).isEmpty();
    }

    @Test
    void testGetNombreMedicamentsAvecDeuxMedicaments() {
        gestion.ajouterMedicament(medicament1);
        gestion.ajouterMedicament(medicament2);
        assertThat(gestion.getNombreMedicaments()).isEqualTo(2);
    }
}
