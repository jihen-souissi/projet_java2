package gestioncontactsnomodule;

import javax.swing.*;        // Composants Swing : JFrame, JTable, JLabel, etc.

import java.awt.*;                          // Classes pour la gestion de l'affichage graphique (layout, couleurs, etc.)

import java.awt.event.ActionEvent;          // Événements liés aux actions (clics de bouton, etc.)

import java.awt.event.ActionListener;       // Interface pour écouter les actions

import java.util.List;                      // Pour utiliser des listes de données (ex: liste de contacts)

import java.util.Optional;                  // Représente une valeur qui peut être présente ou absente (utile pour éviter les null)

import javax.swing.table.JTableHeader;      // Pour personnaliser l'en-tête (titre des colonnes) d'une table

import javax.swing.table.TableModel;        // Interface utilisée pour représenter les données d'un tableau

import javax.swing.table.TableColumnModel;  // Pour manipuler les colonnes d'une table


public class ContactManagerApp extends JFrame { //qui hérite de JFrame

    // Déclaration d’un objet qui gère les opérations sur les contacts (ajouter, modifier, supprimer…)
    private final ContactManager contactManager = new ContactManager();

    // Déclaration des composants de l’interface  
    private JTable contactTable;  //une table pour afficher les contacts
    private ContactTableModel tableModel; //un modèle de table personnalisé

    // Constructeur de la classe
    public ContactManagerApp() {
        initUI();       // Initialise l’interface graphique
        loadContacts(); // Charge les contacts depuis la base de données dans le tableau
    }

    private void initUI() {
        setTitle("Gestionnaire de Contacts");         // Titre affiché en haut de la fenêtre
        setSize(1200, 600);                           // Taille (largeur x hauteur) de la fenêtre
        setDefaultCloseOperation(EXIT_ON_CLOSE);      // Ferme l'application lorsqu'on ferme la fenêtre
        setLocationRelativeTo(null);                  // Centre la fenêtre sur l'écran

        // Création du modèle de données pour la table
        tableModel = new ContactTableModel();

        // Création de la table qui affichera les contacts,
        contactTable = new JTable(tableModel);
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permet de sélectionner une seule ligne à la fois
        contactTable.setAutoCreateRowSorter(true);                          // Active le tri automatique des colonnes

        // Masquer la colonne ID 
        contactTable.getColumnModel().getColumn(0).setMinWidth(0);
        contactTable.getColumnModel().getColumn(0).setMaxWidth(0);
        contactTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        // Création d'un titre visuel pour l'application
        JLabel titleLabel = new JLabel("Gestionnaire de Contacts", JLabel.CENTER); // JLabel centré
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 50));            // Police et taille
        titleLabel.setForeground(new Color(150, 90, 150));                         // Couleur du texte (violet doux)

        // Ajouter une bordure noire autour de la table
        contactTable.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        // Personnalisation des en-têtes de colonnes
        JTableHeader header = contactTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 20));               // Police et style
        header.setForeground(new Color(199, 21, 133));                  // Texte rose foncé
        header.setBackground(new Color(180, 120, 180));                 // Fond rose-violet

        // Ajustement manuel de la largeur des colonnes
        TableColumnModel columnModel = contactTable.getColumnModel();
        columnModel.getColumn(1).setPreferredWidth(200); // Nom
        columnModel.getColumn(2).setPreferredWidth(200); // Prénom
        columnModel.getColumn(3).setPreferredWidth(120); // Téléphone
        columnModel.getColumn(4).setPreferredWidth(250); // Email
        contactTable.setRowHeight(40);                   // Hauteur de chaque ligne

        // Couleur de sélection d'une ligne
        contactTable.setSelectionBackground(new Color(150, 90, 150));  // Violet
        contactTable.setSelectionForeground(Color.WHITE);              // Texte blanc
        contactTable.setFont(new Font("Arial", Font.PLAIN, 16));       // Police du texte des cellules

        // Création d’un panneau pour contenir les boutons (ajouter, modifier, etc.)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Alignement horizontal avec marges
        buttonPanel.setBackground(new Color(224, 102, 153));              // Couleur de fond rose clair

        // Ajout des boutons avec leur action respective
        addButton(buttonPanel, "Ajouter", this::showAddDialog);
        addButton(buttonPanel, "Modifier", this::showEditDialog);
        addButton(buttonPanel, "Supprimer", this::deleteContact);
        addButton(buttonPanel, "Rechercher", this::showSearchDialog);
        addButton(buttonPanel, "Rafraîchir", this::loadContacts);

        // Organisation de la fenêtre principale (layout)
        setLayout(new BorderLayout());                             // Layout en "bords" (Nord, Centre, Sud)
        add(titleLabel, BorderLayout.NORTH);                       // Le titre en haut
        add(new JScrollPane(contactTable), BorderLayout.CENTER);   // La table au centre avec une barre de défilement
        add(buttonPanel, BorderLayout.SOUTH);                      // Les boutons en bas
    }

    // Méthode utilitaire pour créer et ajouter un bouton avec une action personnalisée
    private void addButton(JPanel panel, String text, Runnable action) {
        JButton button = new JButton(text);                                     // Création du bouton avec texte
        button.setBackground(new Color(180, 120, 180));                         // Couleur de fond par défaut
        button.setForeground(new Color(199, 21, 133));                          // Couleur du texte
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));     // Marges internes
        button.setFocusPainted(false);                                         // Ne pas dessiner le contour de sélection
        button.setPreferredSize(new Dimension(150, 50));                       // Taille fixe du bouton
        button.setFont(new Font("Arial", Font.BOLD, 20));                      // Style du texte

        // Effet visuel au survol de la souris (changer la couleur)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(224, 102, 153));  // Couleur plus vive
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(180, 120, 180));  // Couleur de base
            }
        });

        button.addActionListener(e -> action.run()); // Action du bouton (appel de la méthode)
        panel.add(button);                           // Ajout du bouton au panneau
    }

    // Méthode pour charger les contacts depuis la base de données dans la table
    private void loadContacts() {
        try {
            tableModel.setContacts(contactManager.getContacts());  // Récupère et affiche tous les contacts
        } catch (BusinessException e) {
            showError("Erreur lors du chargement des contacts: " + e.getMessage());
        } catch (Exception e) {
            showError("Erreur inattendue: " + e.getMessage());
        }
    }

    // Méthode pour afficher la fenêtre de dialogue d’ajout de contact
    private void showAddDialog() {
        ContactDialog dialog = new ContactDialog(this, "Ajouter un contact"); // Crée une boîte de dialogue
        if (dialog.showDialog()) {  // Si l’utilisateur clique sur "OK"
            try {
                contactManager.ajouterContact(dialog.getContact());  // Ajoute le contact dans la base
                loadContacts();                                      // Recharge les contacts dans la table
            } catch (BusinessException e) {
                showError(e.getMessage());
            }
        }
    }

    // Méthode pour afficher la fenêtre de modification d’un contact sélectionné
    private void showEditDialog() {
        int selectedRow = contactTable.getSelectedRow();  // Index de la ligne sélectionnée
        if (selectedRow < 0) {
            showError("Veuillez sélectionner un contact à modifier");
            return;
        }

        try {
            int id = tableModel.getContactIdAt(selectedRow); // Récupère l'ID du contact à partir du modèle
            Contact contact = contactManager.getContactById(id)
                .orElseThrow(() -> new BusinessException("Contact introuvable"));

            ContactDialog dialog = new ContactDialog(this, "Modifier le contact", contact); // Pré-rempli le formulaire
            if (dialog.showDialog()) {
                Contact updatedContact = dialog.getContact();
                updatedContact.setId(id);                          // Garde le même ID
                contactManager.modifierContact(id, updatedContact); // Mise à jour dans la base
                loadContacts();                                     // Recharge la liste
            }
        } catch (BusinessException e) {
            showError(e.getMessage());
        }
    }

    // Méthode pour afficher une boîte de recherche
    private void showSearchDialog() {
        String searchTerm = JOptionPane.showInputDialog(
            this, "Entrez un terme de recherche:", "Rechercher des contacts", JOptionPane.PLAIN_MESSAGE
        );

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            try {
                tableModel.setContacts(contactManager.rechercherContacts(searchTerm));  // Affiche les résultats
            } catch (BusinessException e) {
                showError(e.getMessage());
            }
        } else {
            loadContacts();  // Si rien n’est saisi, recharge tous les contacts
        }
    }

    // Méthode pour supprimer le contact sélectionné
    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow(); // Ligne sélectionnée
        if (selectedRow < 0) {
            showError("Veuillez sélectionner un contact à supprimer");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this, "Êtes-vous sûr de vouloir supprimer ce contact?", "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = tableModel.getContactIdAt(selectedRow);
                contactManager.supprimerContact(id); // Supprime dans la base
                loadContacts();                     // Recharge la table
            } catch (BusinessException e) {
                showError(e.getMessage());
            }
        }
    }

    // Méthode pour afficher un message d’erreur dans une boîte de dialogue
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    // Méthode principale : point d’entrée de l’application
    public static void main(String[] args) {
        DatabaseConnection.testConnection();  // Vérifie que la base de données fonctionne
        SwingUtilities.invokeLater(() -> {    // Lance l’interface utilisateur sur le thread de l’UI
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // Style du système
                new ContactManagerApp().setVisible(true); // Affiche la fenêtre
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erreur lors du démarrage: " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
