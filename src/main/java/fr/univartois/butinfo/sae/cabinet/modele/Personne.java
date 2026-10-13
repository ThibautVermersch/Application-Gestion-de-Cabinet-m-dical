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
     *
     * @param nom le nom de la personne.
     * @param prenom le prénom de la personne.
     * @param telephone le numéro de téléphone de la personne.
     * @param email l'adresse e-mail de la personne.
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

    /**
     * Retourne l'identifiant unique de la personne.
     *
     * @return l'identifiant.
     */
    public int getId() {
        return id;
    }

    /**
     * Retourne le nom unique de la personne.
     *
     * @return le nom.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de la personne.
     *
     * @param nom le nouveau nom.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne le prenom unique de la personne.
     *
     * @return le prenom.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom de la personne.
     *
     * @param prenom le nouveau prénom.
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Retourne le telephone unique de la personne.
     *
     * @return le telephone.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Définit le numéro de téléphone de la personne.
     *
     * @param telephone le nouveau numéro de téléphone.
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Retourne l'email unique de la personne.
     *
     * @return l'email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'adresse e-mail de la personne.
     *
     * @param email la nouvelle adresse e-mail.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le nom complet (prénom + nom).
     *
     * @return le nom complet de la personne.
     */
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    /**
     * Deux personnes sont égales si elles ont le même ID.
     *
     * @param obj l'objet à comparer.
     * @return {@code true} si les deux personnes ont le même ID, {@code false} sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Personne autre = (Personne) obj;
        return this.id == autre.id;
    }

    /**
     * Retourne le code de hachage basé sur l'identifiant de la personne.
     *
     * @return le code de hachage de la personne.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    /**
     * Retourne une représentation textuelle de la personne.
     *
     * @return une chaîne décrivant l'identifiant, le nom, le prénom,
     *         le téléphone et l'email de la personne.
     */
    @Override
    public String toString() {
        return "[ID=" + id + "] " + prenom + " " + nom +
               " | Tél: " + telephone +
               " | Email: " + email;
    }
}
