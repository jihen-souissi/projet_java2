package gestioncontactsnomodule;

import java.util.List; //gérer des listes de contacts
import java.util.Optional; //gérer des objets(contact) qui peuvent être présents ou absents

public class ContactManager {
    private final ContactDAO dao;   //permet d’accéder à la base de données.

    public ContactManager() {
        this.dao = new ContactDAO();
    }

    public void ajouterContact(Contact contact) throws BusinessException {
        validateContact(contact);// Vérifie que les champs sont valides (nom, prénom...)
        if (dao.existeContact(contact)) {
            throw new BusinessException("Ce contact existe déjà.");
        }
        dao.ajouterContact(contact);
    }

    public List<Contact> getContacts() throws BusinessException {
        return dao.getContacts();// Retourne tous les contacts de la base
    }

    public void modifierContact(int id, Contact nouveauContact) throws BusinessException {
        validateContact(nouveauContact);// Vérifie que les champs sont valides (nom, prénom...)

        Optional<Contact> optionalContact = dao.getContactById(id);
        if (optionalContact.isEmpty()) { //Vérifie si le contact avec cet ID existe.
            throw new BusinessException("Contact introuvable avec l'ID: " + id);
        }

        Contact ancienContact = optionalContact.get(); // Récupère l’ancien contact

        if ((!ancienContact.getNom().equalsIgnoreCase(nouveauContact.getNom()) ||
             !ancienContact.getPrenom().equalsIgnoreCase(nouveauContact.getPrenom()))
            && dao.existeContact(nouveauContact)) {
            throw new BusinessException("Un autre contact avec ce nom et prénom existe déjà.");
        }

        dao.modifierContact(nouveauContact);// On modifie dans la base
    }

    public void supprimerContact(int id) throws BusinessException {
        if (dao.getContactById(id).isEmpty()) {
            throw new BusinessException("Contact introuvable avec l'ID: " + id);
        }
        dao.supprimerContact(id);
    }

    public List<Contact> rechercherContacts(String motCle) throws BusinessException {
        return dao.rechercherContacts(motCle);
    }
    
    
    //Méthode utilisée en interne pour valider qu’un contact est bien rempli.
    private void validateContact(Contact contact) throws BusinessException {
        if (contact == null) {
            throw new BusinessException("Le contact ne peut pas être null.");
        }
        if (contact.getNom() == null || contact.getNom().trim().isEmpty()) {
            throw new BusinessException("Le nom est obligatoire.");
        }
        if (contact.getPrenom() == null || contact.getPrenom().trim().isEmpty()) {
            throw new BusinessException("Le prénom est obligatoire.");
        }
        // Vous pouvez ajouter ici plus de validations si besoin
    }
    
    public Optional<Contact> getContactById(int id) throws BusinessException {
        return dao.getContactById(id); // Retourne un contact s’il existe, sinon vide
    }
}