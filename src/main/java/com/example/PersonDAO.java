package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    private Connection connection;

    public PersonDAO() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/persondbswing", "root", "Mysql");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long savePerson(Person person) {
        String sql = "INSERT INTO persons (firstname, name, email, phonenumber, gender) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, person.getFirstname());
            stmt.setString(2, person.getName());
            stmt.setString(3, person.getEmail());
            stmt.setString(4, person.getPhonenumber());
            stmt.setString(5, person.getGender());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Person getPersonById(Long id) {
        String sql = "SELECT * FROM persons WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Person person = new Person();
                person.setId(rs.getLong("id"));
                person.setFirstname(rs.getString("firstname"));
                person.setName(rs.getString("name"));
                person.setEmail(rs.getString("email"));
                person.setPhonenumber(rs.getString("phonenumber"));
                person.setGender(rs.getString("gender"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePerson(Person person) {
        String sql = "UPDATE persons SET firstname = ?, name = ?, email = ?, phonenumber = ?, gender = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getFirstname());
            stmt.setString(2, person.getName());
            stmt.setString(3, person.getEmail());
            stmt.setString(4, person.getPhonenumber());
            stmt.setString(5, person.getGender());
            stmt.setLong(6, person.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(Long id) {
        String sql = "DELETE FROM persons WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Person> getAllPersons() {
        List<Person> persons = new ArrayList<>();
        String sql = "SELECT * FROM persons";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Person person = new Person();
                person.setId(rs.getLong("id"));
                person.setFirstname(rs.getString("firstname"));
                person.setName(rs.getString("name"));
                person.setEmail(rs.getString("email"));
                person.setPhonenumber(rs.getString("phonenumber"));
                person.setGender(rs.getString("gender"));
                persons.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }
}
