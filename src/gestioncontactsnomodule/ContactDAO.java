package gestioncontactsnomodule;

import java.sql.*;// Importe tout ce qui est nécessaire pour travailler avec les bases de données (SQL)
import java.util.*;// Importe les outils comme List, ArrayList

import gestioncontactsnomodule.BusinessException;
import gestioncontactsnomodule.Contact;
import gestioncontactsnomodule.DatabaseConnection;

public class ContactDAO {
	public void ajouterContact(Contact contact) throws BusinessException {
        String sql = "INSERT INTO contacts(nom, prenom, telephone, email) VALUES (?, ?, ?, ?)";  // Requête SQL pour insérer un nouveau contact
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Préparation de la requête SQL
            stmt.setString(1, contact.getNom());// Remplace le 1er ? par le nom
            stmt.setString(2, contact.getPrenom());
            stmt.setString(3, contact.getTelephone());
            stmt.setString(4, contact.getEmail());
            stmt.executeUpdate(); // Exécute la requête pour ajouter le contact
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de l'ajout du contact", e);
        }
    }
	
	//Récupérer tous les contacts
    public List<Contact> getContacts() throws BusinessException {
        List<Contact> contacts = new ArrayList<>();// Liste vide qui contiendra tous les contacts
        String sql = "SELECT * FROM contacts";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { //Exécution de la requête

            while (rs.next()) {
                contacts.add(mapRowToContact(rs));// On transforme chaque ligne SQL en objet Contact
            }
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la récupération des contacts", e);
        }
        return contacts;
    }
     // recherche par id
    public Optional<Contact> getContactById(int id) throws BusinessException {
        String sql = "SELECT * FROM contacts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {  // On exécute la requête SQL et on récupère le résultat dans un objet ResultSet
                if (rs.next()) {// Si un résultat est trouvé
                    return Optional.of(mapRowToContact(rs)); //On transforme la ligne du résultat (rs) en un objet Contact
                }
            }
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la recherche du contact", e);
        }
        return Optional.empty(); // Aucun contact trouvé → retour vide
    }

    public void modifierContact(Contact contact) throws BusinessException {
        String sql = "UPDATE contacts SET nom = ?, prenom = ?, telephone = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contact.getNom());
            stmt.setString(2, contact.getPrenom());
            stmt.setString(3, contact.getTelephone());
            stmt.setString(4, contact.getEmail());
            stmt.setInt(5, contact.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la modification du contact", e);
        }
    }

    public void supprimerContact(int id) throws BusinessException {
        String sql = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la suppression du contact", e);
        }
    }
    // recherche par mot clé
    public List<Contact> rechercherContacts(String motCle) throws BusinessException {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contacts WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? OR telephone LIKE ?";
        String like = "%" + motCle + "%";// Pour rechercher un mot dans le texte "LIKE '%ali%'"
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) {
                stmt.setString(i, like);
            }
            try (ResultSet rs = stmt.executeQuery()) { //Exécution de la requête
                while (rs.next()) {
                    contacts.add(mapRowToContact(rs));
                }
            }
        } catch (SQLException e) { //gestion des erreurs
            throw new BusinessException("Erreur lors de la recherche", e);
        }
        return contacts;
    }

    public boolean existeContact(Contact contact) throws BusinessException {
        String sql = "SELECT COUNT(*) FROM contacts WHERE LOWER(nom) = ? AND LOWER(prenom) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contact.getNom().toLowerCase());// Nom en minuscule
            stmt.setString(2, contact.getPrenom().toLowerCase());// preNom en minuscule
            ResultSet rs = stmt.executeQuery();
            rs.next(); //Exécution de la requête
            return rs.getInt(1) > 0;// Si > 0 → le contact existe déjà
        } catch (SQLException e) {
            throw new BusinessException("Erreur lors de la vérification de l'existence du contact", e);
        }
    }
    

    private Contact mapRowToContact(ResultSet rs) throws SQLException {
        Contact c = new Contact(); // Nouveau contact vide
        c.setId(rs.getInt("id")); // Récupère l'ID
        c.setNom(rs.getString("nom")); // Récupère le nom
        c.setPrenom(rs.getString("prenom")); // Récupère le prénom
        c.setTelephone(rs.getString("telephone")); // Récupère le téléphone
        c.setEmail(rs.getString("email")); // Récupère l’email
        return c; // Retourne l’objet rempli
    }

}
