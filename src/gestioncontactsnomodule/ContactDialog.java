package gestioncontactsnomodule;


import javax.swing.*;  
import java.awt.*;     // Contient les classes de mise en page comme BorderLayout, GridLayout, etc.


public class ContactDialog extends JDialog {
    
    // Champs de texte pour saisir les informations du contact
    private final JTextField nomField = new JTextField(20);         // 20 colonnes de largeur
    private final JTextField prenomField = new JTextField(20);
    private final JTextField telephoneField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    
    // Indicateur pour savoir si l'utilisateur a validé le formulaire
    private boolean confirmed = false;

    // Constructeur appelé lorsqu'on veut créer un nouveau contact
    public ContactDialog(JFrame parent, String title) {
        this(parent, title, new Contact());  // Appelle l'autre constructeur avec un nouveau Contact vide
    }

    // Constructeur principal, qui peut aussi être utilisé pour modifier un contact existant
    public ContactDialog(JFrame parent, String title, Contact contact) {
        super(parent, title, true);  // Appelle le constructeur de JDialog (modale = true = bloque l'appli tant qu'elle est ouverte)

        // Définition du layout de la fenêtre
        setLayout(new BorderLayout());  // Disposition générale en zones (NORTH, CENTER, SOUTH, etc.)
        setSize(400, 250);              // Taille de la fenêtre
        setLocationRelativeTo(parent);  // Centre la fenêtre par rapport à la fenêtre principale

        // Création d’un panneau avec une grille de 4 lignes et 2 colonnes, espacées de 5 pixels
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Marge intérieure de 10px autour

        // Ajout des labels et champs de texte au panneau de formulaire
        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Prénom:"));
        formPanel.add(prenomField);
        formPanel.add(new JLabel("Téléphone:"));
        formPanel.add(telephoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        // Remplir les champs si un contact existant est passé en paramètre
        nomField.setText(contact.getNom());
        prenomField.setText(contact.getPrenom());
        telephoneField.setText(contact.getTelephone());
        emailField.setText(contact.getEmail());

        // Création du panneau pour les boutons
        JPanel buttonPanel = new JPanel();

        // Création des boutons OK et Annuler
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Annuler");

        // Action à exécuter quand on clique sur "OK"
        okButton.addActionListener(e -> {
            if (validateFields()) {       // Vérifie si les champs obligatoires sont remplis
                confirmed = true;        // L’utilisateur a bien rempli le formulaire
                dispose();               // Ferme la fenêtre de dialogue
            }
        });

        // Action à exécuter quand on clique sur "Annuler"
        cancelButton.addActionListener(e -> dispose());  
        // Ajouter les boutons au panneau
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Ajouter les panneaux à la fenêtre de dialogue
        add(formPanel, BorderLayout.CENTER);  // Le formulaire au centre
        add(buttonPanel, BorderLayout.SOUTH); // Les boutons en bas
    }

    // Méthode privée pour valider les champs obligatoires
    private boolean validateFields() {
        if (nomField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le nom est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;  // On arrête la validation
        }

        
        if (prenomField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le prénom est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;  // Tous les champs obligatoires sont valides
    }

    // Méthode publique pour afficher la boîte de dialogue et attendre que l'utilisateur clique sur un bouton
    public boolean showDialog() {
        setVisible(true);  // Affiche la fenêtre (modale, donc bloque jusqu'à fermeture)
        return confirmed;  // Retourne true si l’utilisateur a cliqué sur OK, sinon false
    }

    // Méthode pour récupérer un objet Contact rempli avec les données saisies
    public Contact getContact() {
        Contact contact = new Contact();  // Crée un nouveau contact vide
        contact.setNom(nomField.getText());            // Récupère le texte du champ "Nom"
        contact.setPrenom(prenomField.getText());      // Champ "Prénom"
        contact.setTelephone(telephoneField.getText());  // Champ "Téléphone"
        contact.setEmail(emailField.getText());        // Champ "Email"
        return contact;  // Retourne le contact rempli
    }
}
