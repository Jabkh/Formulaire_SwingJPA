package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PersonApp extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> genderComboBox;
    private DefaultTableModel tableModel;
    private JTable personTable;
    private PersonDAO personDAO;

    public PersonApp() {
        setTitle("Gestion des Personnes");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        personDAO = new PersonDAO();

        // Création des champs du formulaire
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        genderComboBox = new JComboBox<>(new String[]{"Homme", "Femme", "Autre"});

        // Création du modèle de table et du JTable
        tableModel = new DefaultTableModel(new String[]{"Nom", "Email", "Genre"}, 0);
        personTable = new JTable(tableModel);

        // Chargement des données de la base de données
        loadDataFromDatabase();

        // Création des boutons
        JButton addButton = new JButton("Ajouter");
        JButton detailButton = new JButton("Détails");

        // Disposition du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Genre :"), gbc);
        gbc.gridx = 1;
        formPanel.add(genderComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(addButton, gbc);

        // Ajout du tableau et des boutons au layout principal
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(personTable), BorderLayout.CENTER);
        add(detailButton, BorderLayout.SOUTH);

        // Action du bouton "Ajouter"
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String gender = (String) genderComboBox.getSelectedItem();

                if (!name.isEmpty() && !email.isEmpty()) {
                    Person person = new Person();
                    person.setName(name);
                    person.setEmail(email);
                    person.setGender(gender);

                    personDAO.savePerson(person);
                    tableModel.addRow(new Object[]{name, email, gender});
                    nameField.setText("");
                    emailField.setText("");
                    genderComboBox.setSelectedIndex(0);
                } else {
                    JOptionPane.showMessageDialog(PersonApp.this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Action du bouton "Détails"
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = personTable.getSelectedRow();
                if (selectedRow != -1) {
                    Long id = (Long) personTable.getValueAt(selectedRow, 0);
                    Person person = personDAO.getPersonById(id);
                    JOptionPane.showMessageDialog(PersonApp.this, "Nom : " + person.getName() + "\nEmail : " + person.getEmail() + "\nGenre : " + person.getGender(), "Détails", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(PersonApp.this, "Veuillez sélectionner une ligne", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void loadDataFromDatabase() {
        List<Person> persons = personDAO.getAllPersons();
        for (Person person : persons) {
            tableModel.addRow(new Object[]{person.getName(), person.getEmail(), person.getGender()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PersonApp app = new PersonApp();
            app.setVisible(true);
        });
    }
}
