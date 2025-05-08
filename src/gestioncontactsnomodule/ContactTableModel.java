package gestioncontactsnomodule;

import javax.swing.table.AbstractTableModel; //classe Java utilisée pour gérer les données d’un tableau (JTable)
import java.util.ArrayList;
import java.util.List;

public class ContactTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L; // For serialization
    private final String[] columnNames = {"ID", "Nom", "Prénom", "Téléphone", "Email"};
    private List<Contact> contacts = new ArrayList<>(); // la liste des contacts qu'on va affiche

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        fireTableDataChanged(); //dit à la JTable : « les données ont changé, mets-toi à jour ! »
    }

    public int getContactIdAt(int row) { // savoir quel contact est sélectionné dans la JTable
        return contacts.get(row).getId();
    }

    @Override //Combien de lignes ?
    public int getRowCount() {
        return contacts.size();
    }

    @Override //Combien de colonnes ?
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override // Nom de chaque colonne
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) { //// Récupère le contact de cette ligne
        Contact contact = contacts.get(row);
        switch (column) {
            case 0: return contact.getId();
            case 1: return contact.getNom();
            case 2: return contact.getPrenom();
            case 3: return contact.getTelephone();
            case 4: return contact.getEmail();
            default: return null;
        }
    }
}