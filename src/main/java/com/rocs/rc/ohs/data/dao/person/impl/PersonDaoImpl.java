package com.rocs.rc.ohs.data.dao.person.impl;

import com.rocs.rc.ohs.app.model.enums.gender.Gender;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.person.PersonDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Implements the data access operations for persons.
 */

public class PersonDaoImpl implements PersonDao {

    /** The columns selected for a person. */
    private static final String COLUMNS =
            "select id, first_name, middle_name, last_name, email, "
                    + "date_of_birth, gender, address, contact_number "
                    + "from person ";

    /** The SQL statement that selects a person by ID. */
    private static final String SELECT_BY_ID_SQL =
            COLUMNS + "where id = ?";

    /** The SQL statement that selects persons by name. */
    private static final String SELECT_BY_NAME_SQL =
            COLUMNS + "where lower(first_name || ' ' || last_name) like ? "
                    + "or lower(first_name || ' ' || middle_name || ' ' "
                    + "|| last_name) like ?";

    /**
     * Retrieves the person that matches the given ID.
     *
     * @param id The ID of the person to look up.
     * @return The {@link Person}, or null if it is not found.
     */
    @Override
    public Person findById(String id) {

        Person person = null;
        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    person = toPerson(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println(
                    "An exception was thrown while finding a person. "
                            + e.getMessage());
        }
        return person;
    }

    /**
     * Retrieves the persons whose name contains the given text. The
     * search is not case sensitive.
     *
     * @param name The full or partial name to look up.
     * @return The list of matching {@link Person}, or an empty list if
     *         there is no match.
     */
    @Override
    public List<Person> findByName(String name) {

        List<Person> persons = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            return persons;
        }

        String pattern =
                "%" + name.trim().toLowerCase(Locale.ROOT) + "%";

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt =
                     conn.prepareStatement(SELECT_BY_NAME_SQL)) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    persons.add(toPerson(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println(
                    "An exception was thrown while finding persons. "
                            + e.getMessage());
        }
        return persons;
    }

    /**
     * Maps the current row of the result set to a person.
     *
     * @param rs The result set positioned at a person row.
     * @return The mapped {@link Person}.
     * @throws SQLException If a column cannot be read.
     */
    private Person toPerson(ResultSet rs) throws SQLException {

        Person person = new Person();
        person.setId(rs.getString("id"));
        person.setFirstName(rs.getString("first_name"));
        person.setMiddleName(rs.getString("middle_name"));
        person.setLastName(rs.getString("last_name"));
        person.setEmail(rs.getString("email"));

        Timestamp dateOfBirth = rs.getTimestamp("date_of_birth");
        if (dateOfBirth != null) {
            person.setDateOfBirth(dateOfBirth.toLocalDateTime());
        }

        person.setGender(toGender(rs.getString("gender")));
        person.setAddress(rs.getString("address"));
        person.setContactNumber(rs.getString("contact_number"));
        return person;
    }

    /**
     * Converts a database value to its gender.
     *
     * @param value The database value of the gender.
     * @return The matching {@link Gender}, or null if there is none.
     */
    private Gender toGender(String value) {

        for (Gender gender : Gender.values()) {
            if (gender.getValue().equals(value)) {
                return gender;
            }
        }
        return null;
    }
}