package fr.univartois.butinfo.sae.cabinet.modele;

/**
 * Classe facade abstraite représentant une personne (base commune pour Patient et Medecin).
 * Applique le principe d'encapsulation et sert de superclasse pour l'héritage.
 */
public abstract class Personne {

    // Identifiant unique de la personne
    private final int id;
    protected String nom;
    protected String prenom;
    protected String telephone;
    protected String email;

    // Compteur statique pour générer des IDs uniques automatiquement
    private static int compteurId = 1;

    /**
     * Constructeur complet de Personne.
     */
    public Personne(String nom, String prenom, String telephone, String email) {
        this.id = compteurId++;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
    }

    // -------------------------
    // Getters et Setters
    // -------------------------

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le nom complet (prénom + nom).
     */
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    /**
     * Deux personnes sont égales si elles ont le même ID.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Personne autre = (Personne) obj;
        return this.id == autre.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "[ID=" + id + "] " + prenom + " " + nom +
               " | Tél: " + telephone +
               " | Email: " + email;
    }
}
