package gestioncontactsnomodule;

public class Contact {
	private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;

    public Contact() {}

    public Contact(int id, String nom, String prenom, String telephone, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
    }

    public Contact(String nom, String prenom, String telephone, String email) {
        this(0, nom, prenom, telephone, email);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        this.nom = nom.trim();
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom != null ? prenom.trim() : null;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        if (telephone != null && !telephone.matches("^[+0-9\\s-]*$")) {
            throw new IllegalArgumentException("Format de téléphone invalide");
        }
        this.telephone = telephone != null ? telephone.trim() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email != null && !email.isEmpty() && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
        this.email = email != null ? email.trim() : null;
    }

    @Override
    public String toString() {
        return String.format("%s %s - Tel: %s - Email: %s",
                nom,
                prenom != null ? prenom : "",
                telephone != null ? telephone : "N/A",
                email != null ? email : "N/A");
    }

}
