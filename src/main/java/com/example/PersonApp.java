package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PersonApp extends JFrame {

    private JTextField firstnameField, nameField, emailField, phonenumberField;
    private JComboBox<String> genderComboBox;
    private DefaultTableModel tableModel;
    private JTable personTable;
    private PersonDAO personDAO;

    public PersonApp() {
        setTitle("Gestion des Personnes");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        personDAO = new PersonDAO();

        // Form fields
        firstnameField = new JTextField(15);
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        phonenumberField = new JTextField(15);
        genderComboBox = new JComboBox<>(new String[]{"Homme", "Femme", "Autre"});

        // Table model and JTable
        tableModel = new DefaultTableModel(new String[]{"ID", "Prénom", "Nom", "Email", "Genre", "Téléphone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // Prevent ID modification
            }
        };
        personTable = new JTable(tableModel);
        personTable.getColumnModel().getColumn(0).setMinWidth(0);
        personTable.getColumnModel().getColumn(0).setMaxWidth(0);
        personTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        // Load data from database
        loadDataFromDatabase();

        // Buttons
        JButton addButton = new JButton("Ajouter");
        JButton detailButton = new JButton("Détails");
        JButton updateButton = new JButton("Mettre à jour");
        JButton deleteButton = new JButton("Supprimer");

        // Form layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 1;
        formPanel.add(firstnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Téléphone :"), gbc);
        gbc.gridx = 1;
        formPanel.add(phonenumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Genre :"), gbc);
        gbc.gridx = 1;
        formPanel.add(genderComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(addButton, gbc);

        // Main layout
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(personTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(detailButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add button action
        addButton.addActionListener(e -> {
            String firstname = firstnameField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String phonenumber = phonenumberField.getText();
            String gender = (String) genderComboBox.getSelectedItem();

            if (!firstname.isEmpty() && !name.isEmpty() && !email.isEmpty()) {
                Person person = new Person();
                person.setFirstname(firstname);
                person.setName(name);
                person.setEmail(email);
                person.setPhonenumber(phonenumber);
                person.setGender(gender);

                Long id = personDAO.savePerson(person);
                tableModel.addRow(new Object[]{id, firstname, name, email, gender, phonenumber});

                firstnameField.setText("");
                nameField.setText("");
                emailField.setText("");
                phonenumberField.setText("");
                genderComboBox.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(PersonApp.this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Detail button action
        detailButton.addActionListener(e -> {
            int selectedRow = personTable.getSelectedRow();
            if (selectedRow != -1) {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                Person person = personDAO.getPersonById(id);
                JOptionPane.showMessageDialog(PersonApp.this,
                        "Prénom : " + person.getFirstname() + "\nNom : " + person.getName() +
                                "\nEmail : " + person.getEmail() + "\nGenre : " + person.getGender() +
                                "\nTéléphone : " + person.getPhonenumber(),
                        "Détails", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(PersonApp.this, "Veuillez sélectionner une ligne", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update button action
        updateButton.addActionListener(e -> {
            int selectedRow = personTable.getSelectedRow();
            if (selectedRow != -1) {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                Person person = personDAO.getPersonById(id);
                person.setFirstname(firstnameField.getText());
                person.setName(nameField.getText());
                person.setEmail(emailField.getText());
                person.setPhonenumber(phonenumberField.getText());
                person.setGender((String) genderComboBox.getSelectedItem());

                personDAO.updatePerson(person);
                tableModel.setValueAt(person.getFirstname(), selectedRow, 1);
                tableModel.setValueAt(person.getName(), selectedRow, 2);
                tableModel.setValueAt(person.getEmail(), selectedRow, 3);
                tableModel.setValueAt(person.getGender(), selectedRow, 4);
                tableModel.setValueAt(person.getPhonenumber(), selectedRow, 5);
            } else {
                JOptionPane.showMessageDialog(PersonApp.this, "Veuillez sélectionner une ligne", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete button action
        deleteButton.addActionListener(e -> {
            int selectedRow = personTable.getSelectedRow();
            if (selectedRow != -1) {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                personDAO.deletePerson(id);
                tableModel.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(PersonApp.this, "Veuillez sélectionner une ligne", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadDataFromDatabase() {
        List<Person> persons = personDAO.getAllPersons();
        for (Person person : persons) {
            tableModel.addRow(new Object[]{
                    person.getId(),
                    person.getFirstname(),
                    person.getName(),
                    person.getEmail(),
                    person.getGender(),
                    person.getPhonenumber()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PersonApp().setVisible(true));
    }
}