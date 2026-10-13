package fr.univartois.butinfo.sae.cabinet.service;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests unitaires pour le service GestionMedecins.
 */
class GestionMedecinsTest {

    private GestionMedecins gestion;
    private Medecin medecin1;
    private Medecin medecin2;

    @BeforeEach
    void setUp() {
        gestion = new GestionMedecins();
        medecin1 = new Medecin("Dupont", "Jean", "0600000001", "jean@mail.com",
                "ORD001", Specialite.GENERALISTE, 60.0);
        medecin2 = new Medecin("Martin", "Sophie", "0600000002", "sophie@mail.com",
                "ORD002", Specialite.CARDIOLOGUE, 80.0);
    }

    // --- ajouterMedecin ---

    @Test
    void testAjouterMedecinSucces() {
        gestion.ajouterMedecin(medecin1);
        assertThat(gestion.getNombreMedecins()).isEqualTo(1);
    }

    @Test
    void testAjouterMedecinDoublonNumeroOrdre() {
        gestion.ajouterMedecin(medecin1);
        Medecin doublon = new Medecin("Autre", "Autre", "0600000099", "autre@mail.com",
                "ORD001", Specialite.GENERALISTE, 50.0);
        gestion.ajouterMedecin(doublon);
        assertThat(gestion.getNombreMedecins()).isEqualTo(1);
    }

    // --- rechercherParId ---

    @Test
    void testRechercherParIdTrouve() {
        gestion.ajouterMedecin(medecin1);
        assertThat(gestion.rechercherParId(medecin1.getId())).isEqualTo(medecin1);
    }

    @Test
    void testRechercherParIdNonTrouve() {
        assertThat(gestion.rechercherParId(9999)).isNull();
    }

    // --- rechercherParNumeroOrdre ---

    @Test
    void testRechercherParNumeroOrdreTrouve() {
        gestion.ajouterMedecin(medecin1);
        assertThat(gestion.rechercherParNumeroOrdre("ORD001")).isEqualTo(medecin1);
    }

    @Test
    void testRechercherParNumeroOrdreInsensibleCasse() {
        gestion.ajouterMedecin(medecin1);
        assertThat(gestion.rechercherParNumeroOrdre("ord001")).isEqualTo(medecin1);
    }

    @Test
    void testRechercherParNumeroOrdreNonTrouve() {
        assertThat(gestion.rechercherParNumeroOrdre("INCONNU")).isNull();
    }

    // --- rechercherParNom ---

    @Test
    void testRechercherParNomTrouve() {
        gestion.ajouterMedecin(medecin1);
        gestion.ajouterMedecin(medecin2);
        assertThat(gestion.rechercherParNom("dupont")).hasSize(1);
    }

    @Test
    void testRechercherParNomNonTrouve() {
        assertThat(gestion.rechercherParNom("Inconnu")).isEmpty();
    }

    // --- rechercherParSpecialite ---

    @Test
    void testRechercherParSpecialiteTrouve() {
        gestion.ajouterMedecin(medecin1);
        gestion.ajouterMedecin(medecin2);
        assertThat(gestion.rechercherParSpecialite(Specialite.CARDIOLOGUE)).hasSize(1);
    }

    @Test
    void testRechercherParSpecialiteNonTrouve() {
        gestion.ajouterMedecin(medecin1);
        assertThat(gestion.rechercherParSpecialite(Specialite.DERMATOLOGUE)).isEmpty();
    }

    // --- mettreAJourMedecin ---

    @Test
    void testMettreAJourMedecinExistant() {
        gestion.ajouterMedecin(medecin1);
        gestion.mettreAJourMedecin(medecin1.getId(), "NouveauNom", "NouveauPrenom",
                90.0, "0700000000", "nouveau@mail.com", Specialite.NEUROLOGUE);
        assertThat(medecin1.getNom()).isEqualTo("NouveauNom");
        assertThat(medecin1.getTarifHoraire()).isEqualTo(90.0);
        assertThat(medecin1.getSpecialite()).isEqualTo(Specialite.NEUROLOGUE);
    }

    @Test
    void testMettreAJourMedecinInexistant() {
        assertThatCode(() -> gestion.mettreAJourMedecin(9999, "A", "B",
                50.0, "0000", "a@b.com", Specialite.GENERALISTE)).doesNotThrowAnyException();
    }

    // --- supprimerMedecin ---

    @Test
    void testSupprimerMedecinExistant() {
        gestion.ajouterMedecin(medecin1);
        boolean resultat = gestion.supprimerMedecin(medecin1.getId());
        assertThat(resultat).isTrue();
        assertThat(gestion.getNombreMedecins()).isEqualTo(0);
    }

    @Test
    void testSupprimerMedecinInexistant() {
        boolean resultat = gestion.supprimerMedecin(9999);
        assertThat(resultat).isFalse();
    }

    // --- listerTousLesMedecins / getNombreMedecins ---

    @Test
    void testListerTousLesMedecinsVide() {
        assertThat(gestion.listerTousLesMedecins()).isEmpty();
    }

    @Test
    void testGetNombreMedecinsAvecDeuxMedecins() {
        gestion.ajouterMedecin(medecin1);
        gestion.ajouterMedecin(medecin2);
        assertThat(gestion.getNombreMedecins()).isEqualTo(2);
    }
}
