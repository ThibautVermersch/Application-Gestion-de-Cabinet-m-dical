package fr.univartois.butinfo.sae.cabinet.utils;

import fr.univartois.butinfo.sae.cabinet.enumeration.Specialite;
import fr.univartois.butinfo.sae.cabinet.modele.Medecin;
import fr.univartois.butinfo.sae.cabinet.modele.Patient;
import fr.univartois.butinfo.sae.cabinet.service.GestionMedecins;
import fr.univartois.butinfo.sae.cabinet.service.GestionPatients;
import java.util.List;

/**
 * Module de recherche transversale pour patients et médecins.
 * Regroupe les fonctionnalités de recherche avancée.
 */
public class Recherche {

    private final GestionPatients gestionPatients;
    private final GestionMedecins gestionMedecins;

    /**
     * Constructeur : injecte les services nécessaires.
     *
     * @param gestionPatients le service de gestion des patients.
     * @param gestionMedecins le service de gestion des médecins.
     */
    public Recherche(GestionPatients gestionPatients, GestionMedecins gestionMedecins) {
        this.gestionPatients = gestionPatients;
        this.gestionMedecins = gestionMedecins;
    }

    // -------------------------
    // Recherche patients
    // -------------------------

    /**
     * Recherche des patients par nom ou prénom.
     *
     * @param terme le terme à rechercher dans le nom ou le prénom.
     * @return la liste des patients correspondants.
     */
    public List<Patient> rechercherPatientParNom(String terme) {
        List<Patient> resultats = gestionPatients.rechercherParNom(terme);
        afficherResultatsPatients(resultats, "Patients trouvés pour \"" + terme + "\"");
        return resultats;
    }

    /**
     * Recherche un patient par son numéro de sécurité sociale.
     *
     * @param numeroSecu le numéro de sécurité sociale recherché.
     * @return le patient correspondant, ou {@code null} si introuvable.
     */
    public Patient rechercherPatientParSecu(String numeroSecu) {
        Patient p = gestionPatients.rechercherParNumeroSecu(numeroSecu);
        if (p != null) {
            System.out.println("Patient trouvé : " + p);
        } else {
            System.out.println("Aucun patient trouvé pour le numéro de sécu : " + numeroSecu);
        }
        return p;
    }

    // -------------------------
    // Recherche médecins
    // -------------------------

    /**
     * Recherche des médecins par nom ou prénom.
     *
     * @param terme le terme à rechercher dans le nom ou le prénom.
     * @return la liste des médecins correspondants.
     */
    public List<Medecin> rechercherMedecinParNom(String terme) {
        List<Medecin> resultats = gestionMedecins.rechercherParNom(terme);
        afficherResultatsMedecins(resultats, "Médecins trouvés pour \"" + terme + "\"");
        return resultats;
    }

    /**
     * Recherche des médecins par spécialité.
     *
     * @param specialite la spécialité recherchée.
     * @return la liste des médecins correspondants.
     */
    public List<Medecin> rechercherMedecinParSpecialite(Specialite specialite) {
        List<Medecin> resultats = gestionMedecins.rechercherParSpecialite(specialite);
        afficherResultatsMedecins(resultats,
            "Médecins de spécialité \"" + specialite.getLibelle() + "\"");
        return resultats;
    }

    /**
     * Recherche un médecin par son numéro d'ordre.
     *
     * @param numeroOrdre le numéro d'ordre recherché.
     * @return le médecin correspondant, ou {@code null} si introuvable.
     */
    public Medecin rechercherMedecinParOrdre(String numeroOrdre) {
        Medecin m = gestionMedecins.rechercherParNumeroOrdre(numeroOrdre);
        if (m != null) {
            System.out.println("Médecin trouvé : " + m);
        } else {
            System.out.println("Aucun médecin trouvé pour le n° d'ordre : " + numeroOrdre);
        }
        return m;
    }

    // -------------------------
    // Affichage des résultats
    // -------------------------

    /**
     * Affiche la liste des patients trouvés avec un titre descriptif.
     *
     * @param liste la liste des patients à afficher.
     * @param titre le titre décrivant la recherche effectuée.
     */
    private void afficherResultatsPatients(List<Patient> liste, String titre) {
        System.out.println("--- " + titre + " (" + liste.size() + " résultat(s)) ---");
        if (liste.isEmpty()) {
            System.out.println("  Aucun résultat.");
        } else {
            for (Patient p : liste) System.out.println("  " + p);
        }
    }

    /**
     * Affiche la liste des médecins trouvés avec un titre descriptif.
     *
     * @param liste la liste des médecins à afficher.
     * @param titre le titre décrivant la recherche effectuée.
     */
    private void afficherResultatsMedecins(List<Medecin> liste, String titre) {
        System.out.println("--- " + titre + " (" + liste.size() + " résultat(s)) ---");
        if (liste.isEmpty()) {
            System.out.println("  Aucun résultat.");
        } else {
            for (Medecin m : liste) System.out.println("  " + m);
        }
    }

    /**
     * Retourne une représentation textuelle du module de recherche.
     *
     * @return une chaîne indiquant le nombre de patients et de médecins disponibles.
     */
    @Override
    public String toString() {
        return "Module Recherche { patients=" + gestionPatients.getNombrePatients() +
               ", médecins=" + gestionMedecins.getNombreMedecins() + " }";
    }
}
